package pros.app.com.pros.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.account.model.SignInModel;
import pros.app.com.pros.account.presenter.SignUpPresenter;
import pros.app.com.pros.account.views.SignInView;
import pros.app.com.pros.base.BaseActivity;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.home.activity.HomeActivity;

public class SignUpActivity extends BaseActivity implements SignInView {
    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;


    private SignUpPresenter signUpPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ButterKnife.bind(this);

        toolbarTitle.setText(getString(R.string.sign_up));

        signUpPresenter = new SignUpPresenter(this);
    }

    @OnClick(R.id.ivBack)
    public void onClickBack(){
        finish();
    }

    @OnClick(R.id.tvSignIn)
    public void onClickSignIn() {
        startActivity(new Intent(this, SignInActivity.class));
    }

    @OnClick(R.id.tvTnC)
    public void onClickTnc() {
    }

    @OnClick(R.id.tvSignUp)
    public void onClickSignUp() {
        signUpPresenter.validateData(edtName.getText().toString(), edtEmail.getText().toString(), edtPassword.getText().toString());
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
    public void onValidationError(int message) {
        Toast.makeText(getBaseContext(), getResources().getString(message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSucessforgotPswd() {

    }
}
