package ru.mirea.ptitsyn.mireaproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import ru.mirea.ptitsyn.mireaproject.databinding.FragmentPhotoNoteBinding;

public class PhotoNoteFragment extends Fragment {
    private FragmentPhotoNoteBinding binding;
    private Uri imageUri;
    private SharedPreferences prefs;

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && imageUri != null) {
                    binding.photoImageView.setImageURI(imageUri);
                    // сохраняем URI фото в SharedPreferences
                    prefs.edit().putString("last_photo_uri", imageUri.toString()).apply();
                } else {
                    Toast.makeText(getContext(), "Фото не получено", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPhotoNoteBinding.inflate(inflater, container, false);
        prefs = requireActivity().getSharedPreferences("PhotoNotes", 0);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // загружаем последнее фото, если есть
        String lastUri = prefs.getString("last_photo_uri", null);
        if (lastUri != null) {
            imageUri = Uri.parse(lastUri);
            binding.photoImageView.setImageURI(imageUri);
        }
        // загружаем сохранённую заметку
        String savedNote = prefs.getString("note", "");
        binding.noteEditText.setText(savedNote);

        binding.takePhotoButton.setOnClickListener(v -> takePhoto());
        binding.saveNoteButton.setOnClickListener(v -> {
            String note = binding.noteEditText.getText().toString();
            prefs.edit().putString("note", note).apply();
            Toast.makeText(getContext(), "Заметка сохранена", Toast.LENGTH_SHORT).show();
        });
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                String authorities = requireContext().getPackageName() + ".fileprovider";
                imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraLauncher.launch(intent);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}