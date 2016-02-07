package pinbox.com.pin.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 015240 on 2/4/2016.
 */
public class Helper {
    Context context;
    SharedPreferences sharedPerfs;
    SharedPreferences.Editor editor;
    // Prefs Keys
    static String perfsName = "prefs_helper";

    public Helper(Context context) {
        this.context = context;
        this.sharedPerfs = this.context.getSharedPreferences(perfsName, Context.MODE_PRIVATE);
        this.editor = sharedPerfs.edit();
    }


    public void setLocation(String city) {
        editor.putString("KEY_LOCATION", city);
        editor.commit();
    }


    public String getLocation() {
        return sharedPerfs.getString("KEY_LOCATION", null);
    }

    public void setUsername(String username) {
        editor.putBoolean("isLogin", true);
        editor.putString("KEY_USER", username);
        editor.commit();
    }
    public void setId(String id) {
        editor.putString("KEY_ID", id);
        editor.commit();
    }

    public void logout() {
        editor.clear();
        editor.putBoolean("isLogin", false);
        editor.commit();
    }

    public boolean isLogin() {
        return sharedPerfs.getBoolean("isLogin", false);
    }


    public String getUsername() {
        return sharedPerfs.getString("KEY_USER", null);
    }
    public String getId() {
        return sharedPerfs.getString("KEY_ID", null);
    }
}
