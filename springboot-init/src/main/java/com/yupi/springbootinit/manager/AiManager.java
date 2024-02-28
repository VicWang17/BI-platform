package com.yupi.springbootinit.manager;

import com.yupi.springbootinit.MainApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
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

    public static String getResult(String message) throws Exception {
        String accessToken = getAccessToken();
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + accessToken;



/*        systems.append("option = {\n")
                .append("  xAxis: {\n")
                .append("    type: 'category',\n")
                .append("    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']\n")
                .append("  },\n")
                .append("  yAxis: {\n")
                .append("    type: 'value'\n")
                .append("  },\n")
                .append("  series: [\n")
                .append("    {\n")
                .append("      data: [120, 200, 150, 80, 70, 110, 130],\n")
                .append("      type: 'bar'\n")
                .append("    }\n")
                .append("  ]\n")
                .append("};\n")
                .append("Analyze: ")
                .append("The provided data represents values for each day of the week, followed by numerical values." +
                        "The values for each day vary, indicating potential fluctuation in the measured metric throughout" +
                        "the week. The average value is approximately 118.57. Further analysis could involve calculating" +
                        "the minimum and maximum values, as well as identifying any specific patterns or trends in the data.");*/



        String system = "You are a data analysis expert.\n "+
                "I will provide you with the requirements and data. "+
                "Please output the Echarts code and the analyse result in the specified format below. " +
                "(in addition, do not output any unnecessary beginning, ending, or comments).\n" +
                "You only can speak English.\n"+
                "User: \n" +
                "Requirement: Please analyze the data." +
                "Data: \n" +
                "Mon, Tue, Wed, Thu, Fri, Sat, Sun,\n" +
                "120, 200, 150, 80, 70, 110, 130\n" +
                "You: \n" +
                "Echarts JSON Code:\n" +
                "{\n" +
                "  \"title\": { text: \"Data\", " +
                " left: \"center\" },\n" +
                "  \"xAxis\": {\n" +
                "    \"type\": \"category\",\n" +
                "    \"data\": [\"Mon\", \"Tue\", \"Wed\", \"Thu\", \"Fri\", \"Sat\", \"Sun\"]\n" +
                "  },\n" +
                "  \"yAxis\": {\n" +
                "    \"type\": \"value\"\n" +
                "  },\n" +
                "  \"series\": [\n" +
                "    {\n" +
                "      \"data\": [150, 230, 224, 218, 135, 147, 260],\n" +
                "      \"type\": \"line\"\n" +
                "    }\n" +
                "  ]\n" +
                "}" +
                "Analyze:\n" +
                "The provided data represents values for each day of the week, followed by numerical values. " +
                "The values for each day vary, indicating potential fluctuation in the measured metric throughout " +
                "the week. The average value is approximately 118.57. Further analysis could involve calculating " +
                "the minimum and maximum values, as well as identifying any specific patterns or trends in the data.";
        system = system.replaceAll("\n","  ");
        system = system.replaceAll("\"","'");
        String content = "User: " + message + " You: ";
        content = system + content;

        String payload = "{\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \"" + content + "\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        System.out.println(payload);

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

    public String doChat(String message){
        try {
            String result = getResult(message);
            String cleanedResult = result.split("\"result\":\"")[1].split("\",\"is_truncated\"")[0];
            System.out.println(cleanedResult);
            return cleanedResult;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "AI manager ERROR";
    }



    }