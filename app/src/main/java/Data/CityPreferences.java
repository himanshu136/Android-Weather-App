package Data;

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreferences {
    SharedPreferences preferences;
    public CityPreferences(Activity activity){
        preferences=activity.getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

    }
    public String getCity(){
    return preferences.getString("city","New Delhi,In");
    }
    public void setCity(String city){
        preferences.edit().putString("city",city).commit();
    }
}
