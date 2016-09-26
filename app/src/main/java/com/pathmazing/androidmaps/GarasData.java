package com.pathmazing.androidmaps;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GarasData {
    private Context context;

    public static final String DATA_PATH = "garas.json";

    public GarasData(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<PlaceModel> getAllGaras(String json) {
       ArrayList<PlaceModel> result = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray("results");
            int length = jsonArray.length();
            Log.d("Length",length+"");
            JSONObject jsonObjectTmp;
            JSONArray jsonObjectTmp1;

            Double lat;
            Double lng;
            String name;
            String imgaeURl;
            String photo;
            Double height = null;

            PlaceModel dataObject;

            for (int i = 0; i < length; i++) {
                jsonObjectTmp = jsonArray.getJSONObject(i).getJSONObject("geometry");
                lat=jsonObjectTmp.getJSONObject("location").getDouble("lat");
                lng=jsonObjectTmp.getJSONObject("location").getDouble("lng");
                name=jsonArray.getJSONObject(i).getString("name");
                imgaeURl=jsonArray.getJSONObject(i).getString("icon");
                dataObject = new PlaceModel();
                dataObject.setLat(lat);
                dataObject.setLng(lng);
                dataObject.setName(name);
                dataObject.setIcon(imgaeURl);
                result.add(dataObject);
            }

            lat = null;
            lng = null;
            name=null;
            imgaeURl=null;
            jsonObjectTmp = null;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    }