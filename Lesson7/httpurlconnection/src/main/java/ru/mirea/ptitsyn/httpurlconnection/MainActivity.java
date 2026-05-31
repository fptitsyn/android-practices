package ru.mirea.ptitsyn.httpurlconnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.mirea.ptitsyn.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnGetInfo.setOnClickListener(v -> fetchIpAndWeather());
    }

    private void fetchIpAndWeather() {
        executor.execute(() -> {
            try {
                URL ipUrl = new URL("https://ipwhois.app/json/");
                HttpURLConnection ipConn = (HttpURLConnection) ipUrl.openConnection();
                ipConn.setRequestMethod("GET");
                ipConn.setConnectTimeout(5000);
                ipConn.setReadTimeout(5000);

                int responseCode = ipConn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(ipConn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();
                ipConn.disconnect();

                JSONObject json = new JSONObject(sb.toString());

                if (!json.optBoolean("success", false)) {
                    throw new Exception("API error: " + json.optString("message", "unknown"));
                }

                String ip = json.getString("ip");
                String city = json.getString("city");
                String region = json.getString("region");
                double lat = json.getDouble("latitude");
                double lon = json.getDouble("longitude");

                String weatherUrlStr = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon + "&current_weather=true";
                URL weatherUrl = new URL(weatherUrlStr);
                HttpURLConnection weatherConn = (HttpURLConnection) weatherUrl.openConnection();
                weatherConn.setRequestMethod("GET");
                weatherConn.setConnectTimeout(5000);
                weatherConn.setReadTimeout(5000);
                BufferedReader weatherReader = new BufferedReader(new InputStreamReader(weatherConn.getInputStream()));
                StringBuilder weatherSb = new StringBuilder();
                while ((line = weatherReader.readLine()) != null) weatherSb.append(line);
                weatherReader.close();
                weatherConn.disconnect();

                JSONObject weatherJson = new JSONObject(weatherSb.toString());
                JSONObject current = weatherJson.getJSONObject("current_weather");
                double temperature = current.getDouble("temperature");
                int windspeed = current.getInt("windspeed");
                String weatherDesc = "Температура: " + temperature + "°C, Ветер: " + windspeed + " км/ч";

                String finalIp = ip;
                String finalCity = city;
                String finalRegion = region;
                String finalWeather = weatherDesc;
                mainHandler.post(() -> {
                    binding.tvIP.setText("IP: " + finalIp);
                    binding.tvCity.setText("Город: " + finalCity);
                    binding.tvRegion.setText("Регион: " + finalRegion);
                    binding.tvWeather.setText("Погода: " + finalWeather);
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> binding.tvWeather.setText("Ошибка: " + e.getMessage()));
            }
        });
    }
}