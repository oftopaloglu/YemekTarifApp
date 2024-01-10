package com.grup4.yemektarifapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.adaptor.TariflerAdaptor;
import com.grup4.yemektarifapp.databinding.FragmentResultsBinding;

import java.util.List;

public class ResultsFragment extends Fragment {

    private FragmentResultsBinding binding;
    private TariflerAdaptor tariflerAdaptor;

    public ResultsFragment(List<FoodRecipe> foodRecipes) {
        this.tariflerAdaptor = new TariflerAdaptor(getContext(), foodRecipes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResultsBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(tariflerAdaptor);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
