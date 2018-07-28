package pros.app.com.pros;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;

import pros.app.com.pros.account.model.SignInModel;
import pros.app.com.pros.base.PrefUtils;

public class ProsApplication extends MultiDexApplication {

    private static ProsApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static ProsApplication getInstance() {
        return application;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public boolean isUserLoggedIn() {
        return PrefUtils.getUser() != null;
    }
}
