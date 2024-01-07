package com.grup4.yemektarifapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.grup4.yemektarifapp.R;

import androidx.appcompat.widget.AppCompatButton;

public class ProfileFragment extends Fragment {

    private AppCompatButton logoutButton;
    private AppCompatButton googleAuthButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile2, container, false);

        logoutButton = view.findViewById(R.id.btnLogoutButton);
        googleAuthButton = view.findViewById(R.id.btnGoogleAuth);

        // Sol drawer'ı tanımla
        final DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);

        // Sol drawer içeriğini tanımla
        final View leftDrawer = view.findViewById(R.id.left_drawer);

        googleAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Google Auth işlemleri burada gerçekleştirilebilir
                // Sol drawer'ı aç
                drawerLayout.openDrawer(leftDrawer);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // Sol drawer'ı kapat (çıkış yap butonu için)
                drawerLayout.closeDrawer(leftDrawer);

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

        return view;
    }

    // Yeni eklenen metot: Fragment değiştirme işlemi
    private void replaceFragment(Fragment fragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
