package com.bytehamster.androidxmlformatter.utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class FileUtil {

  public static void writeFile(File file, String content) {
    try {
      FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
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
