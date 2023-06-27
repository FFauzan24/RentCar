package com.acenkzproject.rentcar.view.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.acenkzproject.rentcar.LoginActivity;
import com.acenkzproject.rentcar.R;
import com.acenkzproject.rentcar.databinding.ActivityListRentalMobilBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ListRentalMobil extends AppCompatActivity {

    private NavController navController;
    private ActivityListRentalMobilBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListRentalMobilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle("");
        setSupportActionBar(binding.toolbar);

        BottomNavigationView navigationView = findViewById(R.id.nav_view2);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment2);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_grid_mobil, R.id.navigation_transaksi, R.id.navigation_profile
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        binding.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}