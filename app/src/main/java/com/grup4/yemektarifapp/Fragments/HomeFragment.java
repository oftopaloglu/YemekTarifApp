package com.grup4.yemektarifapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.grup4.yemektarifapp.R;
import com.grup4.yemektarifapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.btnTarifAra.setOnClickListener(this::yemekArama);

        return binding.getRoot();
    }
    private void yemekArama(View view) {
        String editTarifAra = binding.editTarifAra.getText().toString();
        Toast.makeText(getActivity(), editTarifAra, Toast.LENGTH_SHORT).show();
        System.out.println(editTarifAra);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
