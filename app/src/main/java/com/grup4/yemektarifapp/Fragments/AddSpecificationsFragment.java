package com.grup4.yemektarifapp.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.grup4.yemektarifapp.databinding.FragmentAddSpecificationsBinding;
import com.grup4.yemektarifapp.databinding.DialogMalzemeEkleBinding;
import com.grup4.yemektarifapp.databinding.DialogYapilisEkleBinding;
import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.utils.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class AddSpecificationsFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private FragmentAddSpecificationsBinding binding;
    private ArrayList<String> MalzemeListesi;
    private ArrayAdapter<String> MalzemeAdapter;
    private ArrayList<String> YapilisListesi;
    private ArrayAdapter<String> YapilisAdapter;
    private FirebaseFirestore db;
    private FoodRecipe foodRecipe;
    private StorageReference storageReference;
    private Uri image;
    private boolean isPhotoAdded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddSpecificationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        foodRecipe = new FoodRecipe();

        MalzemeListesi = new ArrayList<>();
        MalzemeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, MalzemeListesi);

        YapilisListesi = new ArrayList<>();
        YapilisAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, YapilisListesi);

        binding.btnMalzemeler.setOnClickListener(v -> malzemeEkleDialog());
        binding.btnYapilis.setOnClickListener(v -> yapilisEkleDialog());
        binding.btnKaydet.setOnClickListener(v -> firebaseKaydet());

        binding.editYemekAdi.addTextChangedListener(yemekAdiEkle());

        binding.btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Görsel Seç"), PICK_IMAGE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            image = data.getData();

            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(image);
                int fileSize = inputStream.available();

                float sizeInMB = (float) fileSize / (1024 * 1024);

                if (sizeInMB > 5) {
                    Toast.makeText(getActivity(), "Dosya boyutu 5MB'den büyük olamaz.", Toast.LENGTH_SHORT).show();
                    image = null;
                } else {
                    uploadImage(image);
                    Glide.with(this).load(image).into(binding.selectedImage);
                    isPhotoAdded = true;
                    updateLayout();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void updateLayout() {
        if (isPhotoAdded) {
            binding.selectedImage.setVisibility(View.VISIBLE);
            binding.btnAddPhoto.setVisibility(View.VISIBLE);
            binding.selectedImage.setVisibility(View.VISIBLE); // Yeni eklenen kısım
        } else {
            binding.selectedImage.setVisibility(View.GONE);
            binding.btnAddPhoto.setVisibility(View.VISIBLE);
            binding.selectedImage.setVisibility(View.GONE); // Yeni eklenen kısım
        }
    }


    private void uploadImage(Uri file) {
        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            // imageUrl değişkeni yüklenen görüntünün URL'sini içerir.
                            foodRecipe.setPhotoUrlBytes(imageUrl);
                            Toast.makeText(getActivity(), "Görsel Yüklendi!! URL: " + imageUrl, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Başarısız!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void firebaseKaydet() {
        if (!foodRecipe.isEmpty()) {
            Gson gson = new Gson();
            String json = gson.toJson(foodRecipe);
            Map<String, Object> tarifMap = gson.fromJson(json, Map.class);
            db.collection("tarifler").document(foodRecipe.getName()).set(tarifMap);
        }
    }
/*
    private boolean yemekAdiEkle(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            foodRecipe.setName(v.getText().toString().trim());
            System.out.println(" ife girdi yemek adı  " + foodRecipe.getName());
            return true;
        }
        return false;
    }

 */
    private TextWatcher yemekAdiEkle() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                foodRecipe.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void malzemeEkleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Malzeme Ekle");

        DialogMalzemeEkleBinding malzemeBinding = DialogMalzemeEkleBinding.inflate(getLayoutInflater());
        builder.setView(malzemeBinding.getRoot());

        EditText malzemeEditText = malzemeBinding.editMalzeme;
        ListView malzemeListView = malzemeBinding.malzemeListView;
        malzemeListView.setAdapter(MalzemeAdapter);

        builder.setPositiveButton("Ekle", null);
        builder.setNegativeButton("Tamam", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String malzeme = malzemeEditText.getText().toString().trim();
            if (!malzeme.isEmpty()) {
                listeyeMalzemeEkle(malzeme);
                malzemeEditText.setText("");
            }
        });
    }

    private void listeyeMalzemeEkle(String yeniMalzeme) {
        if (!yeniMalzeme.isEmpty()) {
            MalzemeListesi.add(yeniMalzeme);
            MalzemeAdapter.notifyDataSetChanged();

            foodRecipe.setMaterials(MalzemeListesi);
        }
    }

    private void yapilisEkleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Yapılış Ekle");

        final DialogYapilisEkleBinding yapilisBinding = DialogYapilisEkleBinding.inflate(getLayoutInflater());
        builder.setView(yapilisBinding.getRoot());

        final EditText yapilisEditText = yapilisBinding.editYapilis;
        final ListView yapilisListView = yapilisBinding.yapilisListView;

        yapilisListView.setAdapter(YapilisAdapter);

        builder.setPositiveButton("Ekle", null);
        builder.setNegativeButton("Tamam", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String yapilis = yapilisEditText.getText().toString().trim();
            if (!yapilis.isEmpty()) {
                listeyeYapilisEkle(yapilis);
                yapilisEditText.setText("");
            }
        });
    }

    private void listeyeYapilisEkle(String yeniYapilis) {
        if (!yeniYapilis.isEmpty()) {
            YapilisListesi.add(yeniYapilis);
            YapilisAdapter.notifyDataSetChanged();

            foodRecipe.setNotes(YapilisListesi);
        }
    }
}
