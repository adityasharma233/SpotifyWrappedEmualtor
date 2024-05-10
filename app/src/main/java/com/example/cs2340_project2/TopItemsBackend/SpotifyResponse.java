package com.example.cs2340_project2.TopItemsBackend;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SpotifyResponse {

    private List<Artist> items;
    private List<Track> trackItems;


    public SpotifyResponse() {
        items = new ArrayList<>();
    }

    public SpotifyResponse(int num) {
        trackItems = new ArrayList<>();
    }



    public List<Artist> getItems() {
        return items;
    }

    public List<Track> getTrackItems() {
        return trackItems;
    }

    public void addArtist(Artist artist) {
        items.add(artist);
    }

    public void addTrack(Track track) { trackItems.add(track); }

    public static class Track {
        private Album album;

        private List<Artist> artists;

        private int duration;

        private String trackID;

        private String trackName;

        private int popularity;

        private String previewURL;

        public Track(Album album, List<Artist> artists, int duration, String trackID, String trackName, int popularity, String previewURL) {
            this.album = album;
            this.artists = artists;
            this.duration = duration;
            this.trackID = trackID;
            this.trackName = trackName;
            this.popularity = popularity;
            this.previewURL = previewURL;
        }

        public List<Artist> getArtists() {
            return artists;
        }

        public Album getAlbum() {
            return album;
        }

        public int getDuration() {
            return duration;
        }

        public String getTrackID() {
            return trackID;
        }

        public int getPopularity() {
            return popularity;
        }

        public String getPreviewURL() {
            return previewURL;
        }

        public String getTrackName() {
            return trackName;
        }
    }

    public static class Album {
        private String albumName;

        private String releaseDate;

        private int totalTracks;

        private List<Image> images;

        private String albumID;

        public Album(String albumName, String releaseDate, int totalTracks, List<Image> images, String albumID) {
            this.albumName = albumName;
            this.releaseDate = releaseDate;
            this.totalTracks = totalTracks;
            this.images = images;
            this.albumID = albumID;
        }

        public int getTotalTracks() {
            return totalTracks;
        }

        public String getAlbumID() {
            return albumID;
        }

        public List<Image> getImages() {
            return images;
        }

        public String getAlbumName() {
            return albumName;
        }

        public String getReleaseDate() {
            return releaseDate;
        }
    }

    public static class Artist {
        @SerializedName("external_urls")
        private ExternalUrls externalUrls;
        private Followers followers;
        private List<String> genres;
        private String href;
        private String id;
        private List<Image> images;
        private String name;
        private int popularity;
        private String type;
        private String uri;

        public Artist(String artistName, String artistId, int popularity, List<String> genres, String spotifyUrl, List<Image> images, Followers followers, String type) {
            name = artistName;
            id = artistId;
            this.popularity = popularity;
            this.genres = genres;
            uri = spotifyUrl;
            this.images = images;
            this.followers = followers;
            this.type = type;

        }

        public ExternalUrls getExternalUrls() {
            return externalUrls;
        }

        public Followers getFollowers() {
            return followers;
        }

        public List<String> getGenres() {
            return genres;
        }

        public String getHref() {
            return href;
        }

        public String getId() {
            return id;
        }

        public List<Image> getImages() {
            return images;
        }

        public String getName() {
            return name;
        }

        public int getPopularity() {
            return popularity;
        }

        public String getType() {
            return type;
        }

        public String getUri() {
            return uri;
        }
    }

    public static class ExternalUrls {
        private String spotify;

        public String getSpotify() {
            return spotify;
        }
    }

    public static class Followers {
        private String href;
        private int total;

        public Followers(String href, int total) {
            this.href = href;
            this.total = total;
        }

        public String getHref() {
            return href;
        }

        public int getTotal() {
            return total;
        }
    }

    public static class Image {
        private int height;
        private String url;
        private int width;

        public Image(int height, String url, int width) {
            this.height = height;
            this.url = url;
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }
    }
}
