package pros.app.com.pros.home.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pros.app.com.pros.ProsApplication;
import pros.app.com.pros.R;
import pros.app.com.pros.launch_screen.LaunchActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

       if(ProsApplication.getInstance().isUserLoggedIn()){
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, LaunchActivity.class));
        }
        finish();
    }
}
