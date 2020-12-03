package com.yhjx.yhservice;

import android.app.Activity;
import android.app.Application;
import android.util.Base64;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.yhjx.yhservice.core.AppUncaughtExceptionHandler;
import com.yhjx.yhservice.file.FileUtils;
import com.yhjx.yhservice.util.LogUtils;
import com.yhjx.yhservice.util.PreferenceUtil;
import com.yhjx.yhservice.util.ScreenUtils;
import com.yhjx.yhservice.view.LoggerView;

import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Stack;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    public static final boolean isDebug = true;//是否为调试模式

    private static MyApplication instance;
    private static Stack<Activity> activityStack = new Stack<>();

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
            instance.onCreate();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        LogUtils.e("---", "[MyApplication] onCreate");
        super.onCreate();
        instance = this;
        //初始化PreferenceUtil
        PreferenceUtil.init(this);
        AppUncaughtExceptionHandler.getInstance().init(this);
        initImageLoader();
        initFilePath();
        x.Ext.init(this);
        // 初始化Bugly
        initBugly();
        RunningContext.init(this);

        // 初始化LoggerView
        LoggerView.init(this);
        //默认英文
       // LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_ENGLISH);
    }

    private void initImageLoader() {
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this)) // default = device screen dimensions
                .diskCacheExtraOptions(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this), null)
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initFilePath() {
        FileUtils.getInstance().createFiles(FileUtils.getInstance().getRootPath(), FileUtils.getInstance().getAudioPath(), FileUtils.getInstance().getImagePath(),
                FileUtils.getInstance().getImageTempPath(), FileUtils.getInstance().getPPTUploadPath());
    }

    //往栈中添加activity
    public void addActivity(Activity activity) {
        if (activity != null) {
            activityStack.add(activity);
        }
    }

    //从栈中移出activity
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    //依次销毁activity
    private void finishActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i) != null && !activityStack.get(i).isFinishing()) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    //跳转到登录界面
    public void gotoLoginActivity() {
        //Prefer.getInstance().clearData();
        finishActivity();
//
//        Intent intent = new Intent();
//        intent.setClass(this, LoginActivity.class);
//        Bundle bundle = new Bundle();
//        intent.putExtras(bundle);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }

    //退出登录
    public void exit(Activity activity) {
        //Prefer.getInstance().clearData();
        finishActivity();
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }


    //把list集合转为String
    public static String SceneList2String(List SceneList) throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }

    //把String转为list集合
    @SuppressWarnings("unchecked")
    public static List String2SceneList(String SceneListString) throws IOException, ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List SceneList = (List) objectInputStream.readObject();
        objectInputStream.close();
        return SceneList;
    }
    private void initBugly() {
        /* Bugly SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        * 注意：如果您之前使用过Bugly SDK，请将以下这句注释掉。
        */
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion("1");
        strategy.setAppPackageName("com.yhjx.yhservice");
        strategy.setAppReportDelay(20000);                          //Bugly会在启动20s后联网同步数据

        /*  第三个参数为SDK调试模式开关，调试模式的行为特性如下：
            输出详细的Bugly SDK的Log；
            每一条Crash都会被立即上报；
            自定义日志将会在Logcat中输出。
            建议在测试阶段建议设置成true，发布时设置为false。*/

        CrashReport.initCrashReport(getApplicationContext(), "53df956f2f", true ,strategy);

        Bugly.init(getApplicationContext(), "53df956f2f", false);
    }

}
