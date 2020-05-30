package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    EditText cityText;
    String city;
    TextView weatherInfoTextView;
    DownloadTask downloadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityText = findViewById(R.id.editText);
        weatherInfoTextView = findViewById(R.id.weatherInfo);
    }

    public void getWeather(View view) {
        downloadTask = new DownloadTask();
        city = cityText.getText().toString();

        downloadTask.execute("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=9f84754efcfb4216590deb4dcaa651ef");

    }

    class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                String res = "";

                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1) {
                    char c = (char) data;
                    res += c;
                    data = reader.read();
                }

                return res;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jObject = arr.getJSONObject(i);

                    String weather = jObject.getString("description");

                    if(!weather.equals("")) {
                        weatherInfoTextView.setVisibility(View.VISIBLE);
                        weatherInfoTextView.setText(weather);
                    }

                }

            } catch (Exception e) {
                weatherInfoTextView.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Could not find the place", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

}
