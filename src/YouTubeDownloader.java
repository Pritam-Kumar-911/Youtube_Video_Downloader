import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;

public class YouTubeDownloader extends Application {

    private TextArea outputArea;
    private ComboBox<String> formatComboBox;
    private File selectedFolder;
    private ProgressBar progressBar;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🎬 YouTube Downloader - JavaFX Edition");

        Label urlLabel = new Label("🎥 Enter YouTube URL:");
        TextField urlField = new TextField();
        urlField.setPromptText("https://www.youtube.com/watch?v=...");
        urlField.setPrefWidth(450);

        // Format options
        formatComboBox = new ComboBox<>();
        formatComboBox.getItems().addAll("Video (MP4)", "Audio (MP3)");
        formatComboBox.setValue("Video (MP4)");

        // Folder chooser
        Button chooseFolderBtn = new Button("Choose Folder");
        Label folderLabel = new Label("No folder selected");

        chooseFolderBtn.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Output Folder");
            File folder = chooser.showDialog(primaryStage);
            if (folder != null) {
                selectedFolder = folder;
                folderLabel.setText(folder.getAbsolutePath());
            }
        });

        // Download button
        Button downloadBtn = new Button("Download");

        // Output area
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(200);
        outputArea.setWrapText(true);

        // Progress bar
        progressBar = new ProgressBar();
        progressBar.setPrefWidth(500);
        progressBar.setProgress(0);

        // Action for download
        downloadBtn.setOnAction(e -> {
            String url = urlField.getText().trim();
            if (url.isEmpty()) {
                outputArea.setText("⚠️ Please enter a YouTube URL.");
                return;
            }
            if (selectedFolder == null) {
                outputArea.setText("⚠️ Please choose a folder to save the file.");
                return;
            }
            downloadYouTube(url);
        });

        // Layout
        VBox layout = new VBox(12);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
                urlLabel, urlField,
                new HBox(10, new Label("Format:"), formatComboBox),
                new HBox(10, chooseFolderBtn, folderLabel),
                downloadBtn,
                progressBar,
                outputArea
        );

        Scene scene = new Scene(layout, 600, 450);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void downloadYouTube(String url) {
        outputArea.clear();
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

        new Thread(() -> {
            try {
                String format = formatComboBox.getValue();
                String outputTemplate = selectedFolder.getAbsolutePath() + File.separator + "%(title)s.%(ext)s";

                // Build command
                ProcessBuilder builder;
                if (format.contains("MP3")) {
                    builder = new ProcessBuilder("yt-dlp", "-x", "--audio-format", "mp3", "-o", outputTemplate, url);
                } else {
                    builder = new ProcessBuilder("yt-dlp", "-f", "mp4", "-o", outputTemplate, url);
                }

                builder.redirectErrorStream(true);
                Process process = builder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    String finalLine = line;
                    javafx.application.Platform.runLater(() -> outputArea.appendText(finalLine + "\n"));
                }

                int exitCode = process.waitFor();
                javafx.application.Platform.runLater(() -> {
                    progressBar.setProgress(0);
                    if (exitCode == 0) {
                        outputArea.appendText("✅ Download completed successfully.\n");
                    } else {
                        outputArea.appendText("❌ Download failed. Please check your URL or yt-dlp setup.\n");
                    }
                });

            } catch (IOException | InterruptedException e) {
                javafx.application.Platform.runLater(() -> {
                    progressBar.setProgress(0);
                    outputArea.appendText("❌ Error: " + e.getMessage() + "\n");
                });
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
