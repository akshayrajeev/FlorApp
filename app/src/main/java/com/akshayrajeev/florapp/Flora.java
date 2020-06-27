package com.akshayrajeev.florapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Flora implements Parcelable {
    private int id;
    private boolean completeData;
    private String commonName;
    private String scientificName;
    private String familyCommonName;
    private String familyScientificName;
    private String link;
    private String flowerColour;
    private HashMap<String,String> seedFruit;
    private HashMap<String,String> foliage;
    private HashMap<String,String> growth;
    private ArrayList<String> images;
    private DecimalFormat decimalFormat = new DecimalFormat("#.###");

    protected Flora() {
    }

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
        this.commonName = dataHandler(commonName);
    }

    void setScientificName(String scientificName) {
        this.scientificName = dataHandler(scientificName);
    }

    void setFamilyCommonName(String familyCommonName) {
        this.familyCommonName = dataHandler(familyCommonName);
    }

    void setFamilyScientificName(String familyScientificName) {
        this.familyScientificName = dataHandler(familyScientificName);
    }

    void setFlowerColour(String flowerColour) {
        this.flowerColour = dataHandler(flowerColour);
    }

    void setFoliage(String color, String porositySummer, String porosityWinter, String texture) {
        foliage = new HashMap<>();
        foliage.put("color", dataHandler(color));
        foliage.put("porositySummer", dataHandler(porositySummer));
        foliage.put("porosityWinter", dataHandler(porosityWinter));
        foliage.put("texture", dataHandler(texture));
    }

    void setSeedFruit(String color, String seedPeriodBegin, String seedPeriodEnd, String bloomPeriod, String commercialAvailability, String seedlingVigor, Double seedsPerPound) {
        seedFruit = new HashMap<>();
        seedFruit.put("color", dataHandler(color));
        seedFruit.put("seedPeriodBegin", dataHandler(seedPeriodBegin));
        seedFruit.put("seedPeriodEnd", dataHandler(seedPeriodEnd));
        seedFruit.put("bloomPeriod", dataHandler(bloomPeriod));
        seedFruit.put("commercialAvailability", dataHandler(commercialAvailability));
        seedFruit.put("seedlingVigor", dataHandler(seedlingVigor));
        seedFruit.put("seedsPerPound", dataHandler(seedsPerPound));
    }

    void setGrowth(String caco3Tolerance, String droughtTolerance, String fireTolerance, String maxPh, String minPh, JSONObject maxPlantingDensityObject, JSONObject minPlantingDensityObject, JSONObject maxPrecipitationObject, JSONObject minPrecipitationObject, String fertilityRequirement, String moistureUse, JSONObject matureHeight) {
        growth = new HashMap<>();
        growth.put("caco3Tolerance", dataHandler(caco3Tolerance));
        growth.put("droughtTolerance", dataHandler(droughtTolerance));
        growth.put("fireTolerance", dataHandler(fireTolerance));
        growth.put("maxPh", dataHandler(maxPh));
        growth.put("minPh", dataHandler(minPh));
        growth.put("maxPlantingDensityAcre", dataHandler(maxPlantingDensityObject.optDouble("acre",0)));
        growth.put("maxPlantingDensitySqm", dataHandler(maxPlantingDensityObject.optDouble("sqm",0)));
        growth.put("minPlantingDensityAcre", dataHandler(minPlantingDensityObject.optDouble("acre",0)));
        growth.put("minPlantingDensitySqm", dataHandler(minPlantingDensityObject.optDouble("sqm",0)));
        growth.put("maxPrecipitationCm", dataHandler(maxPrecipitationObject.optDouble("cm",0)));
        growth.put("maxPrecipitationInch", dataHandler(maxPrecipitationObject.optDouble("inches",0)));
        growth.put("minPrecipitationCm", dataHandler(minPrecipitationObject.optDouble("cm",0)));
        growth.put("minPrecipitationInch", dataHandler(minPrecipitationObject.optDouble("inches",0)));
        growth.put("fertilityRequirement", dataHandler(fertilityRequirement));
        growth.put("moistureUse", dataHandler(moistureUse));
        growth.put("matureHeightCm", dataHandler(matureHeight.optDouble("cm",0)));
        growth.put("matureHeightFt", dataHandler(matureHeight.optDouble("ft",0)));
    }

    void setImages(JSONArray images) {
        this.images = new ArrayList<>();
        for(int i=0; i<images.length(); i++) {
            try {
                JSONObject jsonObject = images.getJSONObject(i);
                this.images.add(jsonObject.getString("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    String getLink() {
        return link;
    }

    String getCommonName() {
        return commonName;
    }

    String getScientificName() {
        return scientificName;
    }

    String getFamilyCommonName() {
        return familyCommonName;
    }

    String getFamilyScientificName() {
        return familyScientificName;
    }

    String getFlowerColour() {
        return flowerColour;
    }

    HashMap<String,String> getFoliage() {
        return foliage;
    }

    HashMap<String,String> getSeedFruit() {
        return seedFruit;
    }

    HashMap<String,String> getGrowth() {
        return growth;
    }

    ArrayList<String> getImages() {
        return images;
    }

    private String dataHandler(String data) {
        if(data.equals("") || data.equals("null")) {
            return "No Data";
        }
        return data;
    }

    private String dataHandler(Double data) {
        if(data == 0) {
            return "No Data";
        }
        return decimalFormat.format(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (completeData ? 1 : 0));
        dest.writeString(commonName);
        dest.writeString(scientificName);
        dest.writeString(link);
    }

    protected Flora(Parcel in) {
        id = in.readInt();
        completeData = in.readByte() != 0;
        commonName = in.readString();
        scientificName = in.readString();
        link = in.readString();
    }

    public static final Creator<Flora> CREATOR = new Creator<Flora>() {
        @Override
        public Flora createFromParcel(Parcel in) {
            return new Flora(in);
        }

        @Override
        public Flora[] newArray(int size) {
            return new Flora[size];
        }
    };
}
