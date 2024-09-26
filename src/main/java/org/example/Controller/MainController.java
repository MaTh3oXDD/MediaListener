package org.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
public class MainController {
    @FXML
    private ImageView ImageSong;
    @FXML
    private Label LabelSong;
    @FXML
    private ProgressBar ProgressSong;
    @FXML
    private Slider SliderVolume;
    @FXML
    private Button ButtonPrevious;
    @FXML
    private Button ButtonPause;
    @FXML
    private Button ButtonNext;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private ArrayList<Integer> songs = new ArrayList<>();;
    private int SongNumber=-1;
    private ArrayList<String> songTitles = new ArrayList<>();
    private ArrayList<byte[]> songImages = new ArrayList<>();
    @FXML
    public void initialize()
    {
        getRandomSong();
        ButtonPrevious.setOnAction(event -> setPreviousSong());
        ButtonNext.setOnAction(event -> getRandomSong());
        ButtonPause.setOnAction(event -> {
            if (mediaPlayer != null) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                } else {
                    mediaPlayer.play();
                    isPlaying = true;
                }
            }
        });
        setupVolumeControl();
        setupProgressTracking();
        setupProgressBarClick();
    }
    public void getRandomSong() {
        String jdbcURL = "jdbc:mysql://localhost:3306/medialistener";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            String sql = "SELECT id, song_name, cover_image FROM music ORDER BY RAND() LIMIT 1";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                String songName = resultSet.getString("song_name");
                String id = resultSet.getString("id");
                byte[] coverImageBytes = resultSet.getBytes("cover_image");
                System.out.println("Losowa piosenka: " + songName);
                songs.add(Integer.parseInt(id));
                songTitles.add(songName);
                songImages.add(coverImageBytes);
                SongNumber++;
                String audioFilePath = "src/main/resources/music/" + id + ".mp3";
                File audioFile = new File(audioFilePath);

                if (audioFile.exists()) {
                    Media media = new Media(audioFile.toURI().toString());
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer = new MediaPlayer(media);
                    isPlaying = false;
                } else {
                    System.out.println("Plik audio nie został znaleziony: " + audioFilePath);
                }

                if (coverImageBytes != null) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(coverImageBytes);
                    Image coverImage = new Image(bis);
                    ImageSong.setImage(coverImage);
                }
                LabelSong.setText(songName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPreviousSong() {
        if (SongNumber > 0) {
            SongNumber--;
            String audioFilePath = "src/main/resources/music/" + songs.get(SongNumber) + ".mp3";
            File audioFile = new File(audioFilePath);

            if (audioFile.exists()) {
                Media media = new Media(audioFile.toURI().toString());
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                isPlaying = true;
                LabelSong.setText(songTitles.get(SongNumber));
                ByteArrayInputStream bis = new ByteArrayInputStream(songImages.get(SongNumber));
                Image coverImage = new Image(bis);
                ImageSong.setImage(coverImage);
            } else {
                System.out.println("Plik audio nie został znaleziony: " + audioFilePath);
            }
        } else {
            System.out.println("Brak wcześniejszej piosenki w historii.");
        }
    }

    public void setupVolumeControl() {
        SliderVolume.valueProperty().addListener((observable, oldValue, newValue) -> {
            adjustVolume(newValue.doubleValue());
        });
    }
    public void adjustVolume(double volumeValue) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volumeValue / 100);
        }
    }
    // Śledzenie postępu odtwarzania i aktualizacja ProgressBar
    public void setupProgressTracking() {
        mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
            updateProgressBar();
        });

        mediaPlayer.setOnReady(() -> {
            updateProgressBar();
        });
    }

    // Funkcja aktualizująca ProgressBar
    private void updateProgressBar() {
        if (mediaPlayer != null && mediaPlayer.getTotalDuration().toMillis() > 0) {
            double progress = mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis();
            ProgressSong.setProgress(progress);
        }
    }

    // Umożliwienie przewijania utworu przez kliknięcie na ProgressBar
    public void setupProgressBarClick() {
        ProgressSong.setOnMousePressed(event -> {
            if (mediaPlayer != null && mediaPlayer.getTotalDuration().toMillis() > 0) {
                double mouseX = event.getX();
                double progress = mouseX / ProgressSong.getWidth();
                mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(progress));
            }
        });
    }

}
