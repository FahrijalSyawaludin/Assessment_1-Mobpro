package org.d3if3040.logindanqrcode;

import org.d3if3040.logindanqrcode.Model.UserData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String url = "https://raw.githubusercontent.com/FahrijalSyawaludin/API/main/UserData"; // Ganti dengan URL yang sesuai

        try {
            // Membuat koneksi HTTP
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            // Membaca respon dari koneksi HTTP
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parsing data JSON
            JSONArray jsonArray = new JSONArray(response.toString());
            List<UserData> userDataList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String username = jsonObject.getString("username");
                String password = jsonObject.getString("password");

                UserData userData = new UserData(id, username, password);
                userDataList.add(userData);
            }

            // Menampilkan data kepada pengguna
            for (UserData userData : userDataList) {
                System.out.println("ID: " + userData.getId());
                System.out.println("Username: " + userData.getUsername());
                System.out.println("Password: " + userData.getPassword());
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
