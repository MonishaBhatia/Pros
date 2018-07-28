package pros.app.com.pros.profile.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.profile.presenter.SettingsPresenter;

public class SettingsActivity extends AppCompatActivity {

    private SettingsPresenter settingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        settingsPresenter = new SettingsPresenter();
    }

    @OnClick(R.id.ivBack)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.tvContact)
    public void onClickContactAdmin() {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse("mailto:hello@theprosapp.com"));
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tvChangePswd)
    public void onClickChangePswd() {
        ChangePasswordFragment.newInstance().show(this.getSupportFragmentManager(), ChangePasswordFragment.TAG);
    }

    @OnClick(R.id.tvLogout)
    public void onClickLogout() {
        //settingsPresenter.onLogout();
    }

    @OnClick(R.id.tvDeactivate)
    public void onClickDeactivate() {

        //settingsPresenter.onDeactivate();
    }


}
