package pros.app.com.pros.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import pros.app.com.pros.ProsApplication;
import pros.app.com.pros.account.model.SignInModel;
import pros.app.com.pros.account.model.UserModel;

import static pros.app.com.pros.base.LogUtils.LOGD;

public final class PrefUtils {


    private static final String NAME = "pros.app.com.pros." + PrefUtils.class.getSimpleName();
    private static final String NAME_NEW = "pros.app.com.pros.";
    private static final String USER_PREF_NAME = "pros.user";
    private static SharedPreferences newNamePrefs;
    private static SharedPreferences userPrefs;

    public interface PrefKeys {
    }

    public interface UserKeys {
        String USER = "user";
        String USER_ID = USER + ".id";
        String EMAIL = USER + ".email";
        String FIRST_NAME = USER + ".first_name";
        String LAST_NAME = USER + ".last_name";
        String API_KEY = USER + ".api_key";
        String USER_TYPE = USER + ".user_type";
    }

    private static SharedPreferences getSharedPreferences() {
        return ProsApplication.getInstance().getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getNewSharedPreferences() {
        if (null == newNamePrefs)
            newNamePrefs = ProsApplication.getInstance().getSharedPreferences(NAME_NEW, Context.MODE_PRIVATE);
        return newNamePrefs;
    }

    private static SharedPreferences getUserPreferences() {
        if (null == userPrefs)
            userPrefs = ProsApplication.getInstance().getSharedPreferences(USER_PREF_NAME, Context.MODE_PRIVATE);
        return userPrefs;
    }

    public static boolean contains(String key) {
        return getNewSharedPreferences().contains(key);
    }

    public static void putString(String key, String value) {
        LOGD(PrefUtils.class.getSimpleName(), "putString() --> key: " + key + ", value: " + value);
        getNewSharedPreferences().edit().putString(key, value).apply();
    }

    public static int getInt(String key) {
        int value = getNewSharedPreferences().getInt(key, 0);
        return value;
    }

    public static void putInt(String key, int value) {
        LOGD(PrefUtils.class.getSimpleName(), "putInt() --> key: " + key + ", value: " + value);
        getNewSharedPreferences().edit().putInt(key, value).apply();
    }

    public static void putLong(String key, long value) {
        LOGD(PrefUtils.class.getSimpleName(), "putInt() --> key: " + key + ", value: " + value);
        getNewSharedPreferences().edit().putLong(key, value).apply();
    }

    public static long getLong(String key) {
        return getNewSharedPreferences().getLong(key, 0);
    }



    public static String getString(String key) {
        String value = getNewSharedPreferences().getString(key, "");  // Empty string as default value
        return value;
    }

    public static void putBoolean(String key, boolean value) {
        getNewSharedPreferences().edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return getNewSharedPreferences().getBoolean(key, false);  // false as default value
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getNewSharedPreferences().getBoolean(key, defValue);
    }


    public static void saveUser(@NonNull UserModel user) {
        SharedPreferences.Editor editor = getUserPreferences().edit();
        editor.putString(UserKeys.EMAIL, user.getEmail());
        editor.putString(UserKeys.FIRST_NAME, user.getFirstName());
        editor.putString(UserKeys.LAST_NAME, user.getLastName());
        editor.apply();

        LogUtils.LOGI(PrefUtils.class.getSimpleName(), "saveUser() -> " + user.toString());
    }


    public static UserModel getUser() {

        if (!getUserPreferences().contains(UserKeys.API_KEY))
            return null;

        return new UserModel(
                getUserPreferences().getString(UserKeys.EMAIL, ""),
                getUserPreferences().getString(UserKeys.FIRST_NAME, ""),
                getUserPreferences().getString(UserKeys.LAST_NAME, ""),
                getUserPreferences().getString(UserKeys.API_KEY, ""),
                getUserPreferences().getString(UserKeys.USER_TYPE, ""));
    }

    public static void deleteUser() {
        getUserPreferences().edit().clear().apply();
    }

    public static void clearAllSharedPref() {
        getUserPreferences().edit().clear().apply();
        getNewSharedPreferences().edit().clear().apply();
    }
}
