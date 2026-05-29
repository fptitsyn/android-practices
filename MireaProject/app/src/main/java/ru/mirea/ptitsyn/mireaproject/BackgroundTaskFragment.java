package ru.mirea.ptitsyn.mireaproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class BackgroundTaskFragment extends Fragment {
    private TextView statusTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_background_task, container, false);
        statusTextView = view.findViewById(R.id.statusTextView);
        Button startButton = view.findViewById(R.id.startWorkerButton);

        startButton.setOnClickListener(v -> {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                    .setConstraints(constraints)
                    .build();

            WorkManager.getInstance(requireContext()).enqueue(workRequest);
            WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workRequest.getId())
                    .observe(getViewLifecycleOwner(), workInfo -> {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            statusTextView.setText("Background task completed");
                        } else if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING) {
                            statusTextView.setText("Running...");
                        }
                    });
        });
        return view;
    }

    public static class MyWorker extends Worker {
        public MyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
            super(context, params);
        }
        @NonNull
        @Override
        public Result doWork() {
            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { }
            return Result.success();
        }
    }
}