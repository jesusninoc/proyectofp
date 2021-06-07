package com.diegoflores.onlineacademy.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Helper {

    public static JsonObject obtieneJson(String apiUrl) {
        JsonObject json;
        try {
            //creamos una URL donde esta nuestro webservice
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //indicamos por que verbo HTML ejecutaremos la solicitud
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                //si la respuesta del servidor es distinta al codigo 200 lanzaremos una Exception
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            //creamos un StringBuilder para almacenar la respuesta del web service
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = br.read()) != -1) {
                sb.append((char) cp);
            }
            //en la cadena output almacenamos toda la respuesta del servidor
            String output = sb.toString();
            //convertimos la cadena a JSON a traves de la libreria GSON
            json = new Gson().fromJson(output, JsonObject.class);
            conn.disconnect();
            //retornamos como Json

            return json;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
