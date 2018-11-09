package com.huang.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sinothk.openfire.android.demo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * Created by Administrator on 2017/11/15.
 */

public class CommonUtils {
    public static String imageUrl = "http://112.74.83.188/";// 以后网络地址都在这里改
    public static String postUrl = imageUrl + "/ECMobile/controller/common/upload_img.php"; // 处理图片上传时,POST请求的页面
    public static String postUrl_audio = imageUrl + "/ECMobile/controller/consult/upload_voice.php";// 上传语音到PHP服务器

    /**
     * 切换碎片,淡入淡出效果
     */
    public static void switchFragment(Fragment from, Fragment to, FragmentManager manager, int layout) {
        if (from != to) {
            FragmentTransaction transaction = manager.beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(layout, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    /**
     * 设置图片
     *
     * @param sender_photo sender_photo
     * @param iv           iv
     */
    public static void setImage(String sender_photo, ImageView iv) {
        String imageUri = "";
        if (!TextUtils.isEmpty(sender_photo)) {
            if (sender_photo.contains("http://")) {// 说明用户是用新浪和微博登录的
                imageUri = sender_photo.trim();
            } else {
                imageUri = imageUrl + sender_photo.trim();
            }
        } else {
            imageUri = "drawable://" + R.drawable.no_head_image;
        }
//        ImageLoader.getInstance().displayImage(imageUri, iv,
//                ImageLoaderUtils_circle.MyDisplayImageOptions());
    }

    /**
     * 设置图片
     *
     * @param mContext
     * @param avatarUrl
     * @param imageView
     */
    public static void setImage(Context mContext, String avatarUrl, ImageView imageView) {
        Glide.with(mContext).load(avatarUrl).into(imageView);

//        String imageUri = "";
//        if (!TextUtils.isEmpty(sender_photo)) {
//            if (sender_photo.contains("http://")) {// 说明用户是用新浪和微博登录的
//                imageUri = sender_photo.trim();
//            } else {
//                imageUri = imageUrl + sender_photo.trim();
//            }
//        } else {
//            imageUri = "drawable://" + R.drawable.no_head_image;
//        }
//        ImageLoader.getInstance().displayImage(imageUri, iv,
//                ImageLoaderUtils_circle.MyDisplayImageOptions());
    }


    /**
     * 检查sd卡是否插上
     *
     * @return 检查sd卡是否插上
     */
    public static boolean onCheckSDScardExist() {
        // 没有SDCard
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * @param urlStr   文件全路径
     * @param path     下载保存的文件夹
     * @param fileName 下载保存的文件名
     * @return 成功与否
     */
    public static String fileDownLoadByUrl(String urlStr, String path, String fileName) {

        OutputStream output = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 取得inputStream，并将流中的信息写入SDCard
            String SDCard = Environment.getExternalStorageDirectory() + "";
            String pathName = SDCard + "/" + path + "/" + fileName;// 文件存储路径

            File file = new File(pathName);
            InputStream input = conn.getInputStream();
            if (file.exists()) {
                return "exits";
            } else {
                String dir = SDCard + "/" + path;
                if (!(new File(dir)).exists()) {
                    new File(dir).mkdirs();// 文件夹不存在则新建文件夹
                    file.createNewFile();// 新建文件
                }
                output = new FileOutputStream(file);
                // 读取大文件
                byte[] buffer = new byte[4 * 1024];
                int length;
                while ((length = (input.read(buffer))) > 0) {
                    output.write(buffer, 0, length);
                }

                output.flush();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
                return "success";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "fail";
    }

    /**
     * Android相册中获取图片和路径
     *
     * @param context     context
     * @param originalUri 图片的uri
     * @param cursor      cursor
     * @return
     */
    public static String getPicturePath(Context context, Uri originalUri, Cursor cursor) {

        // 获得用户选择的图片的索引值
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        // 将光标移至开头,这个很重要,不小心很容易引起越界
        cursor.moveToFirst();

        // 最后根据索引值获取图片路径
        String path = cursor.getString(column_index);

        return path;
    }

    /**
     * SD卡是否存在
     *
     * @return 有SD卡返回true，没有返回false
     */
    public static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * SD卡剩余空间
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * deal with Toast
     *
     * @param context
     * @param message
     * @param time
     */
    public static void dealWithToast(Context context, String message, int time) {
        if (time == 1) {// 1为长时间显示
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } else {// 0为短时间显示
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示软键盘 true
     * 隐藏软键盘 false
     *
     * @param softFlag
     */
    public static void doSomethingForKeyboard(Context context, Boolean softFlag, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (softFlag) {
            imm.showSoftInput(editText, 0);//显示键盘
        } else {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);//隐藏键盘
        }
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     */
    public static String replaceStr(String str) {

        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 获取系统当前时间
     */
    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * 修改文件夹的名字
     *
     * @param src  旧文件名
     * @param dest 新文件名
     * @return
     */
    public static boolean renameToNewFile(String src, String dest) {
        dest = dest.substring(dest.lastIndexOf("/"), dest.length());// 截取/后面的文件名
        String parentPath = Environment.getExternalStorageDirectory() + "/com_yongting_care_im_audios";
        File destDir = new File(parentPath + dest);

        File srcDir = new File(src);

        return srcDir.renameTo(destDir);
    }

    /**
     * 压缩图片尺寸
     *
     * @param pathName
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap compressBySize(String pathName, int targetWidth,
                                        int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        //设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }

    /**
     * 保存bitmap到SD卡
     *
     * @param bitName 保存的名字
     * @param mBitmap 图片对像
     *                return 生成压缩图片后的图片路径
     */
    public static String saveMyBitmap(String bitName, Bitmap mBitmap) {
        File f = new File("/sdcard/" + bitName + ".png");

        try {
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("在保存图片时出错：" + e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (Exception e) {
            return "create_bitmap_error";
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "/sdcard/" + bitName + ".png";
    }

    /**
     * 保存bitmap到SD卡
     *
     * @param bitmap
     * @param imagename
     */
    public static String saveBitmapToSDCard(Bitmap bitmap, String imagename) {
        String path = "/sdcard/" + "img-" + imagename + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
            }

            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Android 通过 uri 获取Bitmap对象
     *
     * @param uri
     * @param context
     * @return
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除SD卡文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        String sdState = Environment.getExternalStorageState();

        if (sdState.equals(Environment.MEDIA_MOUNTED)) {
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                }
                // 如果它是一个目录
                else if (file.isDirectory()) {
                    // 声明目录下所有的文件 files[];
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                        deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                    }
                }
                file.delete();
            }
        }
    }
}
