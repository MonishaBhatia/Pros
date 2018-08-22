package pros.app.com.pros.launch_screen;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.account.activity.SignUpActivity;
import pros.app.com.pros.account.activity.SignInActivity;

public class LaunchActivity extends AppCompatActivity {

    @BindView(R.id.videoView)
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.login);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    @OnClick(R.id.tvSignIn)
    public void onClickSignIn() {
        startActivity(new Intent(this, SignInActivity.class));
    }

    @OnClick(R.id.tvFbSignIn)
    public void onClickFbSignIn() {

    }

    @OnClick(R.id.tvSignUp)
    public void onClickSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
