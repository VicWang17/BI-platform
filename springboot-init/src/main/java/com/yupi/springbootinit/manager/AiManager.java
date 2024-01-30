package com.yupi.springbootinit.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AiManager {
    private static final String API_KEY = "qc1jLQtoYGIsUkRkoZGhefFk";
    private static final String SECRET_KEY = "es5Eo5NqgknYOGKiMP41ajFHgTThwU98";
    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;

    public static String getAccessToken() throws Exception {
        URL url = new URL(ACCESS_TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write("".getBytes());
        outputStream.flush();
        outputStream.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString().replaceAll("\"", "").split("access_token:")[1].split(",")[0];
    }

    public static String getResult() throws Exception {
        String accessToken = getAccessToken();
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + accessToken;

        String system = "You only can speak English.";
        String content = "Who are you?";
        content = system + content;
        String payload = "{\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \"" + content + "\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(payload.getBytes());
        outputStream.flush();
        outputStream.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    public static void main(String[] args) {
        try {
            String result = getResult();
            String cleanedResult = result.split("\"result\":\"")[1].split("\",\"is_truncated\"")[0];
            System.out.println(cleanedResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}