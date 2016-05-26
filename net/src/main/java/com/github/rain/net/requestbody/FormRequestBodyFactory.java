package com.github.rain.net.requestbody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-20 15:20
 */
public class FormRequestBodyFactory implements RequestBodyFactory {

    private Map<String, String> mParams = new LinkedHashMap<>();
    private List<FileInput> mFileInputs = new ArrayList<>();

    @Override
    public RequestBody buildRequestBody() {
        if (mFileInputs.isEmpty()) {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder);
            return builder.build();
        }

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder);

        for (int i = 0; i < mFileInputs.size(); i++) {
            FileInput fileInput = mFileInputs.get(i);
            RequestBody fileBody = RequestBody.create(MediaType.parse(getMimeType(fileInput.filename)), fileInput.file);
            builder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
        }
        return builder.build();


    }


    /**
     * add request String  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return 
     */
    public FormRequestBodyFactory addParam(String key, String value) {
        if (key != null && value != null) {
            this.mParams.put(key, value);
        }

        return this;
    }

    /**
     * add request Object  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return 
     */
    public FormRequestBodyFactory addParam(String key, Object value) {
        if (key != null && value != null) {
            this.mParams.put(key, value.toString());
        }

        return this;
    }

    /**
     * add request int  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return 
     */
    public FormRequestBodyFactory addParam(String key, int value) {
        if (key != null) {
            this.mParams.put(key, String.valueOf(value));
        }

        return this;
    }

    /**
     * add request long  params.
     *
     * @param key   param's key
     * @param value param's value
     *
     * @return 
     */
    public FormRequestBodyFactory addParam(String key, long value) {
        if (key != null) {
            this.mParams.put(key, String.valueOf(value));
        }

        return this;
    }



    public FormRequestBodyFactory files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.mFileInputs.add(new FileInput(key, filename, files.get(filename)));
        }
        return this;
    }

    public FormRequestBodyFactory addFile(String name, String filename, File file) {
        mFileInputs.add(new FileInput(name, filename, file));
        return this;
    }


    ///////////////////////////////////////////////////////////////////////////
    // private
    ///////////////////////////////////////////////////////////////////////////

    private void addParams(FormBody.Builder builder) {
        if (mParams != null) {
            for (String key : mParams.keySet()) {
                builder.add(key, mParams.get(key));
            }
        }
    }


    private void addParams(MultipartBody.Builder builder) {
        if (mParams != null && !mParams.isEmpty()) {
            for (String key : mParams.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, mParams.get(key)));
            }
        }
    }


    private String getMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }
}
