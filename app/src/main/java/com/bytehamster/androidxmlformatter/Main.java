package com.bytehamster.androidxmlformatter;

import com.bytehamster.androidxmlformatter.utils.FileUtils;
import com.bytehamster.androidxmlformatter.utils.FormatterOptions;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class Main {

  public static void main(String[] args) throws Exception {
    System.exit(new Main().run(args));
  }

  private int run(String[] args) throws Exception {
    Options options = FormatterOptions.getOptions(args);

    CommandLine cmd;
    try {
      cmd = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.out.println(e.getLocalizedMessage());
      System.out.println();
      
      printHelp(options);
      return 1;
    }

    if (cmd.getArgList().isEmpty()) {
      printHelp(options);
      return 1;
    }

    List<File> files = new ArrayList<>();
    for (String arg : cmd.getArgList()) {
      if (arg.equals("-")) return 1;

      File file = new File(arg);
      if (file.exists()) {
        addFilesFromDir(files, file);
      }
    }

    if (files.isEmpty()) {
      System.out.println("Error: no .xml files found");
      return 1;
    }

    int formattedCount = 0;
    for (File file : files) {
      if (formatFile(file, cmd)) {
        formattedCount += 1;
      }
    }
    System.out.println(formattedCount + " files formated!");

    return 0;
  }

  private void addFilesFromDir(List<File> formatFiles, File file) {
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        addFilesFromDir(formatFiles, f);
      }
      return;
    }

    if (file.getName().endsWith(".xml")) {
      formatFiles.add(file);
    }
  }

  private boolean formatFile(File file, CommandLine cmd) {
    try {
      XMLOutputter outputter =new AndroidXmlOutputter(
        Integer.parseInt(cmd.getOptionValue(FormatterOptions.OPT_INDENTION, FormatterOptions.DEFAULT_INDENTION)),
        Integer.parseInt(cmd.getOptionValue(FormatterOptions.OPT_ATTRIBUTE_INDENTION, FormatterOptions.DEFAULT_INDENTION)),
        cmd.getOptionValue(FormatterOptions.OPT_NAMESPACE_ORDER, FormatterOptions.DEFAULT_NAMESPACE_ORDER).split(","),
        cmd.getOptionValue(FormatterOptions.OPT_ATTRIBUTE_ORDER, FormatterOptions.DEFAULT_ATTRIBUTE_ORDER).split(","),
        cmd.hasOption(FormatterOptions.OPT_ATTRIBUTE_SORT),
        cmd.hasOption(FormatterOptions.OPT_NAMESPACE_SORT)
      );

      String formatedText = outputter.outputString(new SAXBuilder().build(new FileInputStream(file))).trim();
      FileUtils.writeFile(file, formatedText );
      System.out.println("Done formatting: " + file);
      return true;
    } catch (Exception e) {
      System.out.println("Error formatting: " + file + ". Exception: " + e.getMessage());
      return false;
    }
  }

  private void printHelp(Options options) {
    new HelpFormatter().printHelp("[OPTIONS] <FILES>", options);
  }
}