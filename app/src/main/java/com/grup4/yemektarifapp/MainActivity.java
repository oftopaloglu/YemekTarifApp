package com.grup4.yemektarifapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.grup4.yemektarifapp.Fragments.HomeFragment;
import com.grup4.yemektarifapp.Fragments.ProfileFragment;
import com.grup4.yemektarifapp.Fragments.SpecificationsFragment;
import com.grup4.yemektarifapp.Fragments.addSpecificationsFragment;
import com.grup4.yemektarifapp.Fragments.favsFragment;
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
                replaceFragment(new favsFragment());
            } else if (id == R.id.addSpecifications) {
                replaceFragment(new addSpecificationsFragment());
            } else if (id == R.id.specifications) {
                replaceFragment(new SpecificationsFragment());
            } else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
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