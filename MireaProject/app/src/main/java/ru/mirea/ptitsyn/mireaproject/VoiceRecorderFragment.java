package ru.mirea.ptitsyn.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.IOException;
import ru.mirea.ptitsyn.mireaproject.databinding.FragmentVoiceRecorderBinding;

public class VoiceRecorderFragment extends Fragment {
    private FragmentVoiceRecorderBinding binding;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String recordFilePath;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private static final int REQUEST_PERMISSION_CODE = 201;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVoiceRecorderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordFilePath = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "voicenote.3gp").getAbsolutePath();

        checkPermissions();

        binding.recordButton.setOnClickListener(v -> {
            if (!isRecording) {
                startRecording();
                binding.recordButton.setText("Остановить запись");
                binding.playButton.setEnabled(false);
                binding.statusTextView.setText("Идёт запись...");
            } else {
                stopRecording();
                binding.recordButton.setText("Начать запись");
                binding.playButton.setEnabled(true);
                binding.statusTextView.setText("Запись завершена");
            }
            isRecording = !isRecording;
        });

        binding.playButton.setOnClickListener(v -> {
            if (!isPlaying) {
                startPlaying();
                binding.playButton.setText("Остановить воспроизведение");
                binding.recordButton.setEnabled(false);
                binding.statusTextView.setText("Воспроизведение...");
            } else {
                stopPlaying();
                binding.playButton.setText("Воспроизвести");
                binding.recordButton.setEnabled(true);
                binding.statusTextView.setText("Воспроизведение остановлено");
            }
            isPlaying = !isPlaying;
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && android.os.Build.VERSION.SDK_INT < 29)) {
            requestPermissions(new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Разрешения получены", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Разрешения необходимы для работы диктофона", Toast.LENGTH_SHORT).show();
                // Можно закрыть фрагмент или показать предупреждение
                binding.recordButton.setEnabled(false);
                binding.playButton.setEnabled(false);
            }
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
            binding.recordButton.setEnabled(true);
            binding.recordButton.setText("Начать запись");
            isRecording = false;
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            recorder.release();
            recorder = null;
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
            player.setOnCompletionListener(mp -> {
                stopPlaying();
                if (getActivity() != null) {
                    requireActivity().runOnUiThread(() -> {
                        binding.playButton.setText("Воспроизвести");
                        binding.recordButton.setEnabled(true);
                        binding.statusTextView.setText("Воспроизведение завершено");
                        isPlaying = false;
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRecording) {
            stopRecording();
            isRecording = false;
        }
        if (isPlaying) {
            stopPlaying();
            isPlaying = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
        binding = null;
    }
}