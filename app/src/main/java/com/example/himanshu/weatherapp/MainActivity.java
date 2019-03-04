package com.example.himanshu.weatherapp;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import Data.CityPreferences;
import Data.JsonWeatherObject;
import Data.WeatherHttpClient;
import Model.Weather;
import Utils.Utils;

public class MainActivity extends AppCompatActivity {
    private TextView cityName;
    private TextView temp;
    private ImageView iconData;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private  TextView updated;
    private TextView max;
    private TextView min;
    Weather weather=new Weather();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=(TextView) findViewById(R.id.textCity);
        temp=(TextView) findViewById(R.id.tempText);
        iconData=(ImageView)findViewById(R.id.iconImage);
        description=(TextView)findViewById(R.id.cloudText);
        humidity=(TextView)findViewById(R.id.humidityText);
        pressure=(TextView)findViewById(R.id.pressureText);
        wind=(TextView)findViewById(R.id.windText);
        sunrise=(TextView)findViewById(R.id.sunriseText);
        sunset=(TextView)findViewById(R.id.sunsetText);
        updated=(TextView)findViewById(R.id.updateText);
        max=(TextView)findViewById(R.id.maxText);
        min=(TextView)findViewById(R.id.minText);
        CityPreferences cityPref = new CityPreferences(MainActivity.this);

        renderWeather(cityPref.getCity());
    }
    public void renderWeather(String city){
        WratherTask wratherTask=new WratherTask();
        String API_PARAMS = "&units=metric&APPID=864024545418903711cbf73d87520868";
        wratherTask.execute(city+API_PARAMS);


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        URL url;
        HttpURLConnection urlConnection = null;
        InputStream is;

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImage(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iconData.setImageBitmap(bitmap);
        }

        private Bitmap downloadImage(String iconCode) {
            try {
                url = new URL(Utils.ICON + iconCode + ".png");
                Log.i("iconcode",iconCode);
//                url = new URL(Utils.ICON_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int statusCode = urlConnection.getResponseCode();
                Log.i("code", String.valueOf(statusCode));

                if(statusCode != HttpsURLConnection.HTTP_OK){
                    Log.e("DownloadImage", "Error" + statusCode + urlConnection.getResponseMessage());
                    return null;
                }

                is = new BufferedInputStream(urlConnection.getInputStream());

                // Decode contents from the InputStream
                return BitmapFactory.decodeStream(is);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        } // end downloadImage

    }
    private class WratherTask extends AsyncTask<String ,Void, Weather>{

        @Override
        protected Weather doInBackground(String... strings) {
            String data=((new WeatherHttpClient()).getWeatherData(strings[0]));
            try {
                weather = JsonWeatherObject.getWeather(data);
                if (weather != null) {
                    weather.iconData = weather.currentCondition.getIcon();
                    new DownloadImageTask().execute(weather.iconData);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return weather;
        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            DateFormat df= DateFormat.getTimeInstance();
            long dvsunrise = Long.valueOf(weather.place.getSunrise())*1000;
            Date dfsunrise = new java.util.Date(dvsunrise);
            String sunriseValue = new SimpleDateFormat("hh:mm:a").format(dfsunrise);
            long dvsunset = Long.valueOf(weather.place.getSunset())*1000;
            Date dfsunset = new java.util.Date(dvsunset);
            String sunsetValue = new SimpleDateFormat("hh:mm:a").format(dfsunset);
            long dvupdate = Long.valueOf(weather.place.getLastUpdate())*1000;
            Date dfupdate = new java.util.Date(dvupdate);
            String lastupdate = new SimpleDateFormat("hh:mm:a").format(dfupdate);
            DecimalFormat decimalFormat=new DecimalFormat("#.#");
            String tempformat=decimalFormat.format(weather.currentCondition.getTemperature());
            String maxtemp= decimalFormat.format(weather.currentCondition.getMax_temp());
            String mintemp=decimalFormat.format(weather.currentCondition.getMin_temp());
            cityName.setText(weather.place.getCity()+", "+weather.place.getCountry());
            temp.setText(""+tempformat+"°C");
            description.setText("Condition:"+weather.currentCondition.getCondition()+"("+weather.currentCondition.getDescription()+")");
            humidity.setText("Humidity:"+weather.currentCondition.getHumidity()+"%");
            pressure.setText("Pressure:"+weather.currentCondition.getPressure()+"hPa");
            wind.setText("Wind:"+weather.wind.getSpeed()+"mps");
            sunrise.setText("Sunrise:"+sunriseValue);
            sunset.setText("Sunset:"+sunsetValue);
            max.setText("Max:"+maxtemp+"°C");
            min.setText("Min:"+mintemp+"°C");
            updated.setText("Last Updated"+lastupdate);


        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder  builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change City");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("New Delhi,India");
        builder.setView(cityInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CityPreferences cityPref = new CityPreferences(MainActivity.this);
                cityPref.setCity(cityInput.getText().toString().trim());

                String newCity = cityPref.getCity();

                renderWeather(newCity);
            }
        });
        builder.show();


        return super.onOptionsItemSelected(item);
    }
}
