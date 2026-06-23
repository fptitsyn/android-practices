package ru.mirea.ptitsyn.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.util.ArrayList;
import java.util.List;
import ru.mirea.ptitsyn.mireaproject.databinding.FragmentPlacesBinding;

public class PlacesFragment extends Fragment {
    private FragmentPlacesBinding binding;
    private MapView mapView;
    private List<Establishment> establishments;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));
        binding = FragmentPlacesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = binding.mapView;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(14.0);
        mapView.getController().setCenter(new GeoPoint(55.751574, 37.573856)); // Москва

        initEstablishments();
        addMarkersToMap(establishments);

        binding.btnSearch.setOnClickListener(v -> searchEstablishment());
    }

    private void initEstablishments() {
        establishments = new ArrayList<>();
        establishments.add(new Establishment("Кофейня 'Уют'", new GeoPoint(55.753782, 37.624971),
                "ул. Никольская, 10", "Отличный кофе и десерты"));
        establishments.add(new Establishment("Ресторан 'Прага'", new GeoPoint(55.752544, 37.585892),
                "Арбат, 2", "Традиционная русская кухня"));
        establishments.add(new Establishment("Пиццерия 'Италия'", new GeoPoint(55.760186, 37.618514),
                "ул. Петровка, 15", "Лучшая пицца в городе"));
        establishments.add(new Establishment("Кафе 'Дружба'", new GeoPoint(55.703918, 37.529812),
                "Ленинские горы, 1", "Вид на МГУ"));
        establishments.add(new Establishment("Бургерная 'Meat'", new GeoPoint(55.769163, 37.636456),
                "Цветной бульвар, 5", "Сочные бургеры"));
    }

    private void addMarkersToMap(List<Establishment> list) {
        mapView.getOverlays().clear();
        for (Establishment place : list) {
            Marker marker = new Marker(mapView);
            marker.setPosition(place.getLocation());
            marker.setTitle(place.getName());
            marker.setSubDescription(place.getAddress() + "\n" + place.getDescription());
            marker.setOnMarkerClickListener((m, map) -> {
                Toast.makeText(getContext(),
                        m.getTitle() + "\n" + m.getSubDescription(),
                        Toast.LENGTH_LONG).show();
                return true;
            });
            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }

    private void searchEstablishment() {
        String query = binding.etSearch.getText().toString().toLowerCase().trim();
        if (query.isEmpty()) {
            addMarkersToMap(establishments);
            return;
        }
        List<Establishment> filtered = new ArrayList<>();
        for (Establishment e : establishments) {
            if (e.getName().toLowerCase().contains(query) ||
                    e.getAddress().toLowerCase().contains(query) ||
                    e.getDescription().toLowerCase().contains(query)) {
                filtered.add(e);
            }
        }
        if (filtered.isEmpty()) {
            Toast.makeText(getContext(), "Ничего не найдено", Toast.LENGTH_SHORT).show();
        } else {
            addMarkersToMap(filtered);
            if (filtered.size() == 1) {
                mapView.getController().animateTo(filtered.get(0).getLocation());
                mapView.getController().setZoom(16.0);
            }
        }
    }

    private static class Establishment {
        String name;
        GeoPoint location;
        String address;
        String description;
        Establishment(String name, GeoPoint location, String address, String description) {
            this.name = name; this.location = location; this.address = address; this.description = description;
        }
        String getName() { return name; }
        GeoPoint getLocation() { return location; }
        String getAddress() { return address; }
        String getDescription() { return description; }
    }
}