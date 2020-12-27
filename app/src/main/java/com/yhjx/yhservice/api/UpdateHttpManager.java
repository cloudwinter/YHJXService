package com.yhjx.yhservice.api;


import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.vector.update_app.HttpManager;
import com.yhjx.networker.calladater.SSCall;
import com.yhjx.networker.callback.ResultHandler;
import com.yhjx.networker.callback.ResultNotifier;
import com.yhjx.networker.retrofit.Call;
import com.yhjx.networker.retrofit.Callback;
import com.yhjx.networker.retrofit.Response;
import com.yhjx.networker.retrofit.Retrofit;
import com.yhjx.yhservice.RunningContext;
import com.yhjx.yhservice.api.domain.response.VersionUpdateRes;
import com.yhjx.yhservice.util.FilePathProvider;
import com.yhjx.yhservice.util.LogUtils;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;

/**
 * Created by xiayundong on 2020/12/26.
 */
public class UpdateHttpManager implements HttpManager {

    private static final String TAG = "UpdateHttpManager";

    public void downFile(String url,DownloadListener listener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RunningContext.BASEURL)
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build();
        DownService service = retrofit.create(DownService.class);
        Call<ResponseBody> call = service.downloadFile(url);
        call.enqueue(new com.yhjx.networker.retrofit.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                LogUtils.e(TAG,"downFile 成功："+response.body().contentLength());
                String tempPath = FilePathProvider.getAppDownPath()+"temp.apk";
                writeResponseToDisk(tempPath,response,listener);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.e(TAG,"downFile 异常："+t.getMessage());
                listener.onFail("网络错误～");
            }
        });


    }


    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {

    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {
        new ApiModel().updateVersion(new ResultHandler<VersionUpdateRes>() {
            @Override
            protected void onSuccess(VersionUpdateRes data) {
                callBack.onResponse(new Gson().toJson(data));
            }

            @Override
            protected void onFailed(String errCode, String errMsg) {
                callBack.onError(errMsg);
            }
        });
    }

    //LogUtils.e(TAG, e.getMessage());
    //LogUtils.e(TAG, "下载成功:"+response.body().contentLength());

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull FileCallback callback) {
        downFile(url, new DownloadListener() {
            @Override
            public void onStart() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onBefore();
                    }
                });

            }

            @Override
            public void onProgress(int progress,long total) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onProgress((float) progress/100,total);
                    }
                });

            }

            @Override
            public void onFinish(String path) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(new File(path));
                    }
                });

            }

            @Override
            public void onFail(String errorInfo) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(errorInfo);
                    }
                });

            }
        });
    }

    private Handler handler = new Handler(Looper.getMainLooper());



    private static void writeResponseToDisk(String path, Response<ResponseBody> response, DownloadListener downloadListener) {
        //从response获取输入流以及总大小
        writeFileFromIS(new File(path), response.body().byteStream(), response.body().contentLength(), downloadListener);
    }

    private static int sBufferSize = 8192;

    //将输入流写入文件
    private static void writeFileFromIS(File file, InputStream is, long totalLength, DownloadListener downloadListener) {
        //开始下载
        downloadListener.onStart();

        //创建文件
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                downloadListener.onFail("createNewFile IOException");
            }
        }

        OutputStream os = null;
        long currentLength = 0;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[sBufferSize];
            int len;
            while ((len = is.read(data, 0, sBufferSize)) != -1) {
                os.write(data, 0, len);
                currentLength += len;
                //计算当前下载进度
                downloadListener.onProgress((int) (100 * currentLength / totalLength),totalLength);
//                downloadListener.onProgress((int) (currentLength / totalLength),totalLength);
            }
            //下载完成，并返回保存的文件路径
            downloadListener.onFinish(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            downloadListener.onFail("IOException");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public interface DownloadListener {
        void onStart();//下载开始

        void onProgress(int progress,long total);//下载进度

        void onFinish(String path);//下载完成

        void onFail(String errorInfo);//下载失败
    }
}
