package app;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//Joshua Yu & Jie Ouyang

@XmlRootElement(name = "songs")
public class SongListWrapper {

	private List<Song> songs;

	@XmlElement(name = "song")
	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

}