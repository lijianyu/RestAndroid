package com.github.rain.net.callback;


import com.github.rain.net.RestAndroidHttp;
import com.github.rain.net.error.CallError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-23 13:14
 */
public abstract class FileCallBack implements Callback<File> {

    /**
     * 目标文件存储的文件夹路径
     */
    private String mDestFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String mDestFileName;


    public FileCallBack(String destFileDir, String destFileName) {
        this.mDestFileDir = destFileDir;
        this.mDestFileName = destFileName;
    }

    @Override
    public File parseNetworkResponse(Response response) throws Exception {
        return saveFile(response);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgress(float progress, float length) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(CallError error) {

    }

    @Override
    public void onResponse(File response) {

    }

    @Override
    public void onCancel() {

    }

    /**
     * 保存文件到本地
     *
     * @param response
     *
     * @return
     *
     * @throws IOException
     */
    private File saveFile(Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;

            File dir = new File(mDestFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, mDestFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                RestAndroidHttp.getInstance().delivery(new Runnable() {
                    @Override
                    public void run() {

                        onProgress(finalSum * 1.0f / total, total);
                    }
                });
            }
            fos.flush();

            return file;

        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }

        }
    }
}
