package Data;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.Place;
import Model.Weather;
import Utils.Utils;

public class JsonWeatherObject {
    public static Weather getWeather(String data){
        Weather weather=new Weather();
        try {
            JSONObject jsonObject=new JSONObject(data);

            //Place place=new Place();
            //Sys Obj
            JSONObject sysobj= Utils.getObject("sys",jsonObject);
            weather.place.setCountry(Utils.getString("country",sysobj));
            weather.place.setSunset(Utils.getInt("sunset",sysobj));
            weather.place.setSunrise(Utils.getInt("sunrise",sysobj));
            weather.place.setCity(Utils.getString("name",jsonObject));
            weather.place.setLastUpdate(Utils.getInt("dt",jsonObject));
            //weather.place=place;
            //Weather Info
            JSONArray jsonArray= jsonObject.getJSONArray("weather");
            JSONObject jsonweather= jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(Utils.getInt("id",jsonweather));
            weather.currentCondition.setCondition(Utils.getString("main",jsonweather));
            weather.currentCondition.setDescription(Utils.getString("description",jsonweather));
            weather.currentCondition.setIcon(Utils.getString("icon",jsonweather));

            //Wind Object
            JSONObject windObject= Utils.getObject("wind",jsonObject);
            weather.wind.setDeg(Utils.getFloat("deg",windObject));
            weather.wind.setSpeed(Utils.getFloat("speed",windObject));
            //Clouds
            JSONObject cloudObj=Utils.getObject("clouds",jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all",cloudObj));
            //Main
            JSONObject mainobj= Utils.getObject("main",jsonObject);
            weather.currentCondition.setTemperature(Utils.getDouble("temp",mainobj));
            weather.currentCondition.setHumidity(Utils.getFloat("humidity",mainobj));
            weather.currentCondition.setPressure(Utils.getFloat("pressure",mainobj));
            weather.currentCondition.setMax_temp(Utils.getFloat("temp_max",mainobj));
            weather.currentCondition.setMin_temp(Utils.getFloat("temp_min",mainobj));
            return weather;

        } catch (JSONException e) {
            e.printStackTrace();
        }
return null;
    }

}
