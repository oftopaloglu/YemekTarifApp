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

    // "photoUrl" yerine "photoUrlBytes" ile byte dizisi olarak fotoğrafı saklayalım
    @SerializedName("photoUrlBytes")
    private byte[] photoUrlBytes;

    public FoodRecipe() {
        // benzersiz id ekleme işlemi
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

    public byte[] getPhotoUrlBytes() { return photoUrlBytes; }
    public void setPhotoUrlBytes(byte[] photoUrlBytes) { this.photoUrlBytes = photoUrlBytes; }


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
                ", photoUrlBytes=" + photoUrlBytes +
                '}';
    }

}
