package com.grup4.yemektarifapp.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.OnProgressListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.grup4.yemektarifapp.MainActivity;
import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.databinding.FragmentAddSpecificationsBinding;
import com.grup4.yemektarifapp.databinding.DialogYapilisEkleBinding;
import com.grup4.yemektarifapp.databinding.DialogMalzemeEkleBinding;
import com.grup4.yemektarifapp.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;


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

        binding.editYemekAdi.setOnEditorActionListener(this::yemekAdiEkle);

        binding.btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == MainActivity.RESULT_OK && data != null && data.getData() != null) {

            image = data.getData();
            Glide.with(this).load(image).into(binding.selectedImage);
        }
    }

    private void uploadImage(Uri file) {
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed!" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void firebaseKaydet() {
        if (!foodRecipe.isEmpty()){
        Gson gson = new Gson();
        String json = gson.toJson(foodRecipe);
        Map<String, Object> tarifMap = gson.fromJson(json, Map.class);
        // oft bunu neden isim yaptın
        db.collection("tarifler").document(foodRecipe.getName()).set(tarifMap);
        uploadImage(image);
    }}

    

    private boolean yemekAdiEkle(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            foodRecipe.setName(v.getText().toString().trim());
            System.out.println(" ife girdi yemek adı  " + foodRecipe.getName());
            return true;
        }
        return false;
    }
    private void malzemeEkleDialog() {
        System.out.println("1");
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
        System.out.println("2");
        if (!yeniMalzeme.isEmpty()) {
            MalzemeListesi.add(yeniMalzeme);
            MalzemeAdapter.notifyDataSetChanged();

            // Eklendiğinde FoodRecipe sınıfındaki material listesine eklemeyi çağır
            foodRecipe.setMaterials(MalzemeListesi);

            // Eklendikten sonra listenin durumunu ekrana yazdır
            System.out.println("getMaterials " + foodRecipe.getMaterials());
        }
    }

    private void yapilisEkleDialog() {
        System.out.println("3");
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
    private void listeyeYapilisEkle(String yeniYapilis){
        if (!yeniYapilis.isEmpty()){
            YapilisListesi.add(yeniYapilis);
            YapilisAdapter.notifyDataSetChanged();

            foodRecipe.setNotes(YapilisListesi);

            System.out.println("getNotes " + foodRecipe.getNotes());
        }
    }
    private void fetchDataFromFirestore() {
        db.collection("tarifler").document("musakka")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        FoodRecipe fetchedRecipe = documentSnapshot.toObject(FoodRecipe.class);

                        System.out.println(fetchedRecipe.getMaterials());
                    } else {
                        // Document does not exist
                        System.out.println("olmadı");
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("o2");
                    // Handle failures in fetching data
                    // e.g., Log the error or show a message to the user
                });
    }


