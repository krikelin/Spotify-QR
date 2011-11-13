package com.krikelin.spotify.qr;

public class Album {
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	private String uri;
	public String getArtist() {
		return artist;
	}
	private String artist;
	public void setArtist(String artist) {
		this.artist = artist;
	}
	

}
