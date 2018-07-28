package pros.app.com.pros.account.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.ProsApplication;
import pros.app.com.pros.R;
import pros.app.com.pros.account.model.SignInModel;
import pros.app.com.pros.account.presenter.SignInPresenter;
import pros.app.com.pros.account.views.SignInView;
import pros.app.com.pros.base.CustomDialogFragment;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.home.activity.HomeActivity;

public class SignInActivity extends AppCompatActivity implements SignInView {

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    private SignInPresenter signInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);
        signInPresenter = new SignInPresenter(this);

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
        startActivity(new Intent(this, HomeActivity.class));
        this.finish();
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

    private void openDialog(String title, String message, String action) {
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        bundle.putString("Content", message);
        bundle.putString("Action", action);
        CustomDialogFragment.newInstance(bundle).show(this.getSupportFragmentManager(), CustomDialogFragment.TAG);
    }
}
