package com.superbigbang.meetmap;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class GetNaviaddressesOnMap {
    public Response gettednaviadresses;

    public void getnaviaddressedonmap(Context context, double lt_lat, double lt_lng, double rb_lat, double rb_lng) {
        //https://staging-api.naviaddress.com/api/v1.5/map/?zoom=15&lt_lat=55.7342569&lt_lng=37.643011&rb_lat=55.7318556&rb_lng=37.6483870&limit=100&address_type=free,premium - Наилучший для нас запрос
        String URLgetnaviaddresses = "https://staging-api.naviaddress.com/api/v1.5/map/?zoom=15&limit=100&address_type=free,premium"
                + "&lt_lat=" + lt_lat + "&lt_lng=" + lt_lng + "&rb_lat=" + rb_lat + "&rb_lng=" + rb_lng;

        RequestQueue requestPOSTQueue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                URLgetnaviaddresses,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        gettednaviadresses = new Gson().fromJson(response, Response.class);
                    }
                }
                ,
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        );
        requestPOSTQueue.add(jsonObjectRequest);
    }

    public void testgettedObjects() {
        Log.e("Result", gettednaviadresses.toString()); //проверка
    }
}

class Response {     //Приходит массив result "result":[{"address_type":"premium","category":{"id":2,"name":"Hotel"},"container":"7495","id":115349,"naviaddress":"495","point":{"lat":55.73350327976226,"lng":37.64376577728399},"zoom_level":12}...]
    public Result result[];

    @Override
    public String toString() { //для проверки
        if (result != null) {
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                out.append("container: ");
                out.append(result[i].container);
                out.append(", naviaddress: ");
                out.append(result[i].naviaddress);
                out.append("\n");
                out.append("lat: ");
                out.append(result[i].point.lat);
                out.append(", lng: ");
                out.append(result[i].point.lng);
                out.append("\n");
            }
            return out.toString();
        } else {
            Log.e("Error", "result is null =(");
            return super.toString();
        }
    }

    class Result { //расфасовываем result {"address_type":"premium","category":{"id":2,"name":"Hotel"},"container":"7495","id":115349,"naviaddress":"495","point":{"lat":55.73350327976226,"lng":37.64376577728399},"zoom_level":12}
        String container;
        String naviaddress;
        Point point;

        class Point { //расфасовываем point "point":{"lat":55.73350327976226,"lng":37.64376577728399}
            double lat;
            double lng;
        }
    }
}
