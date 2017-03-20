package com.hundsun.codecompete.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wyj on 8/25/15. 统一日志信息，当发布版本时，令 private static int currentStage =
 * RELEASE;
 */
public class LogUtils {

    /**
     * log日志状态
     */

    private static final int INFO = 0;

    private static final int ERROR = 1;

    private static final int WARN=2;
    public static final String TAG = "HS";

    // private static LogUtils logUtilsInstance=new LogUtils();
    // private LogUtils(){}
    // public static LogUtils getInstance(){
    // return logUtilsInstance;
    // }

    /**
     * 开发阶段
     */
    private static final int DEVELOP = 0;
    /**
     * 内部测试阶段
     */
    private static final int DEBUG = 1;
    /**
     * 公开测试
     */
    private static final int BATE = 2;
    /**
     * 正式版
     */
    private static final int RELEASE = 3;

    /**
     * 当前开发 阶段标示
     */
    private static int currentStage = DEVELOP;

    private static String path;
    private static File file;
    private static FileOutputStream outputStream;
    private static String pattern = "yyyy-MM-dd HH:mm:ss";

    static {
        if (currentStage == BATE
                && Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
            File externalStorageDirectory = Environment
                    .getExternalStorageDirectory();
            path = externalStorageDirectory.getAbsolutePath() + "/tzyj/";
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            file = new File(new File(path), "log.txt");
            Log.i("SDCAEDTAG", path);
            try {
                outputStream = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {

            }
        } else {

        }
    }

    /**
     * info引用
     * 
     * @param clazz
     * @param msg
     */
    public static void i(Class clazz, String msg) {
        writeMessage(clazz.getName(), msg, INFO);
    }

    public static void i(String tag, String msg) {
        writeMessage(tag, msg, INFO);
    }

    public static void i(String msg) {
        writeMessage(Log.class.getName(), msg, INFO);
    }

    /**
     * 错误日志引用
     * 
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        writeMessage(tag, msg, ERROR);
    }

    public static void e(String msg) {
        writeMessage(Log.class.getName(), msg, ERROR);
    }

    /**
     * 错误日志引用
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        writeMessage(tag, msg, WARN);
    }

    public static void w(String msg) {
        writeMessage(Log.class.getName(), msg, WARN);
    }

    private static void writeMessage(String tag, String msg, int logStatus) {
        switch (currentStage) {
        case DEVELOP:
            // 控制台输出
            Log.i(tag, msg);
            break;
        case DEBUG:
            // 在应用下面创建目录存放日志
            // 控制台输出
            switch (logStatus) {
            case INFO:
                Log.i(tag, msg);
                break;
            case ERROR:
                Log.e(tag, msg);
                break;
            case WARN:
                Log.w(tag,msg);
            default:
                break;
            }
            break;
            //Log.i(tag, msg);
        case BATE:
            // 写日志到sdcard
            Date date = new Date();
            String time = new SimpleDateFormat(pattern).format(date);
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                if (outputStream != null) {
                    try {
                        outputStream.write(time.getBytes());
                        String className = "";
                        if (tag != null) {
                            className = Log.class.getSimpleName();
                        }
                        outputStream.write(("    " + className + "\r\n")
                                .getBytes());
                        outputStream.write(msg.getBytes());
                        outputStream.write("\r\n".getBytes());
                        outputStream.flush();
                    } catch (IOException e) {

                    }
                } else {
                    Log.i("SDCAEDTAG", "file is null");

                }
            }
            break;
        case RELEASE:
            // 一般不做日志记录
            break;
        default:
            break;
        }
    }

}
