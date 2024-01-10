package com.grup4.yemektarifapp.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.databinding.ItemBinding;

import java.util.List;

public class TariflerAdaptor extends RecyclerView.Adapter<TariflerAdaptor.ViewHolder> {

    private final List<FoodRecipe> foodRecipeList;

    public TariflerAdaptor(Context context, List<FoodRecipe> foodRecipeList) {
        this.foodRecipeList = foodRecipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // View Binding ile oluşturulan ItemBinding sınıfını kullanarak inflater yapalım
        ItemBinding binding = ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodRecipe foodRecipe = foodRecipeList.get(position);
        holder.bind(foodRecipe);
    }

    @Override
    public int getItemCount() {
        return foodRecipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemBinding binding;

        public ViewHolder(@NonNull ItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FoodRecipe foodRecipe) {
            // FoodRecipe nesnesinden verileri al
            // Yemek adı
            binding.dishName.setText(foodRecipe.getName());

            // Malzemeler
            List<String> materials = foodRecipe.getMaterials();
            if (materials != null && !materials.isEmpty()) {
                binding.ingredientsLayout.removeAllViews(); // Önceki malzemeleri temizle
                for (String material : materials) {
                    TextView ingredientTextView = new TextView(binding.getRoot().getContext());
                    ingredientTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    ingredientTextView.setText("- " + material);
                    binding.ingredientsLayout.addView(ingredientTextView);
                }
            }

            // Yemek Fotoğrafı
            String photoUrl = foodRecipe.getPhotoUrl();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                Glide.with(binding.getRoot().getContext()).load(photoUrl).into(binding.foodImage);
            }

            // Yapılış Şekli
            List<String> steps = foodRecipe.getNotes();
            if (steps != null && !steps.isEmpty()) {
                binding.preparationLayout.removeAllViews(); // Önceki adımları temizle
                for (String step : steps) {
                    TextView stepTextView = new TextView(binding.getRoot().getContext());
                    stepTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    stepTextView.setText("- " + step);
                    binding.preparationLayout.addView(stepTextView);
                }
            }
        }
    }
}
