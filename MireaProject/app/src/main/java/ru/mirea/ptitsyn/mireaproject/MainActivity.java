package ru.mirea.ptitsyn.mireaproject;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open, R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DataFragment())
                    .commit();
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.nav_data) {
                    selectedFragment = new DataFragment();
                } else if (itemId == R.id.nav_web) {
                    selectedFragment = new WebViewFragment();
                } else if (itemId == R.id.nav_worker) {
                    selectedFragment = new BackgroundTaskFragment();
                } else if (itemId == R.id.nav_compass) {
                    selectedFragment = new CompassFragment();
                }
                else if (itemId == R.id.nav_photo_note) {
                    selectedFragment = new PhotoNoteFragment();
                }
                else if (itemId == R.id.nav_voice) {
                    selectedFragment = new VoiceRecorderFragment();
                }
                else if (itemId == R.id.nav_permission) {
                    selectedFragment = new PermissionFragment();
                }
                else if (item.getItemId() == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                } else if (item.getItemId() == R.id.nav_file_work) {
                    selectedFragment = new FileWorkFragment();
                }
                else if (item.getItemId() == R.id.nav_network) {
                    selectedFragment = new NetworkFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                drawerLayout.closeDrawers();

                return true;
            }
        });
    }
}