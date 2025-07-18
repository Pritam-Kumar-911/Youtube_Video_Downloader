# 🎥 YouTube Video Downloader (JavaFX)

A simple desktop YouTube video downloader built using JavaFX and `yt-dlp`. This project provides a GUI for downloading videos from YouTube using the powerful yt-dlp backend.

> ⚠️ This release contains **source code only**. You will need to configure JavaFX and compile/run it manually.

---

## 🧩 Features

- JavaFX GUI
- Video downloading via [yt-dlp](https://github.com/yt-dlp/yt-dlp)
- Clean and simple interface
- Lightweight, written in pure Java

---

## ⚙️ Requirements

- JDK 17+ (Java Development Kit)
- JavaFX SDK (Tested with JavaFX 24.0.2)
- yt-dlp executable (`yt-dlp.exe`) in the project root or system PATH
- IntelliJ IDEA (Recommended) or any Java IDE

---

## 🛠️ How to Run (Manual Configuration)

### 🔁 1. Download JavaFX

Download JavaFX SDK from [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)

- Extract it and place it somewhere like:
- 
### 📁 2. Project Structure

Make sure your folder looks like this:

YourProject/  
├─ src/  
│ └─ YouTubeDownloader.java  
├─ yt-dlp.exe  

## 🛠️ IntelliJ IDEA Configuration
- Go to Run > Edit Configurations...  
- Under VM Options, add:  
--module-path "C:\javafx-sdk-24.0.2\lib" --add-modules javafx.controls,javafx.fxml
