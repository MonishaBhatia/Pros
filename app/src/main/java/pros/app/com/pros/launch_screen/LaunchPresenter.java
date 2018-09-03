package pros.app.com.pros.launch_screen;

import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import pros.app.com.pros.R;
import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.BaseView;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.ProsConstants;

public class LaunchPresenter implements HttpServiceView {

    private BaseView baseView;

    public LaunchPresenter(BaseView baseView) {
        this.baseView = baseView;
    }

    public void fbCallback(CallbackManager callbackManager) {
        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                navigateWithfb(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                baseView.onFailure(R.string.internal_error);
            }
        });
    }

    private void navigateWithfb(final LoginResult loginResult) {

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        callSignUp(ApiEndPoints.fb_sign_in.getApi() + "code=" + loginResult.getAccessToken());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void callSignUp(String url) {

        new HttpServiceUtil(
                this,
                url,
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.fb_sign_in.getTag()
        ).execute();
    }

    @Override
    public void response(String response, int tag) {

    }

    @Override
    public void onError(int tag) {

    }
}
