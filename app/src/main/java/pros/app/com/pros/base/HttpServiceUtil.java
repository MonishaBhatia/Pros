package pros.app.com.pros.base;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static pros.app.com.pros.base.ProsConstants.DELETE_METHOD;
import static pros.app.com.pros.base.ProsConstants.GET_METHOD;
import static pros.app.com.pros.base.ProsConstants.PATCH_METHOD;
import static pros.app.com.pros.base.ProsConstants.POST_METHOD;
import static pros.app.com.pros.base.ProsConstants.PUT_METHOD;

public class HttpServiceUtil extends AsyncTask<String, String, String> {


    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final int LOW_CONNECT_TIMEOUT = 30 * 1000;
    private static final long LOW_READ_TIMEOUT = 30 * 1000;
    private static final int LOW_WRITE_TIME_OUT = 30 * 1000;
    /*
     * Listener to catch and parse response
     */
    private HttpServiceView mListener;
    private String url;
    private String method;
    private String jsonRequest;
    private int tag;
    private RequestBody body;
    private byte[] bytes;

    private String getTokenHeader() {
        if (PrefUtils.getUser() != null) {
            if ("Fan".equals(Objects.requireNonNull(PrefUtils.getUser()).getUserType()))
                return "X-Fan-Auth-Token";
            else
                return "X-Athlete-Auth-Token";
        }
        return "";
    }

    private String getTokenValue() {
        if (PrefUtils.getUser() != null) {
            return Objects.requireNonNull(PrefUtils.getUser()).getApiKey();
        }
        return "";
    }

    public HttpServiceUtil(HttpServiceView mListener, String url, String method, String jsonRequest, int tag) {
        this.mListener = mListener;
        this.url = url;
        this.method = method;
        this.jsonRequest = jsonRequest;
        this.tag = tag;
    }

    public HttpServiceUtil(HttpServiceView mListener, String url) {
        this.mListener = mListener;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... strings) {

        OkHttpClient client;
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(LOW_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(LOW_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(LOW_WRITE_TIME_OUT, TimeUnit.MILLISECONDS);

        client = clientBuilder.build();
        Request request = null;
        MediaType mediaType = MediaType.parse("application/json");

        switch (method) {
            case GET_METHOD:
                if(url.contains("video")){
                    request = new Request.Builder()
                            .url(url)
                            .get()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Accept", "application/json")
                            .addHeader(getTokenHeader(), getTokenValue())
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(url)
                            .get()
                            .addHeader("content-type", "application/json")
                            .addHeader(getTokenHeader(), getTokenValue())
                            .addHeader("Accept", "application/json")
                            .build();
                }
                break;
            case POST_METHOD:
                if(jsonRequest == null) {
                    body = RequestBody.create(null, new byte[]{});
                } else {
                    body = RequestBody.create(mediaType, jsonRequest);
                }

                    if (tag == ApiEndPoints.sign_in.getTag() || tag == ApiEndPoints.sign_up.getTag()) {
                        request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .addHeader("content-type", "application/json")
                                .addHeader("Accept", "application/json")
                                .build();
                    } else {
                        request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .addHeader("content-type", "application/json")
                                .addHeader(getTokenHeader(), getTokenValue())
                                .addHeader("Accept", "application/json")
                                .build();
                    }
                break;
            case DELETE_METHOD:
                request = new Request.Builder()
                        .url(url)
                        .delete(null)
                        .addHeader("content-type", "application/json")
                        .addHeader(getTokenHeader(), getTokenValue())
                        .addHeader("Accept", "application/json")
                        .build();
                break;
            case PATCH_METHOD:
                body = RequestBody.create(mediaType, jsonRequest);
                request = new Request.Builder()
                        .url(url)
                        .patch(body)
                        .addHeader("content-type", "application/json")
                        .addHeader(getTokenHeader(), getTokenValue())
                        .addHeader("Accept", "application/json")
                        .build();
                break;

            case PUT_METHOD:
                if(jsonRequest.contains("video")) {

                    try {
                        FileInputStream fis = new FileInputStream(jsonRequest);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] b = new byte[1024];

                        for (int readNum; (readNum = fis.read(b)) != -1; ) {
                            bos.write(b, 0, readNum);
                        }

                        bytes = bos.toByteArray();

                    }catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }

                    mediaType = MediaType.parse("application/octet-stream");
                    body = RequestBody.create(mediaType, bytes);
                    request = new Request.Builder()
                            .url(url)
                            .put(body)
                            .addHeader("Content-Type", "application/octet-stream")
                            .addHeader("Content-Range", "bytes 0 - 10000")
                            .addHeader(getTokenHeader(), getTokenValue())
                            .addHeader("Accept", "application/json")
                            .build();
                } else {
                    mediaType = MediaType.parse("image/jpg");
                    body = RequestBody.create(mediaType, "file:///storage/emulated/0/Pictures/1532864791381.jpg");
                    request = new Request.Builder()
                            .url(url)
                            .put(body)
                            .addHeader("Content-Type", "image/jpg")
                            .build();
                }
        }

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                return null;

            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (null == response) {
            mListener.onError(tag);
        } else {
            mListener.response(response, tag);
        }
    }
}


