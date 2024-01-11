

package com.grup4.yemektarifapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.grup4.yemektarifapp.MainActivity;
import com.grup4.yemektarifapp.R;
import androidx.appcompat.widget.AppCompatButton;

public class ProfileFragment extends Fragment {

    private AppCompatButton favFoodsButton;
    private AppCompatButton recipesButton;
    private AppCompatButton logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile2, container, false);

        logoutButton = view.findViewById(R.id.btnLogoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // Çıkış işlemi sonrasında MainActivity'ye yönlendirme işlemi
                replaceFragment(new HomeFragment());
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userName = user.getDisplayName();
            String userEmail = user.getEmail();

            TextView nameTextView = view.findViewById(R.id.nickName);
            TextView emailTextView = view.findViewById(R.id.email);

            nameTextView.setText(userName);
            emailTextView.setText(userEmail);
        }

        favFoodsButton = view.findViewById(R.id.favFoods);
        recipesButton = view.findViewById(R.id.recipes);

        favFoodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Favori Yemekler butonuna tıklanınca FavoritesFragment'e geçiş yap
                replaceFragment(new FavoritesFragment());
            }
        });

        recipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Yemek Tarifleri butonuna tıklanınca SpecificationsFragment'e geçiş yap
                replaceFragment(new SpecificationsFragment());
            }
        });

        return view;
    }

    // Eğer onStop metodu içinde HomeFragment'e geçiş yapmak istemiyorsanız bu metodu kaldırabilirsiniz.
    /*
    @Override
    public void onStop() {
        super.onStop();
        replaceFragment(new HomeFragment());
    }
    */

    private void replaceFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
