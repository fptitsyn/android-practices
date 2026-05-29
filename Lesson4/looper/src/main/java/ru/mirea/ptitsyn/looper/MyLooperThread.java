package ru.mirea.ptitsyn.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooperThread extends Thread {
    private static final String TAG = "MyLooperThread";
    private Handler workerHandler;
    private Looper looper;

    @Override
    public void run() {
        Looper.prepare();
        looper = Looper.myLooper();
        workerHandler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                int age = data.getInt("age", 0);
                String job = data.getString("job", "");

                Log.d(TAG, "Получено: возраст=" + age + ", работа=" + job);

                // Задержка на количество секунд, равное возрасту
                try {
                    Thread.sleep(age * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Вывод результата в Log.d после задержки
                Log.d(TAG, "Результат: возраст " + age + " лет, работа: " + job +
                        ". Задержка составила " + age + " секунд(ы)");
            }
        };
        Looper.loop();
    }

    public Handler getHandler() {
        return workerHandler;
    }

    public void quit() {
        if (looper != null) {
            looper.quit();
        }
    }
}