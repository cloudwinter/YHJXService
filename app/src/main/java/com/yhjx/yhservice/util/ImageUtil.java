package com.yhjx.yhservice.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;


import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ImageUtil {

	/**
	 * 图像缩放类型 - 适合
	 */
	public static final int SCALE_FIT = 0;
	/**
	 * 图像缩放类型 - 裁剪
	 */
	public static final int SCALE_CROP = 1;

	/**
	 * 启动相机拍照存放照片的临时uri地址
	 */
	private static final String PHOTO_TEMP = "tempPic.jpg";
	/**
	 * 选择图片压缩过后存放的临时uri地址
	 */
	public static final String SELECT_PICTURE_TEMP = "tempSelPic.jpg";

	/**
	 * 
	 * @param drawable
	 *            drawable图片
	 * @param roundPx
	 *            角度
	 * @return
	 * @Description:// 获得圆角图片的方法
	 */

	public static Bitmap toRoundedCornerBitmap(Drawable drawable, float roundPx) {
		Bitmap bitmap = drawableToBitmap(drawable);
		return toRoundedCornerBitmap(bitmap, roundPx);
	}

	/**
	 * 
	 * @param drawable
	 * @return
	 * @Description:将Drawable转化为Bitmap
	 */

	private static Bitmap drawableToBitmap(Drawable drawable) {

		int width = drawable.getIntrinsicWidth();

		int height = drawable.getIntrinsicHeight();

		Bitmap bitmap = Bitmap.createBitmap(width, height,

		drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888

		: Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);

		drawable.setBounds(0, 0, width, height);

		drawable.draw(canvas);

		return bitmap;

	}

	/**
	 * 转换图片成圆角矩形
	 *
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundedCornerBitmap(Bitmap bitmap, float pixels) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect((int) 0, (int) 0, (int) width, (int) height);
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 转换图片成圆形
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * Get images from SD card by path and the name of image
	 *
	 * @param photoName
	 * @return
	 */
	public static Bitmap getBitmapFromDisk(String path, String photoName) {
		Bitmap photoBitmap = BitmapFactory.decodeFile(path + File.separator
				+ photoName + ".png");
		if (photoBitmap == null) {
			return null;
		} else {
			return photoBitmap;
		}
	}

	/**
	 * Get image from SD card by path and the name of image
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean findBitmapFromDisk(String path, String photoName) {
		boolean flag = false;

		if (!TextUtils.isEmpty(path)) {
			File dir = new File(path);
			if (dir.exists()) {
				File folders = new File(path);
				File photoFile[] = folders.listFiles();
				for (int i = 0; i < photoFile.length; i++) {
					String fileName = photoFile[i].getName().split("\\.")[0];
					if (fileName.equals(photoName)) {
						flag = true;
					}
				}
			} else {
				flag = false;
			}
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * Save image to the SD card
	 *
	 * @param photoBitmap
	 * @param photoName
	 * @param path
	 */
	public static void saveBitmapToDisk(Bitmap photoBitmap, String path,
			String photoName) {
		if (!TextUtils.isEmpty(path)) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File photoFile = new File(path, photoName + ".png");
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				// e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				// e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 传的路径包含图片名称
	 */
	public static void saveBitmapToDisk(Bitmap photoBitmap, String path) {
		if (!TextUtils.isEmpty(path)) {
			String dirPath = "";
			dirPath = path.substring(0, path.lastIndexOf("/"));
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File photoFile = new File(path);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				// e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				// e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get rounded corner images
	 *
	 * @param bitmap
	 * @param roundPx
	 *            5 10
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 从文件中读取指定缩放参数的图片
	 *
	 * @param pathName
	 * @param dstWidth
	 * @param dstHeight
	 * @param scaleType
	 * @return
	 */
	public static Bitmap createScaledBitmap(String pathName, int dstWidth,
			int dstHeight, int scaleType) {
		Bitmap unscaledBitmap = decodeFile(pathName, dstWidth, dstHeight,
				scaleType);
		return createScaledBitmap(unscaledBitmap, dstWidth, dstHeight,
				scaleType);
	}

	/**
	 * 从文件中读取保持比例的图片
	 *
	 * @param pathName
	 * @param dstWidth
	 * @param dstHeight
	 * @param scaleType
	 * @return
	 */
	public static Bitmap decodeFile(String pathName, int dstWidth,
			int dstHeight, int scaleType) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateSampleSize(options.outWidth,
				options.outHeight, dstWidth, dstHeight, scaleType);
		Bitmap unscaledBitmap = BitmapFactory.decodeFile(pathName, options);
		return unscaledBitmap;
	}

	/**
	 * 从文件中读取保持比例的图片
	 *
	 * @param pathName
	 * @param dstWidth
	 * @param dstHeight
	 * @param scaleType
	 * @param config
	 * @return
	 */
	public static Bitmap decodeFile(String pathName, int dstWidth,
			int dstHeight, int scaleType, Config config) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = config;
		options.inSampleSize = calculateSampleSize(options.outWidth,
				options.outHeight, dstWidth, dstHeight, scaleType);
		Bitmap unscaledBitmap = BitmapFactory.decodeFile(pathName, options);
		return unscaledBitmap;
	}

	public static int calculateSampleSize(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, int scaleType) {
		if (scaleType == SCALE_FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return srcWidth / dstWidth;
			} else {
				return srcHeight / dstHeight;
			}
		} else {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return srcHeight / dstHeight;
			} else {
				return srcWidth / dstWidth;
			}
		}
	}

	/**
	 * 获取缩放倍数
	 *
	 * @param srcWidth
	 * @param srcHeight
	 * @param dstWidth
	 * @param dstHeight
	 * @param scaleType
	 * @return
	 */
	public static int findBestSampleSize(int actualWidth, int actualHeight,
			int desiredWidth, int desiredHeight) {
		if (actualWidth <= 0 || actualHeight <= 0 || desiredWidth <= 0
				|| desiredHeight <= 0) {
			return 1;
		}
		double wr = (double) actualWidth / desiredWidth;
		double hr = (double) actualHeight / desiredHeight;
		int ratio = Integer.parseInt(new java.text.DecimalFormat("0")
				.format(Math.min(wr, hr)));
		int n = 1;
		while ((n * 2) <= ratio) {
			n *= 2;
		}
		return n;
	}

	/**
	 * 创建指定缩放类型的图片
	 *
	 * @param unscaledBitmap
	 * @param dstWidth
	 * @param dstHeight
	 * @param scaleType
	 * @return
	 */
	public static Bitmap createScaledBitmap(Bitmap unscaledBitmap,
			int dstWidth, int dstHeight, int scaleType) {
		Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight, scaleType);
		Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight, scaleType);

		Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(),
				dstRect.height(), Config.ARGB_8888);
		Canvas canvas = new Canvas(scaledBitmap);
		canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(
				Paint.FILTER_BITMAP_FLAG));
		return scaledBitmap;
	}

	private static Rect calculateSrcRect(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, int scaleType) {
		if (scaleType == SCALE_CROP) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				final int srcRectWidth = (int) (srcHeight * dstAspect);
				final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
				return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth,
						srcHeight);
			} else {
				final int srcRectHeight = (int) (srcWidth / dstAspect);
				final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
				return new Rect(0, scrRectTop, srcWidth, scrRectTop
						+ srcRectHeight);
			}
		} else {
			return new Rect(0, 0, srcWidth, srcHeight);
		}
	}

	private static Rect calculateDstRect(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, int scaleType) {
		if (scaleType == SCALE_FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;
			if (srcAspect > dstAspect) {
				return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
			} else {
				return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
			}
		} else {
			return new Rect(0, 0, dstWidth, dstHeight);
		}
	}


	/**
	 * 字节数组转化成图片
	 *
	 * @param imgBytes
	 * @return
	 */
	public static Bitmap bytes2Bimap(byte[] imgBytes) {
		if (imgBytes.length != 0) {
			try {
				return BitmapFactory.decodeByteArray(imgBytes, 0,
						imgBytes.length);
			} catch (Exception e) {
			}
		}
		return null;

	}

	/**
	 * 图片比例压缩
	 *
	 * @param image
	 * @return
	 */
	public static Bitmap compressZoom(Bitmap image) {
		return compressZoom(image, 0, 0, null, null);
	}

	/**
	 * 图片比例压缩
	 *
	 * @param image
	 * @param be
	 *            图片压缩比例
	 * @param fileSize
	 *            图片压缩后的大小
	 * @return
	 */
	public static Bitmap compressZoom(Bitmap image, int be, int fileSize,
			Bitmap.CompressFormat compressFormat,
			Options compressOpts) {
		if (null == image) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
		if (baos.toByteArray().length / 1024 > 1024) {
			baos.reset();
			// 这里压缩50%，把压缩后的数据存放到baos中
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Options newOpts = new Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		if (be <= 0) {
			float hh = 800f;// 这里设置高度为800f
			float ww = 480f;// 这里设置宽度为480f
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			// 如果宽度大的话根据宽度固定大小缩放
			if (w > h && w > ww) {
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0) {
				be = 1;// be=1表示不缩放
			}
		}
		// 设置缩放比例
		newOpts.inSampleSize = be;
		// 降低图片从ARGB888到RGB565
		newOpts.inPreferredConfig = Config.RGB_565;
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap, fileSize, compressFormat, compressOpts);
	}

	/**
	 * 图片比例压缩
	 *
	 * @param image
	 * @param be
	 *            图片压缩比例
	 * @return
	 */
	public static Bitmap compressZoom(Bitmap image, int be) {
		if (null == image) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Options newOpts = new Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		if (be <= 0) {
			float hh = 800f;// 这里设置高度为800f
			float ww = 480f;// 这里设置宽度为480f
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			// 如果宽度大的话根据宽度固定大小缩放
			if (w > h && w > ww) {
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0) {
				be = 1;// be=1表示不缩放
			}
		}
		// 设置缩放比例
		newOpts.inSampleSize = be;
		// 降低图片从ARGB888到RGB565
		newOpts.inPreferredConfig = Config.RGB_565;
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return bitmap;
	}

	/**
	 * 图片质量压缩
	 *
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		return compressImage(image, 0, null, null);
	}

	/**
	 * 图片质量压缩
	 *
	 * @param image
	 * @param fileSize
	 *            图片压缩后的大小
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image, int fileSize,
			Bitmap.CompressFormat compressFormat, Options opts) {
		if (null == image) {
			return null;
		}
		if (compressFormat == null) {
			compressFormat = Bitmap.CompressFormat.JPEG;
		}
		if (fileSize <= 0) {
			fileSize = 100;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(compressFormat, 100, baos);
		if (compressFormat != Bitmap.CompressFormat.PNG) {
			int options = 100;
			// 循环判断如果压缩后图片是否大于100KB,大于继续压缩
			while (options > 0 && baos.toByteArray().length / 1024 >= fileSize) {
				baos.reset();// 重置baos即清空baos
				options -= 10;// 每次都减少10
				image.compress(compressFormat, options, baos);

			}
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, opts);
		return bitmap;
	}

	/**
	 * 获取纵向的图片
	 *
	 * @param filePath
	 * @return
	 */
	public static Bitmap getVerticalBitmap(String filePath) {

		if (TextUtils.isEmpty(filePath)) {
			return null;
		}

		ExifInterface exifInterface = null;
		try {
			exifInterface = new ExifInterface(filePath);
		} catch (IOException e) {
			return null;
		}
		int tag = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				-1);
		// 角度旋转
		int degree = 0;
		if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
			degree = 90;
		} else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
			degree = 180;
		} else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
			degree = 270;
		}
		Bitmap bitmap = decodeFile(filePath, 1280, 720, SCALE_FIT);
		if (degree != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degree, (float) bitmap.getWidth() / 2,
					(float) bitmap.getHeight() / 2);
			return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
		}

		return bitmap;
	}

	/**
	 * bitmap修改为黑白图片
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toGrayscale(Bitmap graymap) {
		// 得到图形的宽度和长度
		int width = graymap.getWidth();
		int height = graymap.getHeight();
		// 创建二值化图像
		Bitmap binarymap = null;
		binarymap = graymap.copy(Config.ARGB_8888, true);
		// 依次循环，对图像的像素进行处理
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// 得到当前像素的值
				int col = binarymap.getPixel(i, j);
				// 得到alpha通道的值
				int alpha = col & 0xFF000000;
				// 得到图像的像素RGB的值
				int red = (col & 0x00FF0000) >> 16;
				int green = (col & 0x0000FF00) >> 8;
				int blue = (col & 0x000000FF);
				// 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
				// 图像知识：byqiuchunlong
				// 当其权值取不同的值时，能够形成不同灰度的灰度图象，由于人眼对绿色的敏感度最高，红色次之，
				// 蓝色最低，因此当wg > wr > wb时，所产生的灰度图像更符合人眼的视觉感受。
				// 通常wr=30%，wg=59%，wb=11%，图像的灰度最合理。
				int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
				// 对图像进行二值化处理
				// 再此不做二值话处理
				// if (gray <= 95) {
				// gray = 0;
				// } else {
				// gray = 255;
				// }
				// 新的ARGB
				int newColor = alpha | (gray << 16) | (gray << 8) | gray;
				// 设置新图像的当前像素值
				binarymap.setPixel(i, j, newColor);
			}
		}
		return binarymap;
	}

	/**
	 * 根据路径获取一个压缩后的图片(压缩比例480*800或者800*480，压缩质量80)，根据exif旋转图片
	 */
	public static Bitmap getBitmapFromPathAndCompress(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		// 先保存exif中的orintation信息
		ExifInterface exifInterface = null;
		try {
			exifInterface = new ExifInterface(path);
		} catch (IOException e) {
			return null;
		}
		int tag = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				-1);
		// 开始压缩尺寸和质量
		Options options = new Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Config.RGB_565;
		BitmapFactory.decodeFile(path, options);
		int be = 1;
		int w = options.outWidth;
		int h = options.outHeight;
		float ww = 480f;// 这里设置宽度为480f
		float hh = 800f;// 这里设置高度为800f
		// 缩放比。宽度缩放到480或者高度缩放到800
		if (w >= h && w > ww && h > hh) {
			be = (int) (h / hh);
		} else if (w < h && h > hh && w > ww) {
			be = (int) (w / ww);
		}
		if (be <= 0) {
			be = 1;// be=1表示不缩放
		}
		// 设置缩放比例
		options.inSampleSize = be;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 质量压缩
		int quality = 80;
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		// 循环判断如果压缩后图片是否大于1000KB,大于继续压缩
		while (quality > 0 && baos.toByteArray().length / 1024 >= 1024) {
			baos.reset();// 重置baos即清空baos
			quality -= 10;// 每次都减少10
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, options);
		// 开始角度旋转
		int degree = 0;
		if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
			degree = 90;
		} else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
			degree = 180;
		} else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
			degree = 270;
		}
		if (degree != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degree, (float) bitmap.getWidth() / 2,
					(float) bitmap.getHeight() / 2);
			return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
		}
		return bitmap;
	}


	/**
	 * 根据路径获取一个压缩后的图片路径(压缩比例480*800或者800*480，压缩质量80)，
	 */
	public static String getTempPathFromPathAndCompress(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		File tempFile = getTempFile();
		// 开始压缩尺寸和质量
		Options options = new Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Config.RGB_565;
		BitmapFactory.decodeFile(path, options);
		int be = 1;
		int w = options.outWidth;
		int h = options.outHeight;
		float ww = 600f;// 这里设置宽度为480f
		float hh = 800f;// 这里设置高度为800f
		// 缩放比。宽度缩放到480或者高度缩放到800
		if (w >= h && w > ww && h > hh) {
			be = (int) (h / hh);
		} else if (w < h && h > hh && w > ww) {
			be = (int) (w / ww);
		}
		if (be <= 0) {
			be = 1;// be=1表示不缩放
		}
		// 设置缩放比例
		options.inSampleSize = be;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 质量压缩
		int quality = 80;
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		// 循环判断如果压缩后图片是否大于100KB,大于继续压缩
		while (quality > 0 && baos.toByteArray().length / 1024 >= 200) {
			baos.reset();// 重置baos即清空baos
			quality -= 10;// 每次都减少10
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		}
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(tempFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fileOutputStream.write(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempFile.getAbsolutePath();
	}

	public static String saveBitmapFile(Bitmap bitmap){
		File file= getTempFile();//将要保存图片的路径
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}


	/**
	 * 获取相机临时图片Uri
	 * 
	 * @return
	 */
	public static File getTempFile() {
		String tempFileName = DateUtil.formatDate(new Date(),"yyyyMMddHHmmss");
		File tempFile =  new File(FilePathProvider
				.getAppImageFolderPath(), tempFileName+PHOTO_TEMP);
		if (!tempFile.exists()) {
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tempFile;
	}
}
