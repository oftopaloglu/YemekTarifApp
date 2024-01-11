package com.grup4.yemektarifapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.R;
import com.grup4.yemektarifapp.SingletonList;
import com.grup4.yemektarifapp.databinding.FragmentHomeBinding;
import com.grup4.yemektarifapp.firebase.serach.FirestoreCallback;
import com.grup4.yemektarifapp.firebase.serach.FirestoreFoodSearch;
import com.grup4.yemektarifapp.services.GetRecipeList;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirestoreFoodSearch firestoreFoodSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        firestoreFoodSearch = new FirestoreFoodSearch();

        binding.btnTarifAra.setOnClickListener(this::yemekArama);

        // Firestore'dan veriyi çekme işlemini gerçekleştir
        fetchFirestoreData();

        return binding.getRoot();
    }

    private void fetchFirestoreData() {
        GetRecipeList getRecipeList = new GetRecipeList();
        getRecipeList.fetchFirestoreData(new FirestoreCallback() {
            @Override
            public void onCallback(List<FoodRecipe> list) {
                SingletonList singletonList = SingletonList.getInstance();
                singletonList.setRecipeList(list);

                // Firebase'den veri çekildikten sonra random bir öğeyi göster
                showRandomRecipe();
            }
        });
    }

    private void showRandomRecipe() {
        SingletonList singletonList = SingletonList.getInstance();
        List<FoodRecipe> recipeListsRandom = singletonList.getRecipeList();
        Random random = new Random();

        if (!recipeListsRandom.isEmpty()) {
            int randomIndex = random.nextInt(recipeListsRandom.size());
            FoodRecipe randomRecipe = recipeListsRandom.get(randomIndex);

            loadImage(requireContext(), randomRecipe.getPhotoUrlBytes(), binding.imageHome);
            binding.foodName.setText(randomRecipe.getName());
        } else {
            Log.e("HomeFragment", "Recipe list is empty");
        }
    }

    private void yemekArama(View view) {
        String editTarifAra = binding.editTarifAra.getText().toString();
        firestoreFoodSearch.searchDocumentsByFieldName("tarifler", "name", editTarifAra, new FirestoreCallback() {
            @Override
            public void onCallback(List<FoodRecipe> list) {
                // Handle the list of FoodRecipe objects here
                Toast.makeText(getActivity(), "Found " + list.size() + " results", Toast.LENGTH_SHORT).show();
                // Create a new ResultsFragment with the list of FoodRecipe objects
                ResultsFragment resultsFragment = new ResultsFragment(list);

                // Replace the current fragment with the ResultsFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, resultsFragment)
                        .addToBackStack(null)
                        .commit();
                System.out.println("Found " + list.size() + " results");
                for (FoodRecipe recipe : list) {
                    System.out.println(recipe.toString());
                }
            }
        });
    }

    private void loadImage(Context context, String imageURL, ImageView imageView) {
        Glide.with(context)
                .load(imageURL)
                .into(imageView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
