package com.submeter.android.tools;

import android.text.TextUtils;

import com.submeter.android.SubmeterApp;
import com.submeter.android.constants.SystemConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;

public class FileUtils {

    public static void downloadFile(String fileUrl, String fileName, DownloadListener downloadListener) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = null;
        long fileSize = 0;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(28000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                file = new File(fileName);
                if(!file.exists()) {
                    File dirFile = new File(file.getParent());
                    if(!dirFile.exists()) {
                        dirFile.mkdirs();
                    }

                    file.createNewFile();
                }
                outputStream = new FileOutputStream(file);
                fileSize = connection.getContentLength();
                int length = 0;
                byte[] buffer = new byte[1024];
                int downloadSize = 0;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                    if(null != downloadListener) {
                        downloadSize = downloadSize + length;
                        downloadListener.updateProgress((int) ((downloadSize / (float) fileSize) * 100));
                    }
                }
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            if(null != downloadListener) {
                downloadListener.updateProgress(-1);
            }
            e.printStackTrace();

            if (null != file) {
                file.deleteOnExit();
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void clearAdCache() {
        File adCacheFile = SubmeterApp.getInstance().getExternalFilesDir(null);
        String cacheDir = adCacheFile.getAbsolutePath() + SystemConstant.AD_CACHE_DIR;
        File cacheFold = new File(cacheDir);
        if(cacheFold.exists()) {
            File[] files = cacheFold.listFiles();//声明目录下所有的文件 files[];
            for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件
                files[i].delete();
            }
        }
    }

    /**
     * 复制文件
     *
     * @param srcFile : 源文件全路径
     * @param destDir : 目标文件全路径
     * @return
     */
    public static long copyFile(File srcFile, File destDir, String newFileName) {
        long copySizes = 0;
        if (!srcFile.exists()) {
            copySizes = -1;
        } else if (!destDir.exists()) {
            copySizes = -1;
        } else if (TextUtils.isEmpty(newFileName)) {
            copySizes = -1;
        } else {
            FileChannel fcin = null;
            FileChannel fcout = null;
            try {
                fcin = new FileInputStream(srcFile).getChannel();
                fcout = new FileOutputStream(new File(destDir, newFileName)).getChannel();
                long size = fcin.size();
                fcin.transferTo(0, fcin.size(), fcout);
                copySizes = size;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fcin != null) {
                        fcin.close();
                    }
                    if (fcout != null) {
                        fcout.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return copySizes;
    }

    public interface DownloadListener {
        public void updateProgress(int progress);
    }
}