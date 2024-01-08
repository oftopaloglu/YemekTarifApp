package com.grup4.yemektarifapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FoodRecipe {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("materials")
    private List<String> materials;

    @SerializedName("notes")
    private List<String> notes;

    @SerializedName("photo_url")
    private String photoUrl;
    public FoodRecipe() {
        // burada benzersiz id ekleme işlemi yapılmıştır
        this.id = UUID.randomUUID().toString();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getMaterials() { return materials; }
    public void setMaterials(List<String> materials) { this.materials = materials; }

    public List<String> getNotes() { return notes; }
    public void setNotes(List<String> notes) { this.notes = notes; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }


    public boolean isEmpty() {
        return name == null && (materials == null || materials.isEmpty()) &&
                (notes == null || notes.isEmpty());
    }

    @Override
    public String toString() {
        return "FoodRecipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", materials=" + materials +
                ", notes=" + notes +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
/* listeye add leme işlemini yazmayı unutmayın tarifler listesi ismi

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
tarifListesi.add(pudingTarifi)


FoodRecipe kahveTarifi = new FoodRecipe();
kahveTarifi.setName("Kahve");

// Yapılışı hakkında kısa bilgi ekleme
List<String> yapilis = new ArrayList<>();
yapilis.add("Su kaynatın ve demliğe veya fincana koyun.");
yapilis.add("Kahveyi ekleyin ve karıştırın.");
yapilis.add("İsteğe bağlı olarak şeker veya süt ekleyerek servis yapın.");
kahveTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
List<String> malzemeler = new ArrayList<>();
malzemeler.add("Su");
malzemeler.add("Kahve");
// İsteğe bağlı olarak şeker veya süt eklenebilir

kahveTarifi.setMaterials(malzemeler);


FoodRecipe cayTarifi = new FoodRecipe();
cayTarifi.setName("Çay");

// Yapılışı hakkında kısa bilgi ekleme
List<String> yapilis = new ArrayList<>();
yapilis.add("Su kaynatın ve çaydanlık veya demlik içine koyun.");
yapilis.add("Çayı ekleyin ve demlenmeye bırakın (demlenme süresi kişisel tercihe göre değişebilir).");
yapilis.add("Demlendikten sonra isteğe bağlı olarak şeker veya limon ekleyerek servis yapın.");
cayTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
List<String> malzemeler = new ArrayList<>();
malzemeler.add("Su");
malzemeler.add("Çay");

cayTarifi.setMaterials(malzemeler);


FoodRecipe tavukSoteTarifi = new FoodRecipe();
tavukSoteTarifi.setName("Tavuk Sote");

// Yapılışı hakkında kısa bilgi ekleme
List<String> yapilis = new ArrayList<>();
yapilis.add("Tavukları küp şeklinde doğrayın ve bir tavada yağ ile kızartın.");
yapilis.add("Üzerine doğranmış soğan, biber ve domatesi ekleyip soteleyin.");
yapilis.add("Baharatlarınızı ve isteğe bağlı olarak sos ekleyebilirsiniz.");
yapilis.add("Pişirme işlemi tamamlandıktan sonra sıcak olarak servis yapın.");
tavukSoteTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
List<String> malzemeler = new ArrayList<>();
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

// Yapılışı hakkında kısa bilgi ekleme
List<String> yapilis = new ArrayList<>();
yapilis.add("Tavukları temizleyin ve üzerindeki tüyleri alın. Tavukları yıkayın ve kurulayın.");
yapilis.add("Fırın tepsisine ya da fırın poşetine tavukları yerleştirin.");
yapilis.add("Tavukların üzerine zeytinyağı, tuz, karabiber ve isteğe bağlı baharatlarla marine edin.");
yapilis.add("Önceden ısıtılmış 180 derece fırında tavukları pişirin. Pişme süresi tavuk büyüklüğüne bağlı olarak değişebilir.");
tavukFirinTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
List<String> malzemeler = new ArrayList<>();
malzemeler.add("Tavuk");
malzemeler.add("Zeytinyağı");
malzemeler.add("Tuz");
malzemeler.add("Karabiber");
// İsteğe bağlı olarak diğer baharatlar veya aromalar eklenebilir

tavukFirinTarifi.setMaterials(malzemeler);

// Tarif oluşturuldu: tavukFirinTarifi
FoodRecipe mangalTarifi = new FoodRecipe();
mangalTarifi.setName("Mangal");

// Yapılışı hakkında kısa bilgi ekleme
List<String> yapilis = new ArrayList<>();
yapilis.add("Mangal kömürlerini hazırlayın ve ateşi yakın. Kömürler iyice kızarana kadar bekleyin.");
yapilis.add("Mangal ızgarasını ateşin üzerine yerleştirin ve ızgarayı iyice ısıtın.");
yapilis.add("Izgaranın üzerine marine ettiğiniz etleri, sebzeleri veya istediğiniz malzemeleri yerleştirin.");
yapilis.add("Malzemeler pişene kadar ara ara çevirerek kontrollü bir şekilde pişirin.");
mangalTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim (genel olarak mangal için)
List<String> malzemeler = new ArrayList<>();
malzemeler.add("Et (Köfte, Tavuk, Kırmızı Et vb.)");
malzemeler.add("Sebzeler (Biber, Domates, Patlıcan, Mantar vb.)");
malzemeler.add("Mangal Kömürü");
malzemeler.add("Mangal Izgarası");

mangalTarifi.setMaterials(malzemeler);

// Tarif oluşturuldu: mangalTarifi



FoodRecipe kofteTarifi = new FoodRecipe();
kofteTarifi.setName("Köfte");

// Yapılışı hakkında kısa bilgi ekleme
List<String> yapilis = new ArrayList<>();
yapilis.add("Kıymayı bir kaseye alın, üzerine rendelenmiş soğanı, galeta unu veya ekmek içini, tuz, karabiber, kimyon ve isteğe bağlı olarak maydanozu ekleyin.");
yapilis.add("Malzemeleri yoğurarak köfte harcı elde edin. Harçtan parçalar koparıp yuvarlayarak köfte şekli verin.");
yapilis.add("Tavada veya ızgara üzerinde köfteleri pişirin ve servis yapın.");
kofteTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
List<String> malzemeler = new ArrayList<>();
malzemeler.add("Kıyma");
malzemeler.add("Soğan");
malzemeler.add("Galeta Unu veya Ekmek İçi");
malzemeler.add("Tuz");
malzemeler.add("Karabiber");
malzemeler.add("Kimyon");
// İsteğe bağlı olarak maydanoz veya diğer baharatlar eklenebilir

kofteTarifi.setMaterials(malzemeler);

// Tarif oluşturuldu: kofteTarifi





FoodRecipe makarnaTarifi = new FoodRecipe();
makarnaTarifi.setName("Makarna");

// Yapılışı hakkında kısa bilgi ekleme
List<String> yapilis = new ArrayList<>();
yapilis.add("Tencerede su kaynatın ve içine bir tutam tuz atın.");
yapilis.add("Makarnayı kaynar suya ekleyin ve paketin üzerinde belirtilen süre kadar pişirin.");
yapilis.add("Pişen makarnayı süzün ve isteğe bağlı sos veya zeytinyağı ile karıştırarak servis yapın.");
makarnaTarifi.setNotes(yapilis);

// Yemek yapımında kullanılan malzemeleri ekleyelim
List<String> malzemeler = new ArrayList<>();
malzemeler.add("Makarna");
malzemeler.add("Tuz");
// İsteğe bağlı olarak sos veya zeytinyağı eklenebilir

makarnaTarifi.setMaterials(malzemeler);

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
 */