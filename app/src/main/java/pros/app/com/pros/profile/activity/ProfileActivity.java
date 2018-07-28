package pros.app.com.pros.profile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.ivSettings)
    public void onClickSettings() {
        startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
    }

    @OnClick(R.id.ivGoBack)
    public void onClickBack() {
        finish();
    }
}
