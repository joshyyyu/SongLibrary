package app;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

//Joshua Yu & Jie Ouyang

public class SongLib extends Application {

	private static Stage primaryStage;
	private static ObservableList<Song> songInfo = FXCollections.observableArrayList();

	@Override
	public void start(Stage primaryStage) {
		SongLib.primaryStage = primaryStage;
		SongLib.primaryStage.setTitle("Song Library");
		File file = getSongFilePath();
		if (file != null && file.length() != 0) {
			loadSongData(file);

		}
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/LibraryLayout.fxml"));
			AnchorPane LibraryLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(LibraryLayout);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static ObservableList<Song> getSongInfo() {
		return songInfo;
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void saveSongData(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(SongListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			SongListWrapper wrapper = new SongListWrapper();
			if (songInfo.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");

				alert.setHeaderText("File does not contain any songs");
				alert.setContentText("File cannot be saved because there is no songs");

				alert.showAndWait();
			} else {
				wrapper.setSongs(songInfo);

				m.marshal(wrapper, file);

				setSongFilePath(file);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	public static void loadSongData(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(SongListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			SongListWrapper wrapper = (SongListWrapper) um.unmarshal(file);

			songInfo.clear();
			songInfo.addAll(wrapper.getSongs());

			setSongFilePath(file);

		} catch (Exception e) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			if (e.getMessage() == null) {
				alert.setHeaderText("File does not contain any data");
			} else {
				alert.setHeaderText("Could not load data");
			}
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	public static void setSongFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(SongLib.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());

			primaryStage.setTitle("Song Library - " + file.getName());
		} else {
			prefs.remove("filePath");

			primaryStage.setTitle("Song Library");
		}
	}

	public File getSongFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(SongLib.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

}
