package app;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Joshua Yu & Jie Ouyang

public class Song {

	private StringProperty songName;
	private StringProperty artistName;
	private StringProperty albumName;
	private StringProperty year;
	
    public Song() {
        this(null);
    }
	
	public Song(String songName){
		this.songName = new SimpleStringProperty(songName);
		this.artistName = new SimpleStringProperty();
		this.albumName = new SimpleStringProperty();
		this.year = new SimpleStringProperty();
		
	}
	
	public String getSongName(){
		return songName.get();
	}
	
	public void setSongName(String songName){
		this.songName.set(songName);
	}
    public StringProperty songNameProperty() {
        return songName;
    }
    
	public String getArtistName(){
		return artistName.get();
	}
	
	public void setArtistName(String artistName){
		this.artistName.set(artistName);
	}
	
	public String getAlbumName(){
		return albumName.get();
	}
	
	public void setAlbumName(String albumName){
		this.albumName.set(albumName);
	}
	
	public String getYear(){
		return year.get();
	}
	
	public void setYear(String year){
		this.year.set(year);
	}
}
