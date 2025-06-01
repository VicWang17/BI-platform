package com.yupi.springbootinit.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupi.springbootinit.config.DeepSeekConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AiManager {
    @Resource
    private DeepSeekConfig deepSeekConfig;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String doChat(String message) {
        try {
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

            URL url = new URL(deepSeekConfig.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + deepSeekConfig.getKey());
            connection.setDoOutput(true);

            String payload = "{\n" +
                    "    \"model\": \"deepseek-chat\",\n" +
                    "    \"messages\": [\n" +
                    "        {\n" +
                    "            \"role\": \"system\",\n" +
                    "            \"content\": \"" + system + "\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"role\": \"user\",\n" +
                    "            \"content\": \"" + message + "\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"temperature\": 0.7\n" +
                    "}";

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

            // 解析JSON响应
            JsonNode jsonNode = objectMapper.readTree(response.toString());
            return jsonNode.get("choices").get(0).get("message").get("content").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "AI manager ERROR: " + e.getMessage();
        }
    }
}