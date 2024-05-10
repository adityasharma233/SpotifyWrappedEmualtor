package com.example.cs2340_project2.TopItemsBackend;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SpotifyWrapped {

    private List<SpotifyResponse.Artist> topArtists;

    private String GeminiKey = "AIzaSyDc9Y8bavHdl2Vzpz6eXGAThV9G5gpMc-0";

    private List<String> adjectives;

    private List<String> descriptions;

    public SpotifyWrapped(List<SpotifyResponse.Artist> topArtists) {
        this.topArtists = topArtists;
    }

    public List<String> getTopGenres() {
        Map<String, Integer> genreCountMap = new HashMap<>();

        // Count occurrences of each genre
        for (SpotifyResponse.Artist artist : topArtists) {
            for (String genre : artist.getGenres()) {
                genreCountMap.put(genre, genreCountMap.getOrDefault(genre, 0) + 1);
            }
        }

        // Sort genres by count in descending order
        List<String> topGenres = genreCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Return the top 3 genres
        return topGenres.subList(0, Math.min(topGenres.size(), 3));
    }

    public List<SpotifyResponse.Artist> getTopArtists() {
        return topArtists.subList(0, Math.min(topArtists.size(), 3));
    }

    public void gptDescription(List<SpotifyResponse.Artist> topArtists, List<String> topGenres) {

        String artists = formatList2(topArtists);
        String genres = formatList(topGenres);

        // Use a model that's applicable for your use case (see "Implement basic use cases" below)
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-pro",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                /* apiKey */ GeminiKey);

        // Use the GenerativeModelFutures Java compatibility layer which offers
        // support for ListenableFuture and Publisher APIs
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Give me a list of 5 characteristics to describe how someone acts/thinks/dresses based on their top artists and genres. Artists: " + artists + "Genres: " + genres + ".")
                .build();

        Executor executor = Executors.newSingleThreadExecutor();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                adjectives = new ArrayList<>();
                descriptions = new ArrayList<>();
                String resultText = result.getText();
                System.out.println(resultText);
                assert resultText != null;
                String[] lines = resultText.split("\n");
                for (String line : lines) {
                    int startTagIndex = line.indexOf("**");
                    int endTagIndex = line.indexOf(":**");
                    if (startTagIndex != -1 && endTagIndex != -1) {
                        String word = line.substring(startTagIndex + 2, endTagIndex);
                        String sentence = line.substring(endTagIndex + 3).trim();
                        if (!word.equals("Characteristics") && !word.equals("5 Characteristics")) {
                            adjectives.add(word);
                        }
                        descriptions.add(sentence);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, executor);
    }

    private String formatList(List<String> list) {
        StringBuilder formattedList = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            formattedList.append(list.get(i));
            if (i < list.size() - 1) {
                formattedList.append(", ");
            }
        }
        formattedList.append("]");
        return formattedList.toString();
    }

    private String formatList2(List<SpotifyResponse.Artist> list) {
        StringBuilder formattedList = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            formattedList.append(list.get(i).getName());
            if (i < list.size() - 1) {
                formattedList.append(", ");
            }
        }
        formattedList.append("]");
        return formattedList.toString();
    }

    public List<String> getAdjectives() {
        return adjectives;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }
}
