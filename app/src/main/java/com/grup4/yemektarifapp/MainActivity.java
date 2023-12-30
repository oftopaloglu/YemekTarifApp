package com.grup4.yemektarifapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.grup4.yemektarifapp.Fragments.HomeFragment;
import com.grup4.yemektarifapp.Fragments.LoginFragment;
import com.grup4.yemektarifapp.Fragments.SpecificationsFragment;
import com.grup4.yemektarifapp.Fragments.AddSpecificationsFragment;
import com.grup4.yemektarifapp.Fragments.FavoritesFragment;
import com.grup4.yemektarifapp.databinding.ActivityMainBinding;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(a -> {
            int id = a.getItemId();
            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.favs) {
                replaceFragment(new FavoritesFragment());
            } else if (id == R.id.addSpecifications) {
                replaceFragment(new AddSpecificationsFragment());
            } else if (id == R.id.specifications) {
                replaceFragment(new SpecificationsFragment());
            } else if (id == R.id.profile) {
                replaceFragment(new LoginFragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}