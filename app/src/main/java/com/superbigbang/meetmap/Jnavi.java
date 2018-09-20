package com.superbigbang.meetmap;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Jnavi extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {
        if (strings[0].equals("GET")) {
            try {
// Подготавливаем запрос
                URL url = new URL(strings[1]);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                // Устанавливаем параметры запроса
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "application/json");

                // Получаем ответ в буфер
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuffer stringBuffer = new StringBuffer();

                // Добавляем данные из буфера в строку
                while ((inputLine = in.readLine()) != null) {
                    stringBuffer.append(inputLine);
                }
                in.close();

                // Ответ получен
                String jsonString = stringBuffer.toString();

                // Печать результатов
                Gson gson = new Gson();
                Response response = gson.fromJson(jsonString, Response.class);

                //System.out.println(response.toString()); - это отформатированный вывод
                //  System.out.println(jsonString);  - это не форматированная строка.

                return response.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (strings[0].equals("PUT")) {
        }
        if (strings[0].equals("DELETE")) {
        }

        return null;
    }


    class Response {
        public Result result;

        public String toString() {
            return result.toString();// + " - " + description;
        }
    }

    class Result {
        public String description;
        public String container;
        public String naviaddress;
        public List<CoverImage> cover;

        public String toString() {
            String s = "Address:";
            s += "[" + container + "]" + naviaddress + " - " + description + "\n";
            s += "Images:\n";
            for (int i = 0; i < cover.size(); i++) {
                s += Integer.toString(i) + ": " + cover.get(i).toString() + "\n";
            }
            return s;
        }
    }

    class CoverImage {
        public String image;

        public String toString() {
            return image;
        }
    }
}
