package lib.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileUtil {

    public static String CacheDirectory = "/";
    public static String Audio = "audio";

    public static InputStream getInputStreamFromFile(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static String getSdCardPath() {
        String sdCardPath = null;
        if (isSDCardExist()) {
            sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return sdCardPath;
    }

    public static File getFile(String path, String fileName) {
        File file = null;
        File dirFile = null;
        dirFile = new File(path);
        if (dirFile.isDirectory() && !dirFile.exists()) {
            return file;
        }
        file = new File(path, fileName);
        return file;
    }

    /**
     * 获取或创建Cache目录
     *
     * @param bucket 临时文件目录，bucket = "cache/" ，则放在"sdcard/linked-joyrun/cache"; 如果bucket=""或null,则放在"sdcard/linked-joyrun/"
     */
    public static String getMyCacheDir(String bucket) {
        String dir;

        // 保证目录名称正确
        if (bucket != null) {
            if (!bucket.equals("")) {
                if (!bucket.endsWith("/")) {
                    bucket = bucket + "/";
                }
            }
        }

        if (isSDCardExist()) {
            dir = Environment.getExternalStorageDirectory().toString() + CacheDirectory + bucket;
        } else {
            dir = Environment.getDownloadCacheDirectory().toString() + CacheDirectory + bucket;
        }

        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
        return dir;
    }

    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getCacheSDPath() {
        String dir;
        if (isSDCardExist()) {
            dir = Environment.getExternalStorageDirectory().toString() + CacheDirectory;
        } else {
            dir = Environment.getDownloadCacheDirectory().toString() + CacheDirectory;
        }

        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
        return dir;
    }
}
