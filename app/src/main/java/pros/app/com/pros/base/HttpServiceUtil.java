package pros.app.com.pros.base;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static pros.app.com.pros.base.ProsConstants.DELETE_METHOD;
import static pros.app.com.pros.base.ProsConstants.GET_METHOD;
import static pros.app.com.pros.base.ProsConstants.PATCH_METHOD;
import static pros.app.com.pros.base.ProsConstants.POST_METHOD;

public class HttpServiceUtil extends AsyncTask<String, String, String> {


    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    /*
     * Listener to catch and parse response
     */
    private HttpServiceView mListener;
    private String url;
    private String method;
    private String jsonRequest;
    private final int tag;

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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... strings) {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        Request request = null;

        if (GET_METHOD.equals(method)) {
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("content-type", "application/json")
                    .addHeader(getTokenHeader(), getTokenValue())
                    .addHeader("Accept", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
        } else if (POST_METHOD.equals(method)) {
            RequestBody body = RequestBody.create(mediaType, jsonRequest);
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader(getTokenHeader(), getTokenValue())
                    .addHeader("Accept", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
        } else if (DELETE_METHOD.equals(method)) {
            request = new Request.Builder()
                    .url(url)
                    .delete(null)
                    .addHeader("content-type", "application/json")
                    .addHeader(getTokenHeader(), getTokenValue())
                    .addHeader("Accept", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
        } else if (PATCH_METHOD.equals(method)) {
            RequestBody body = RequestBody.create(mediaType, jsonRequest);
            request = new Request.Builder()
                    .url(url)
                    .patch(body)
                    .addHeader("content-type", "application/json")
                    .addHeader(getTokenHeader(), getTokenValue())
                    .addHeader("Accept", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
        }

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response.toString());

            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (TextUtils.isEmpty(response)) {
            mListener.onError(tag);
        } else {
            mListener.response(response, tag);
        }
    }
}


