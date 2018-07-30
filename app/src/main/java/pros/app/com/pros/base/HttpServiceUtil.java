package pros.app.com.pros.base;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Switch;

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
import static pros.app.com.pros.base.ProsConstants.PUT_METHOD;

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
    private RequestBody body;

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

        switch (method) {
            case GET_METHOD:
                request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("content-type", "application/json")
                        .addHeader(getTokenHeader(), getTokenValue())
                        .addHeader("Accept", "application/json")
                        .build();
                break;
            case POST_METHOD:
                body = RequestBody.create(mediaType, jsonRequest);
                if(tag == ApiEndPoints.sign_in.getTag() || tag == ApiEndPoints.sign_up.getTag()){
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
                mediaType = MediaType.parse("image/jpg");
                RequestBody body = RequestBody.create(mediaType, "file:///storage/emulated/0/Pictures/1532864791381.jpg");
                request = new Request.Builder()
                        .url(url)
                        .put(body)
                        .addHeader("Content-Type", "image/jpg")
                        .build();
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


