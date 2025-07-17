import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class YouTubeDownloader extends JFrame {

    private JTextField urlField;
    private JComboBox<String> formatBox;
    private JTextArea outputArea;
    private JButton downloadButton;
    private JProgressBar progressBar;

    public YouTubeDownloader() {
        setTitle("YouTube Video Downloader");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("YouTube URL:"));
        urlField = new JTextField(35);
        row1.add(urlField);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Format:"));
        formatBox = new JComboBox<>(new String[]{"best", "mp4", "bestaudio", "mp3"});
        row2.add(formatBox);
        downloadButton = new JButton("Download");
        row2.add(downloadButton);

        inputPanel.add(row1);
        inputPanel.add(row2);
        add(inputPanel, BorderLayout.NORTH);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Progress Bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.SOUTH);

        // Button Action
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
        progressBar.setIndeterminate(true);
        progressBar.setString("Downloading...");

        new Thread(() -> {
            try {
                // Ensure "downloads" folder exists
                File downloadDir = new File("downloads");
                if (!downloadDir.exists()) downloadDir.mkdir();

                ProcessBuilder pb = new ProcessBuilder("yt-dlp", "-f", format, url);
                pb.directory(downloadDir);
                pb.redirectErrorStream(true);
                Process process = pb.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    outputArea.append(line + "\n");
                }

                int exitCode = process.waitFor();
                progressBar.setIndeterminate(false);
                if (exitCode == 0) {
                    progressBar.setString("✅ Download completed");
                    outputArea.append("\n✅ Download completed!");
                } else {
                    progressBar.setString("❌ Download failed");
                    outputArea.append("\n❌ Download failed. Exit code: " + exitCode);
                }

            } catch (IOException | InterruptedException ex) {
                progressBar.setIndeterminate(false);
                progressBar.setString("⚠️ Error");
                outputArea.append("\n⚠️ Error: " + ex.getMessage());
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(YouTubeDownloader::new);
    }
}
