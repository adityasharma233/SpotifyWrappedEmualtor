package com.example.cs2340_project2.TopItemsBackend;

import com.example.cs2340_project2.TopItemsBackend.SpotifyResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseJSON {

    private JSONObject response;
    private SpotifyResponse spotifyResponse;

    private SpotifyResponse spotifyResponseTracks;

    public ParseJSON(JSONObject response) {
        this.response = response;
        parseResponse();
    }

    public ParseJSON(JSONObject response, SpotifyResponse passedResponse) {
        this.response = response;
        parseResponseTracks(passedResponse);
    }

    public List<SpotifyResponse.Artist> getList() {
        return spotifyResponse.getItems();
    }

    public List<SpotifyResponse.Track> getTrackList() { return spotifyResponseTracks.getTrackItems(); }

    private void parseResponse() {
        try {
            JSONArray itemsArray = response.getJSONArray("items");

            spotifyResponse = new SpotifyResponse();
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);
                // Parse each item and add it to your SpotifyResponse object
                String artistName = itemObject.getString("name");
                String artistId = itemObject.getString("id");
                int popularity = itemObject.getInt("popularity");

                JSONArray genresArray = itemObject.getJSONArray("genres");
                List<String> genres = new ArrayList<>();
                for (int j = 0; j < genresArray.length(); j++) {
                    genres.add(genresArray.getString(j));
                }

                JSONObject externalUrlsObject = itemObject.getJSONObject("external_urls");
                String spotifyUrl = externalUrlsObject.getString("spotify");

                JSONArray imagesArray = itemObject.getJSONArray("images");
                List<SpotifyResponse.Image> images = new ArrayList<>();
                for(int k = 0; k < imagesArray.length(); k++) {
                    JSONObject imagesObject = imagesArray.getJSONObject(k);
                    int imageHeight = imagesObject.getInt("height");
                    String imageURL = imagesObject.getString("url");
                    int imageWidth = imagesObject.getInt("width");
                    SpotifyResponse.Image image = new SpotifyResponse.Image(imageHeight, imageURL, imageWidth);
                    images.add(image);
                }

                JSONObject followersObject = itemObject.getJSONObject("followers");
                SpotifyResponse.Followers followers = new SpotifyResponse.Followers(followersObject.getString("href"), followersObject.getInt("total"));

                String type = itemObject.getString("type");

                SpotifyResponse.Artist artist = new SpotifyResponse.Artist(artistName, artistId, popularity, genres, spotifyUrl, images, followers, type);
                spotifyResponse.addArtist(artist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseResponseTracks(SpotifyResponse passedResponse) {
        try {
            JSONArray itemsArray = response.getJSONArray("items");

            spotifyResponseTracks = new SpotifyResponse(0);

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);

                JSONObject albumObject = itemObject.getJSONObject("album");
                String albumID = albumObject.getString("id");
                String albumName = albumObject.getString("name");
                String releaseDate = albumObject.getString("release_date");
                int totalTracks = albumObject.getInt("total_tracks");
                JSONArray imagesArray = albumObject.getJSONArray("images");
                List<SpotifyResponse.Image> imagesTrack = new ArrayList<>();
                for(int k = 0; k < imagesArray.length(); k++) {
                    JSONObject imagesObject = imagesArray.getJSONObject(k);
                    int imageHeight = imagesObject.getInt("height");
                    String imageURL = imagesObject.getString("url");
                    int imageWidth = imagesObject.getInt("width");
                    SpotifyResponse.Image image = new SpotifyResponse.Image(imageHeight, imageURL, imageWidth);
                    imagesTrack.add(image);
                }
                SpotifyResponse.Album album = new SpotifyResponse.Album(albumName,releaseDate,totalTracks,imagesTrack,albumID);
                int duration = itemObject.getInt("duration_ms");
                String trackID = itemObject.getString("id");
                String trackName = itemObject.getString("name");
                int popularity = itemObject.getInt("popularity");
                String previewURL = itemObject.getString("preview_url");
                //check ID cross references
                List<SpotifyResponse.Artist> allArtist = passedResponse.getItems();
                JSONArray artist = itemObject.getJSONArray("artists");
                List<String> artistIDs = new ArrayList<>();
                for (int j = 0; j < allArtist.size(); j++) {
                    artistIDs.add(allArtist.get(j).getId());
                }
                List<SpotifyResponse.Artist> artistList = new ArrayList<>();
                for (int k = 0; k < artist.length(); k++) {
                    if (artistIDs.contains(artist.get(k))) {
                        artistList.add(allArtist.get(k));
                    }
                }
                SpotifyResponse.Track track = new SpotifyResponse.Track(album, artistList, duration, trackID, trackName, popularity, previewURL);
                spotifyResponseTracks.addTrack(track);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public SpotifyResponse getSpotifyResponse() {
        return spotifyResponse;
    }
}
