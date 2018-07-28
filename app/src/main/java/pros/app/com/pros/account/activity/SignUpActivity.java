package pros.app.com.pros.account.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.account.model.SignInModel;
import pros.app.com.pros.account.presenter.SignUpPresenter;
import pros.app.com.pros.account.views.SignInView;
import pros.app.com.pros.base.CustomDialogFragment;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.home.activity.HomeActivity;

public class SignUpActivity extends AppCompatActivity implements SignInView {
    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    private SignUpPresenter signUpPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ButterKnife.bind(this);

        signUpPresenter = new SignUpPresenter(this);
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
        startActivity(new Intent(this, HomeActivity.class));
        this.finish();
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

    private void openDialog(String title, String message, String action) {
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        bundle.putString("Content", message);
        bundle.putString("Action", action);
        CustomDialogFragment.newInstance(bundle).show(this.getSupportFragmentManager(), CustomDialogFragment.TAG);
    }
}
