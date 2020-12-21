package com.duobang.common.util;


import android.os.Environment;

import com.blankj.utilcode.util.LogUtils;
import com.duobang.common.data.constant.IConstant;

import java.io.File;

/**
  * @Des:      附件格式工具类
  * @Author:   JayGengi
  * @Date:     2020/10/14 14:39
 */
public class FileFormUtils {
    public static int getFileType(String file) {
        if (isWord(file)) {
            return IConstant.FILE.WORD;
        } else if (isPDF(file)) {
            return IConstant.FILE.PDF;
        } else if (isExcel(file)) {
            return IConstant.FILE.EXCEL;
        } else if (isTxt(file)) {
            return IConstant.FILE.TXT;
        } else {
            return IConstant.FILE.OTHER;
        }
    }

    public static boolean isWord(String file) {
        if (null == file || "".equals(file)) {
            return false;
        }
        String filename = file;
        if (filename != null) {
            String[] split = filename.split("\\.");
            String suffix = split[split.length - 1];
            return suffix != null && suffix.equalsIgnoreCase("doc") || suffix.equalsIgnoreCase("docx");
        }
        return false;
    }

    public static boolean isPDF(String file) {
        if (null == file || "".equals(file)) {
            return false;
        }
        String filename = file;
        if (filename != null) {
            String[] split = filename.split("\\.");
            String suffix = split[split.length - 1];
            return suffix != null && suffix.equalsIgnoreCase("pdf");
        }
        return false;
    }

    public static boolean isExcel(String file) {
        if (null == file || "".equals(file)) {
            return false;
        }
        String filename = file;
        if (filename != null) {
            String[] split = filename.split("\\.");
            String suffix = split[split.length - 1];
            return suffix != null && suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx");
        }
        return false;
    }

    public static boolean isTxt(String file) {
        if (null == file || "".equals(file)) {
            return false;
        }
        String filename = file;
        if (filename != null) {
            String[] split = filename.split("\\.");
            String suffix = split[split.length - 1];
            return suffix != null && suffix.equalsIgnoreCase("txt");
        }
        return false;
    }

    /**
     * 判断是否为图片
     *
     * @param duobangFile
     * @return
     */
    public boolean isPhoto(String duobangFile) {
        if (null == duobangFile || "".equals(duobangFile)) {
            return false;
        }
        String[] split = duobangFile.split("\\.");
        String suffix = split[split.length - 1];
        return suffix.equalsIgnoreCase("jpeg") || suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("gif") || suffix.equalsIgnoreCase("webp");
    }

    public static void delFileByPath(String filePath) {
        try {
            File dir = new File(filePath);
            if (dir == null || !dir.exists() || !dir.isDirectory()) {
                return;
            }
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    //自定义
                    /**
                     if (Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date())) - Integer.parseInt(file.getName().split("-")[2].split("\\.")[0].substring(0, 8)) > 30) {
                     Log.e(TAG, "deleteFolderFile:30天之前的删除 ");
                     }
                     Log.e(TAG, "deleteFile: " + file.getName().split("-")[2].split("\\.")[0].substring(0, 8));
                     */
                    //具体操作
                    file.delete(); // 删除所有文件
                } else if (file.isDirectory()) {
                    delFileByPath(file.getPath()); // 递规的方式删除文件夹
                }
            }
            LogUtils.e("fileCache", "deleteFolder: " + dir.getName());
//            dir.delete();// 删除目录本身
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final String pathDir = Environment.getExternalStorageDirectory().getPath();
    /**
     * @return
     */
    public static String getPathDir() {
        File file = new File(pathDir + "/pms");
        if (!file.exists()) {
            if (!file.mkdir()) {
                return pathDir;
            }
        }
        return file.getPath();
    }
}
