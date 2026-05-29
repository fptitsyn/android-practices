package ru.mirea.ptitsyn.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import ru.mirea.ptitsyn.mireaproject.databinding.FragmentPermissionBinding;

public class PermissionFragment extends Fragment {
    private FragmentPermissionBinding binding;
    private static final int REQUEST_CAMERA = 101;
    private static final int REQUEST_MIC = 102;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPermissionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCheckCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                binding.tvPermissionStatus.setText("Камера: РАЗРЕШЕНА");
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            }
        });

        binding.btnCheckMicrophone.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                binding.tvPermissionStatus.setText("Микрофон: РАЗРЕШЕН");
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MIC);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.tvPermissionStatus.setText("Камера: РАЗРЕШЕНА");
            } else {
                binding.tvPermissionStatus.setText("Камера: ЗАПРЕЩЕНА");
            }
        } else if (requestCode == REQUEST_MIC) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.tvPermissionStatus.setText("Микрофон: РАЗРЕШЕН");
            } else {
                binding.tvPermissionStatus.setText("Микрофон: ЗАПРЕЩЕН");
            }
        }
    }
}