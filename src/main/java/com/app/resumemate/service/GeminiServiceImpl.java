package com.app.resumemate.service;

import com.app.resumemate.model.ResumeRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class GeminiServiceImpl implements GeminiService {

    private static final String API_KEY = "AIzaSyBSoBHH_AixfFJ9EYvb2zzmKVsgjhuMf-s"; 
    private static final String GEMINI_URL = 
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key=" + API_KEY;

    @Override
    public String analyzeResume(ResumeRequest request) {
        try {
           
        	String prompt = String.format(
        		    "You are an expert resume analyzer. Compare the following resume and job description:\n\n" +
        		    "Resume:\n%s\n\nJob Description:\n%s\n\n" +
        		    "Return the result in clean HTML format. Use these exact div IDs:\n" +
        		    "<div id='score'>  [score in %%]</div>\n" +
        		    "<div id='matched'> <ul><li>Skill 1</li><li>Skill 2</li></ul></div>\n" +
        		    "<div id='missing'> <ul><li>Missing 1</li></ul></div>\n" +
        		    "<div id='suggestions'> <ul><li>Improve XYZ</li></ul></div>\n" +
        		    "Include all sections even if they are empty. Avoid adding extra text outside these divs.",
        		    request.getResumeText(), request.getJobDescription()
        		

            );

            // Create the HTTP client with specified timeouts
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Construct the JSON request body
            MediaType mediaType = MediaType.parse("application/json");
            String json = String.format("{ \"contents\": [{ \"parts\": [{ \"text\": \"%s\" }] }] }", prompt);

            RequestBody body = RequestBody.create(json, mediaType);

            // Build the request to the Gemini API
            Request httpRequest = new Request.Builder()
                    .url(GEMINI_URL)
                    .post(body)
                    .build();

            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    // Log error with detailed response body
                    String responseBody = response.body().string();
                    return "Gemini API Error: " + response.code() + " - " + responseBody;
                }

                // Parse the successful response from the Gemini API
                String responseBody = response.body().string();
                return parseGeminiResponse(responseBody);
            }
        } catch (IOException e) {
            // Handle specific IO exceptions
            e.printStackTrace();
            return "Request failed. Please try again later. Error: " + e.getMessage();
        } catch (Exception e) {
            // Catch any other exceptions
            e.printStackTrace();
            return "Error during Gemini analysis: " + e.getMessage();
        }
    }

    // Helper method to parse the Gemini API response and extract personalized advice
    private String parseGeminiResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode candidateNode = rootNode.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text");

            // 🔍 Debug: print full parsed text
            String fullResponse = candidateNode.asText();
            

            return fullResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing Gemini API response: " + e.getMessage();
        }
    }

}