// kod burada kalsın  eklemek istediğimizde kullanırız
    // bu kodu kullanarak firabase  model listesi ekleyeceğiz veri ekleyebiliriz
    /*private void firebaseKaydetFoodRecipeList() {

        List<FoodRecipe> tarifListesi = new ArrayList<>();

        // Kek tarifi
        FoodRecipe kekTarifi = new FoodRecipe();
        kekTarifi.setName("Kek");
        List<String> kekYapilis = new ArrayList<>();
        kekYapilis.add("Un, şeker, yumurta, sıvı yağ, süt, kabartma tozu ve vanilyayı bir karıştırma kabında karıştırın.");
        kekYapilis.add("Kek kalıbını yağlayın ve hazırladığınız karışımı içine dökün.");
        kekYapilis.add("Önceden ısıtılmış 180 derece fırında kek pişene kadar yaklaşık 25-30 dakika pişirin.");
        kekYapilis.add("Pişen keki fırından çıkarın, soğuduktan sonra dilimleyerek servis yapın.");
        kekTarifi.setNotes(kekYapilis);
        List<String> kekMalzemeler = new ArrayList<>();
        kekMalzemeler.add("Un");
        kekMalzemeler.add("Şeker");
        kekMalzemeler.add("Yumurta");
        kekMalzemeler.add("Sıvı Yağ");
        kekMalzemeler.add("Süt");
        kekMalzemeler.add("Kabartma Tozu");
        kekMalzemeler.add("Vanilya");
        kekTarifi.setMaterials(kekMalzemeler);
        tarifListesi.add(kekTarifi);

        // Ekmek tarifi
        FoodRecipe ekmekTarifi = new FoodRecipe();
        ekmekTarifi.setName("Ekmek");
        List<String> ekmekYapilis = new ArrayList<>();
        ekmekYapilis.add("Un, su, tuz ve mayayı bir kapta karıştırarak hamur elde edin.");
        ekmekYapilis.add("Hamuru yoğurun ve mayalanmaya bırakın, hamur iki katına çıkana kadar bekleyin.");
        ekmekYapilis.add("Mayalanan hamuru şekillendirin ve fırın tepsisine ya da ekmek kalıbına koyun.");
        ekmekYapilis.add("Önceden ısıtılmış 200 derece fırında yaklaşık 30-40 dakika pişirin.");
        ekmekTarifi.setNotes(ekmekYapilis);
        List<String> ekmekMalzemeler = new ArrayList<>();
        ekmekMalzemeler.add("Un");
        ekmekMalzemeler.add("Su");
        ekmekMalzemeler.add("Tuz");
        ekmekMalzemeler.add("Maya");
        ekmekTarifi.setMaterials(ekmekMalzemeler);
        tarifListesi.add(ekmekTarifi);


        FoodRecipe pudingTarifi = new FoodRecipe();
        pudingTarifi.setName("Puding");

// Yapılışı hakkında kısa bilgi ekleme
        List<String> yapilis = new ArrayList<>();
        yapilis.add("Puding karışımını bir tencereye alın.");
        yapilis.add("Üzerine sütü ekleyin ve karıştırın.");
        yapilis.add("Ocağa alın ve orta ateşte sürekli karıştırarak pişirin.");
        yapilis.add("Pişen pudingi kaselere veya kaplara dökün ve buzdolabında soğutun.");
        pudingTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
        List<String> malzemeler = new ArrayList<>();
        malzemeler.add("Puding Karışımı");
        malzemeler.add("Süt");

        pudingTarifi.setMaterials(malzemeler);
        tarifListesi.add(pudingTarifi);



        FoodRecipe kahveTarifi = new FoodRecipe();
        kahveTarifi.setName("Kahve");

// Yapılışı hakkında kısa bilgi ekleme
        List<String> yapilis0 = new ArrayList<>();
        yapilis.add("Su kaynatın ve demliğe veya fincana koyun.");
        yapilis.add("Kahveyi ekleyin ve karıştırın.");
        yapilis.add("İsteğe bağlı olarak şeker veya süt ekleyerek servis yapın.");
        kahveTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
        List<String> malzemeler0 = new ArrayList<>();
        malzemeler.add("Su");
        malzemeler.add("Kahve");
// İsteğe bağlı olarak şeker veya süt eklenebilir

        kahveTarifi.setMaterials(malzemeler);
        tarifListesi.add(kahveTarifi);

        FoodRecipe cayTarifi = new FoodRecipe();
        cayTarifi.setName("Çay");

// Yapılışı hakkında kısa bilgi ekleme
        List<String> yapilis1 = new ArrayList<>();
        yapilis.add("Su kaynatın ve çaydanlık veya demlik içine koyun.");
        yapilis.add("Çayı ekleyin ve demlenmeye bırakın (demlenme süresi kişisel tercihe göre değişebilir).");
        yapilis.add("Demlendikten sonra isteğe bağlı olarak şeker veya limon ekleyerek servis yapın.");
        cayTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
        List<String> malzemeler1 = new ArrayList<>();
        malzemeler.add("Su");
        malzemeler.add("Çay");

        cayTarifi.setMaterials(malzemeler);

        tarifListesi.add(cayTarifi);
        FoodRecipe tavukSoteTarifi = new FoodRecipe();
        tavukSoteTarifi.setName("Tavuk Sote");

// Yapılışı hakkında kısa bilgi ekleme
        List<String> yapilis2 = new ArrayList<>();
        yapilis.add("Tavukları küp şeklinde doğrayın ve bir tavada yağ ile kızartın.");
        yapilis.add("Üzerine doğranmış soğan, biber ve domatesi ekleyip soteleyin.");
        yapilis.add("Baharatlarınızı ve isteğe bağlı olarak sos ekleyebilirsiniz.");
        yapilis.add("Pişirme işlemi tamamlandıktan sonra sıcak olarak servis yapın.");
        tavukSoteTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
        List<String> malzemeler2 = new ArrayList<>();
        malzemeler.add("Tavuk");
        malzemeler.add("Soğan");
        malzemeler.add("Biber");
        malzemeler.add("Domates");
        malzemeler.add("Yağ");
        malzemeler.add("Baharatlar");
        malzemeler.add("Sos (isteğe bağlı)");

        tavukSoteTarifi.setMaterials(malzemeler);


        FoodRecipe tavukFirinTarifi = new FoodRecipe();
        tavukFirinTarifi.setName("Tavuk Fırın");
        tarifListesi.add(pudingTarifi);
// Yapılışı hakkında kısa bilgi ekleme
        List<String> yapilis3 = new ArrayList<>();
        yapilis.add("Tavukları temizleyin ve üzerindeki tüyleri alın. Tavukları yıkayın ve kurulayın.");
        yapilis.add("Fırın tepsisine ya da fırın poşetine tavukları yerleştirin.");
        yapilis.add("Tavukların üzerine zeytinyağı, tuz, karabiber ve isteğe bağlı baharatlarla marine edin.");
        yapilis.add("Önceden ısıtılmış 180 derece fırında tavukları pişirin. Pişme süresi tavuk büyüklüğüne bağlı olarak değişebilir.");
        tavukFirinTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
        List<String> malzemeler3 = new ArrayList<>();
        malzemeler.add("Tavuk");
        malzemeler.add("Zeytinyağı");
        malzemeler.add("Tuz");
        malzemeler.add("Karabiber");
// İsteğe bağlı olarak diğer baharatlar veya aromalar eklenebilir

        tavukFirinTarifi.setMaterials(malzemeler);
        tarifListesi.add(tavukFirinTarifi);
        tarifListesi.add(tavukSoteTarifi);
// Tarif oluşturuldu: tavukFirinTarifi
        FoodRecipe mangalTarifi = new FoodRecipe();
        mangalTarifi.setName("Mangal");

// Yapılışı hakkında kısa bilgi ekleme
        List<String> yapilis4 = new ArrayList<>();
        yapilis.add("Mangal kömürlerini hazırlayın ve ateşi yakın. Kömürler iyice kızarana kadar bekleyin.");
        yapilis.add("Mangal ızgarasını ateşin üzerine yerleştirin ve ızgarayı iyice ısıtın.");
        yapilis.add("Izgaranın üzerine marine ettiğiniz etleri, sebzeleri veya istediğiniz malzemeleri yerleştirin.");
        yapilis.add("Malzemeler pişene kadar ara ara çevirerek kontrollü bir şekilde pişirin.");
        mangalTarifi.setNotes(yapilis);
        tarifListesi.add(pudingTarifi);
// Yemek yapımında kullanılan malzemeleri ekleyelim (genel olarak mangal için)
        List<String> malzemeler4 = new ArrayList<>();
        malzemeler.add("Et (Köfte, Tavuk, Kırmızı Et vb.)");
        malzemeler.add("Sebzeler (Biber, Domates, Patlıcan, Mantar vb.)");
        malzemeler.add("Mangal Kömürü");
        malzemeler.add("Mangal Izgarası");

        mangalTarifi.setMaterials(malzemeler);
        tarifListesi.add(mangalTarifi);
// Tarif oluşturuldu: mangalTarifi



        FoodRecipe kofteTarifi = new FoodRecipe();
        kofteTarifi.setName("Köfte");

// Yapılışı hakkında kısa bilgi ekleme
        List<String> yapilis5 = new ArrayList<>();
        yapilis.add("Kıymayı bir kaseye alın, üzerine rendelenmiş soğanı, galeta unu veya ekmek içini, tuz, karabiber, kimyon ve isteğe bağlı olarak maydanozu ekleyin.");
        yapilis.add("Malzemeleri yoğurarak köfte harcı elde edin. Harçtan parçalar koparıp yuvarlayarak köfte şekli verin.");
        yapilis.add("Tavada veya ızgara üzerinde köfteleri pişirin ve servis yapın.");
        kofteTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
        List<String> malzemeler5 = new ArrayList<>();
        malzemeler.add("Kıyma");
        malzemeler.add("Soğan");
        malzemeler.add("Galeta Unu veya Ekmek İçi");
        malzemeler.add("Tuz");
        malzemeler.add("Karabiber");
        malzemeler.add("Kimyon");
// İsteğe bağlı olarak maydanoz veya diğer baharatlar eklenebilir

        kofteTarifi.setMaterials(malzemeler);
        tarifListesi.add(kofteTarifi);
// Tarif oluşturuldu: kofteTarifi





        FoodRecipe makarnaTarifi = new FoodRecipe();
        makarnaTarifi.setName("Makarna");

// Yapılışı hakkında kısa bilgi ekleme
        List<String> yapilis7 = new ArrayList<>();
        yapilis.add("Tencerede su kaynatın ve içine bir tutam tuz atın.");
        yapilis.add("Makarnayı kaynar suya ekleyin ve paketin üzerinde belirtilen süre kadar pişirin.");
        yapilis.add("Pişen makarnayı süzün ve isteğe bağlı sos veya zeytinyağı ile karıştırarak servis yapın.");
        makarnaTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
        List<String> malzemeler7 = new ArrayList<>();
        malzemeler.add("Makarna");
        malzemeler.add("Tuz");
// İsteğe bağlı olarak sos veya zeytinyağı eklenebilir

        makarnaTarifi.setMaterials(malzemeler);
        tarifListesi.add(makarnaTarifi);
// Tarif oluşturuldu: makarnaTarifi
        FoodRecipe recipe = new FoodRecipe();
        recipe.setName("Pilav");

        // Yapılışı hakkında kısa bilgi ekleme
        String instructions = "Pilav yapmak için pirinci bir süzgece alıp su altında yıkayın ve süzün. "
                + "Bir tencerede yağı kızdırın, üzerine pirinci ekleyip kavurun. "
                + "Sıcak suyu ve tuzu ekleyin, kaynamaya bırakın. Kaynadıktan sonra kısık ateşte suyunu çekene kadar pişirin. "
                + "Pişen pilavın üstüne temiz bir bez örterek demlenmeye bırakın. Demlendikten sonra servis yapabilirsiniz.";
        recipe.setNotes(Collections.singletonList(instructions));

        // Yemek yapımında kullanılan malzemeleri ekleyelim
        List<String> materials = new ArrayList<>();
        materials.add("Pirinç");
        materials.add("Su");
        materials.add("Yağ");
        materials.add("Tuz");

        recipe.setMaterials(materials);
        tarifListesi.add(recipe);
        for (FoodRecipe recipes : tarifListesi) {
            // Belge kimliğini özel olarak atayarak ekleme işlemi
            db.collection("tarifler").document(recipes.getId()).set(recipes)
                    .addOnSuccessListener(aVoid -> {
                        // Ekleme başarılı olduğunda yapılacak işlemler
                        Log.d("Firestore", "Belge başarıyla eklendi. ID: " + recipes.getId());
                    })
                    .addOnFailureListener(e -> {
                        // Ekleme başarısız olduğunda yapılacak işlemler
                        Log.w("Firestore", "Belge eklenirken hata oluştu.", e);
                    });
        }

    }
*/

}
