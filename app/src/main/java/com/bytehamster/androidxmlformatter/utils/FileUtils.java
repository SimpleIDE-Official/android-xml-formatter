package com.bytehamster.androidxmlformatter.utils;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class FileUtils {

  public static void writeFile(File file, String content) {
    try (FileOutputStream outputStream = new FileOutputStream(file)) {

      byte[] bytes = content.getBytes();

      outputStream.write(bytes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static final String readFile(File file) {
    try (FileInputStream inputStream = new FileInputStream(file)) {
      return new String(inputStream.readAllBytes());
    } catch (IOException ioe) {
      return "";
    }
  }

  public static String getFilePath(File file) {
    try {
      return file.getCanonicalPath();
    } catch (Exception e) {
      return file.getAbsolutePath();
    }
  }
}
