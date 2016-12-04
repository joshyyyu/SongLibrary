package view;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import app.Song;
import app.SongLib;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

//Joshua Yu & Jie Ouyang

public class LibraryController {
	@FXML
	private ListView<Song> songListView;
	@FXML
	private Label songNameLabel;
	@FXML
	private Label artistNameLabel;
	@FXML
	private Label albumNameLabel;
	@FXML
	private Label yearLabel;
	@FXML
	private TextField songNameText;
	@FXML
	private TextField artistNameText;
	@FXML
	private TextField albumNameText;
	@FXML
	private DatePicker yearDate;

	private static ObservableList<Song> songInfo = FXCollections.observableArrayList();

	@FXML
	private void addButton() {
		if (isInputValid()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation");
			alert.setHeaderText("Please Confirm");
			alert.setContentText("Are you sure you want to add new song?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {

				Song tempSong = new Song();
				tempSong.setSongName(songNameText.getText());
				tempSong.setArtistName(artistNameText.getText());
				tempSong.setAlbumName(albumNameText.getText());
				if (yearDate.getValue() != null) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
					LocalDate date = yearDate.getValue();
					tempSong.setYear(formatter.format(date));
				}
				songInfo = SongLib.getSongInfo();
				if (songCheck(songInfo, tempSong)) {
					Alert alertError = new Alert(AlertType.ERROR);
					alertError.setTitle("Invalid Fields");
					alertError.setHeaderText("Please correct invalid fields");
					alertError.setContentText("An existing song with the same artist exists");

					alertError.showAndWait();
				} else {
					songInfo.add(tempSong);
					sort(songInfo);
					int i = songInfo.indexOf(tempSong);
					
					if (!songInfo.isEmpty())
						showSongDetail(songInfo.get(i));
					
					songListView.getSelectionModel().select(i);
					resetText();
				}



			} else {
				alert.close();
			}
		}

	}

	private void sort(ObservableList<Song> songInfo) {
		Comparator<Song> compareSong = new Comparator<Song>() {
			@Override
			public int compare(Song s1, Song s2) {
				return s1.getSongName().compareTo(s2.getSongName());
			}
		};

		Collections.sort(songInfo, compareSong);
	}

	@FXML
	private void clearButton() {
		resetText();
	}

	private void resetText() {
		songNameText.setText("");
		artistNameText.setText("");
		albumNameText.setText("");
		yearDate.setValue(null);
	}

	private boolean songCheck(ObservableList<Song> songInfo, Song tempSong) {
		for (int i = 0; i < songInfo.size(); i++) {
			if (songInfo.get(i).getSongName().equals(tempSong.getSongName())
					&& songInfo.get(i).getArtistName().equals(tempSong.getArtistName())) {
				return true;
			}
		}
		return false;
	}

	private boolean editSongCheck(ObservableList<Song> songInfo, Song tempSong, int p) {

		for (int i = 0; i < songInfo.size(); i++) {
			if (i != p) {
				if (songInfo.get(i).getSongName().equals(tempSong.getSongName())
						&& songInfo.get(i).getArtistName().equals(tempSong.getArtistName())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isInputValid() {
		String errorMessage = "";
		if (songNameText.getText().isEmpty()) {
			errorMessage += "No song name\n";
		}
		if (artistNameText.getText().isEmpty()) {
			errorMessage += "No artist name!\n";
		}
		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	@FXML
	private void editButton() {
		Song editsong = songListView.getSelectionModel().getSelectedItem();
		if (editsong != null) {
			if (isInputValid()) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Please Confirm");
				alert.setContentText("Are you sure you want to edit song?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					Song tempSong = new Song();
					tempSong.setSongName(songNameText.getText());
					tempSong.setArtistName(artistNameText.getText());
					tempSong.setAlbumName(albumNameText.getText());
					if (yearDate.getValue() != null) {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
						LocalDate date = yearDate.getValue();
						tempSong.setYear(formatter.format(date));
					}
					songInfo = SongLib.getSongInfo();

					if (editSongCheck(songInfo, tempSong, songInfo.indexOf(editsong))) {
						Alert alertError = new Alert(AlertType.ERROR);
						alertError.setTitle("Invalid Fields");
						alertError.setHeaderText("Please correct invalid fields");
						alertError.setContentText("An existing song with the same artist exists");

						alertError.showAndWait();
					} else {
						songInfo.remove(editsong);
						songInfo.add(tempSong);
						sort(songInfo);
						int i = songInfo.indexOf(tempSong);
						
						if (!songInfo.isEmpty())
							showSongDetail(songInfo.get(i));
						
						songListView.getSelectionModel().select(i);
					}
				}
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Edit Song");
			alert.setHeaderText("No Song Selected");
			alert.setContentText("Please select a song");

			alert.showAndWait();
		}
	}

	@FXML
	private void deleteButton() {
		int selectedIndex = songListView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			songListView.getItems().remove(selectedIndex);
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Delete Song");
			alert.setHeaderText("No Song Selected");
			alert.setContentText("Please select a song");

			alert.showAndWait();
		}
	}

	@FXML
	private void openButton() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showOpenDialog(SongLib.getPrimaryStage());
		if (file != null) {
			SongLib.loadSongData(file);
		}
	}

	@FXML
	private void saveButton() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showSaveDialog(SongLib.getPrimaryStage());

		if (file != null) {
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			SongLib.saveSongData(file);
		}
	}

	@FXML
	private void initialize() {

		songListView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showSongDetail(newValue));

		songInfo = SongLib.getSongInfo();
		sort(songInfo);

		songListView.setItems(songInfo);
		songListView.setCellFactory(param -> new ListCell<Song>() {
			@Override
			protected void updateItem(Song item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null || item.getSongName() == null) {
					setText(null);
				} else {
					setText(item.getSongName());
				}
			}
		});

		if (!songInfo.isEmpty())
			showSongDetail(songInfo.get(0));
		songListView.getSelectionModel().select(0);
	}

	private void showSongDetail(Song song) {
		if (song != null) {
			songNameLabel.setText(song.getSongName());
			artistNameLabel.setText(song.getArtistName());
			albumNameLabel.setText(song.getAlbumName());
			yearLabel.setText(song.getYear());

			songNameText.setText(song.getSongName());
			artistNameText.setText(song.getArtistName());
			albumNameText.setText(song.getAlbumName());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

			if (song.getYear() != null && !song.getYear().isEmpty()) {
				LocalDate date = LocalDate.parse(song.getYear(), formatter);
				yearDate.setValue(date);
			} else {
				yearDate.setValue(null);
			}

		} else {
			songNameLabel.setText("");
			artistNameLabel.setText("");
			albumNameLabel.setText("");
			yearLabel.setText("");
		}
	}

}
