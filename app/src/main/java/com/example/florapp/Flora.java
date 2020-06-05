package com.example.florapp;

import java.io.Serializable;
import java.util.HashMap;

public class Flora implements Serializable {
    int id;
    boolean completeData;
    String commonName;
    String scientificName;
    String familyCommonName;
    String link;
    HashMap<String,String> fruitSeed;
    HashMap<String,String> growth;

    void setId(int id) {
        this.id = id;
    }

    void setCompleteData(boolean completeData) {
        this.completeData = completeData;
    }

    void setLink(String link) {
        this.link = link;
    }

    void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    void setFamilyCommonName(String familyCommonName) {
        this.familyCommonName = familyCommonName;
    }

    String getStringData() {
        return id + "_" + commonName + "_" + scientificName + "_" + familyCommonName + "_" + link + "_" + completeData;
    }
}
