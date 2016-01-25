package pinbox.com.pin.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 015240 on 1/25/2016.
 */
public class UserHelper {
    Context context;
    SharedPreferences sharedPerfs;
    SharedPreferences.Editor editor;

    // Prefs Keys
    static String perfsName = "prefs_user";


    public UserHelper(Context context) {
        this.context = context;
        this.sharedPerfs = this.context.getSharedPreferences(perfsName, Context.MODE_PRIVATE);
        this.editor = sharedPerfs.edit();
    }

    public void createSession(String username) {
        editor.putBoolean("isLogin", true);
        editor.putString("KEY_USER", username);
        editor.commit();
    }

    public void deleteSession() {
        editor.clear();
        editor.commit();
    }

    public boolean isLogin() {
        return sharedPerfs.getBoolean("isLogin", false);
    }


    public String getUserID() {
        return sharedPerfs.getString("KEY_USER", null);
    }
}
