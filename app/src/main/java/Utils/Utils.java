package Utils;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
public static final String BASEURL="http://api.openweathermap.org/data/2.5/weather?q=";
public static final String ICON="http://openweathermap.org/img/w/";
public static JSONObject getObject(String tagname,JSONObject jsonObject) throws JSONException{
JSONObject jobject= jsonObject.getJSONObject(tagname);
return jobject;
}
public static String getString(String tagname,JSONObject jsonObject) throws JSONException{
    return jsonObject.getString(tagname);
}
public static float getFloat(String tagname, JSONObject jsonObject) throws  JSONException{
    return   (float) jsonObject.getDouble(tagname);
}
    public static double getDouble(String tagname, JSONObject jsonObject) throws  JSONException{
        return   (float) jsonObject.getDouble(tagname);
    }
    public static int getInt(String tagname, JSONObject jsonObject) throws  JSONException{
        return    jsonObject.getInt(tagname);
    }

}
