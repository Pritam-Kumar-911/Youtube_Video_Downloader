import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class YouTubeDownloader extends JFrame {

    private JTextField urlField;
    private JComboBox<String> formatBox;
    private JTextArea outputArea;
    private JButton downloadButton;

    public YouTubeDownloader() {
        setTitle("YouTube Video Downloader");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel topPanel = new JPanel(new FlowLayout());
        urlField = new JTextField(30);
        formatBox = new JComboBox<>(new String[]{"best", "mp4", "bestaudio", "mp3"});
        downloadButton = new JButton("Download");

        topPanel.add(new JLabel("YouTube URL:"));
        topPanel.add(urlField);
        topPanel.add(new JLabel("Format:"));
        topPanel.add(formatBox);
        topPanel.add(downloadButton);
        add(topPanel, BorderLayout.NORTH);

        // Output Panel
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Action
        downloadButton.addActionListener(e -> {
            String url = urlField.getText();
            String format = (String) formatBox.getSelectedItem();
            if (!url.isEmpty()) {
                downloadVideo(url, format);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid URL.");
            }
        });

        setVisible(true);
    }

    private void downloadVideo(String url, String format) {
        outputArea.setText("Starting download...\n");

        new Thread(() -> {
            try {
                // Create "downloads" directory if it doesn't exist
                File downloadDir = new File("downloads");
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                }

                // yt-dlp command with output path (-P)
                ProcessBuilder pb = new ProcessBuilder(
                        "yt-dlp",
                        "-P", downloadDir.getAbsolutePath(),
                        "-f", format,
                        url
                );

                pb.redirectErrorStream(true);
                Process process = pb.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    outputArea.append(line + "\n");
                }

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    outputArea.append("\n✅ Download completed!");
                } else {
                    outputArea.append("\n❌ Download failed. Exit code: " + exitCode);
                }

            } catch (IOException | InterruptedException ex) {
                outputArea.append("\n⚠️ Error: " + ex.getMessage());
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new YouTubeDownloader());
    }
}
