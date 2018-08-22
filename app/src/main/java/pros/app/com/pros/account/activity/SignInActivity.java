package pros.app.com.pros.account.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.account.model.SignInModel;
import pros.app.com.pros.account.presenter.SignInPresenter;
import pros.app.com.pros.account.views.SignInView;
import pros.app.com.pros.base.BaseActivity;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.home.activity.HomeActivity;

public class SignInActivity extends BaseActivity implements SignInView {

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @BindView(R.id.videoView)
    VideoView videoView;

    private SignInPresenter signInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        toolbarTitle.setText(getString(R.string.sign_in));

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login);
        videoView.setVideoURI(uri);
        videoView.start();

        signInPresenter = new SignInPresenter(this);
    }

    @OnClick(R.id.ivBack)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.tvSignIn)
    public void onClickSignIn() {
        signInPresenter.validateData(edtEmail.getText().toString(), edtPassword.getText().toString());
    }

    @OnClick(R.id.tvForgotPassword)
    public void onClickForgotPassword() {
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            openDialog(getString(R.string.forgot_paswd_error_title), getString(R.string.forgot_paswd_error_text), "OK");
        } else {
            signInPresenter.forgotPassword(edtEmail.getText().toString());
        }
    }

    @OnClick(R.id.tvSignUp)
    public void onClickSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override
    public void onSucess(SignInModel signInModel) {
        PrefUtils.saveUser(signInModel.getFan());
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onFailure(String message) {
        openDialog("", message, "Close");
    }

    @Override
    public void onValidationError(int error) {
        Toast.makeText(getBaseContext(), getResources().getString(error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSucessforgotPswd() {
        openDialog(getString(R.string.password_reset), getString(R.string.password_reset_text), "OK");
    }
}
