package com.grup4.yemektarifapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.grup4.yemektarifapp.R;
import com.grup4.yemektarifapp.UserServices.UserService;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginFragment extends Fragment {

    private Button googleAuth;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(@NonNull View view) {
        googleAuth = view.findViewById(R.id.btnGoogleAuth);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        googleAuth.setOnClickListener(this::onGoogleAuthClick);
    }

    private void onGoogleAuthClick(View view) {
        googleSignIn();
    }

    private void googleSignIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            handleGoogleSignInResult(data);
        }
    }

    private void handleGoogleSignInResult(Intent data) {
        try {
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
            firebaseAuth(account.getIdToken());
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this::onAuthComplete);
    }

    private void onAuthComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            FirebaseUser user = auth.getCurrentUser();
            replaceFragment(new HomeFragment());
            //saveUserToDatabase(user);
            UserService.saveUserToDatabase(user , database );
        } else {
            Toast.makeText(getActivity(), "Doğrulama başarısız.", Toast.LENGTH_SHORT).show();
        }
    }

/*    private void saveUserToDatabase(FirebaseUser user) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", user.getUid());
        map.put("name", user.getDisplayName());
        map.put("profile", user.getPhotoUrl().toString());

        // FavRecipes örneği
        ArrayList<String> favRecipes = new ArrayList<>();

        map.put("favRecipes", favRecipes);

        // MyRecipes örneği
        ArrayList<String> myRecipes = new ArrayList<>();
        map.put("MyRecipes", myRecipes);

        database.getReference().child("users").child(user.getUid()).setValue(map);
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
