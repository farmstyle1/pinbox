package pinbox.com.pin.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 015240 on 1/25/2016.
 */
public class LocationHelper {
    Context context;
    SharedPreferences sharedPerfs;
    SharedPreferences.Editor editor;

    // Prefs Keys
    static String perfsName = "prefs_location";


    public LocationHelper(Context context) {
        this.context = context;
        this.sharedPerfs = this.context.getSharedPreferences(perfsName, Context.MODE_PRIVATE);
        this.editor = sharedPerfs.edit();
    }

    public void setLocation(String city) {
        editor.putString("KEY_LOCATION", city);
        editor.commit();
    }

    public void deleteSession() {
        editor.clear();
        editor.commit();
    }

    public String getLocation() {
        return sharedPerfs.getString("KEY_LOCATION", null);
    }
}
