package ru.mirea.ptitsyn.mireaproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ru.mirea.ptitsyn.mireaproject.databinding.FragmentCompassBinding;

public class CompassFragment extends Fragment implements SensorEventListener {
    private FragmentCompassBinding binding;
    private SensorManager sensorManager;
    private Sensor magnetometer;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean hasAccelerometer = false;
    private boolean hasMagnetometer = false;
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCompassBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        if (magnetometer != null) {
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            hasAccelerometer = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            hasMagnetometer = true;
        }
        if (hasAccelerometer && hasMagnetometer) {
            SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(rotationMatrix, orientation);
            float azimuth = (float) Math.toDegrees(orientation[0]); // азимут в градусах
            azimuth = (azimuth + 360) % 360;
            String direction;
            if (azimuth >= 337.5 || azimuth < 22.5) direction = "Север";
            else if (azimuth >= 22.5 && azimuth < 67.5) direction = "Северо-Восток";
            else if (azimuth >= 67.5 && azimuth < 112.5) direction = "Восток";
            else if (azimuth >= 112.5 && azimuth < 157.5) direction = "Юго-Восток";
            else if (azimuth >= 157.5 && azimuth < 202.5) direction = "Юг";
            else if (azimuth >= 202.5 && azimuth < 247.5) direction = "Юго-Запад";
            else if (azimuth >= 247.5 && azimuth < 292.5) direction = "Запад";
            else direction = "Северо-Запад";

            binding.tvAzimuth.setText(String.format("Азимут: %.0f°", azimuth));
            binding.tvDirection.setText("Направление: " + direction);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        if (magnetometer != null) sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }
}