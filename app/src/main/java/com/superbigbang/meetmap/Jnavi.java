package com.superbigbang.meetmap;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Jnavi {
    public String token;
    public Map<String, String> out;

    public Map<String, String> getOut() {
        return this.out;
    }

    public void setOut(Map<String, String> out) {
        this.out = out;
    }

    public Jnavi(String token, Map<String, String> out) {
        this.token = token;
        this.out = out;
    }

    private String URLsessionslogin = "https://staging-api.naviaddress.com/api/v1.5/Sessions";
    private String URLcreateaddress = "https://staging-api.naviaddress.com/api/v1.5/addresses/";

    public void loginuser(Context context, String password, String type, String email) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("password", password);
        params.put("type", type);
        params.put("Email", email);
        RequestQueue requestPOSTQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URLsessionslogin,
                new JSONObject(params),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //   Log.e("Response", getDataFromGsonResponse(response));
                        getKEYFromGsonResponse(response);

                    }
                }
                ,
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                //    headers.put("Content-Type" ,"application/json");  -добавляется автоматически Valley lib.
                return headers;
            }
        };
        requestPOSTQueue.add(jsonObjectRequest);

    }

    public void createnaviaddress(Context context, String lat, String lng, String addresstype, String defaultlang, final String authtoken) {

        Map<String, String> params = new HashMap<String, String>();//lat: 55.761315757185166, lng: 37.65203475952149, address_type: "free", default_lang: "ru"
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("address_type", addresstype);
        params.put("default_lang", defaultlang);
        RequestQueue requestPOSTQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URLcreateaddress,
                new JSONObject(params),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getContainerAndNaviaddressFromGsonResponse(response);
                        Log.e("Response", response.toString());


                    }
                }
                ,
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                //    headers.put("Content-Type" ,"application/json");  -добавляется автоматически Valley lib.
                headers.put("auth-token", authtoken);
                return headers;
            }
        };
        requestPOSTQueue.add(jsonObjectRequest);
    }

    public void acceptnaviaddress(Context context, String URLcreateaddress, String container, String naviaddress, final String authtoken) {

        Map<String, String> params = new HashMap<String, String>();//lat: 55.761315757185166, lng: 37.65203475952149, address_type: "free", default_lang: "ru"
        params.put("container", container);
        params.put("naviaddress", naviaddress);
        RequestQueue requestPOSTQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URLcreateaddress,
                new JSONObject(params),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                    }
                }
                ,
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                //    headers.put("Content-Type" ,"application/json");  -добавляется автоматически Valley lib.
                headers.put("auth-token", authtoken);
                return headers;
            }
        };
        requestPOSTQueue.add(jsonObjectRequest);
    }

    public void getKEYFromGsonResponse(JSONObject response) {
        Gson gson = new Gson();
        GsonAccumulator responseto = gson.fromJson(response.toString(), GsonAccumulator.class);
        Map<String, String> map = new HashMap<String, String>();
        map.put("AUTH_TOKEN", responseto.token);
        setOut(map);
    }

    public void getContainerAndNaviaddressFromGsonResponse(JSONObject response) {
        Gson gson = new Gson();
        GsonResultAccumulator responseto = gson.fromJson(response.toString(), GsonResultAccumulator.class);
        Map<String, String> map = new HashMap<String, String>();
        map.put("CONTAINER", responseto.result.container);
        map.put("NAVIADDRESS", responseto.result.naviaddress);
        setOut(map);
    }


    class GsonAccumulator {
        public String token;


    }

    class GsonResultAccumulator {
        public ResultGS result;

        @Override
        public String toString() {
            return result.toString();
        }
    }

    class ResultGS {
        public String container;
        public String naviaddress;
    }
/*
   class Result {
        public String description;
        public String container;
        public String naviaddress;
        public List<CoverImage> cover;
        public String token;
//public String[] langs;
        public String toString() {
      StringBuilder stringBuilder = new StringBuilder("Address:"); //разницы ни по скорости, ни по памяти незаметил, (по замеренным цифрам всё практически одинаково)
         stringBuilder.append("[");
         stringBuilder.append(container);
         stringBuilder.append("]");
         stringBuilder.append(naviaddress);
         stringBuilder.append(" - ");
         stringBuilder.append(description);
         stringBuilder.append("\n");
         stringBuilder.append("Images:\n");
            for (int i = 0; i < cover.size(); i++) {
                stringBuilder.append(Integer.toString(i));
                stringBuilder.append(": ");
                stringBuilder.append(cover.get(i).toString());
                stringBuilder.append("\n");
            }
            stringBuilder.append(token);
            stringBuilder.append("\n");
            return stringBuilder.toString();
        }
    }

    class CoverImage {
        public String image;

        public String toString() {
            return image;
        }
    }
}
*/
}