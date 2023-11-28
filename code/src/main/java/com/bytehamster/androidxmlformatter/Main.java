package com.bytehamster.androidxmlformatter;

import com.bytehamster.androidxmlformatter.utils.Constants;
import com.bytehamster.androidxmlformatter.utils.FileUtil;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class Main {

  public static void main(String[] args) throws Exception {
    System.exit(new Main().run(args));
  }

  private int run(String[] args) throws Exception {
    Options options = ensureOptions(args);

    CommandLine cmd;
    try {
      cmd = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.out.println(e.getLocalizedMessage());
      System.out.println();

      String jarPath =
          new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toString())
              .getName();
      new HelpFormatter().printHelp(jarPath + " [OPTIONS] <FILES>", options);
      return 1;
    }

    if (cmd.getArgList().isEmpty()) {
      System.out.println("Usage: [OPTIONS ex: --option] <FILES>");
      return 1;
    }

    List<File> files = new ArrayList<>();

    for (var arg : cmd.getArgList()) {
      if (arg.equals("-")) return 1;
      File file = new File(arg);

      if (file.isDirectory()) {
        addFilesFromDirectory(files, file.listFiles());
      } else if (file.getName().endsWith(".xml")) {
        files.add(file);
      }
    }

    if (files.isEmpty()) {
      System.out.println("Error: no .xml files found");
      return 1;
    }

    try {
      for (File file : files) {
        formatFile(file, cmd);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return 0;
  }

  private void addFilesFromDirectory(List<File> filesToReformat, File[] files) {
    if (files == null) {
      return;
    }
    for (File file : files) {
      if (file.isDirectory()) {
        addFilesFromDirectory(filesToReformat, file.listFiles());
      } else if (file.getName().endsWith(".xml")) {
        filesToReformat.add(file);
      }
    }
  }

  private Options ensureOptions(String[] args) {
    Options options = new Options();
    options.addOption(
        Option.builder().longOpt(Constants.OPT_INDENTION).desc("Indention.").hasArg().build());
    options.addOption(
        Option.builder()
            .longOpt(Constants.OPT_ATTRIBUTE_INDENTION)
            .desc("Indention of attributes.")
            .hasArg()
            .build());
    options.addOption(
        Option.builder()
            .longOpt(Constants.OPT_ATTRIBUTE_ORDER)
            .desc("When ordering attributes by name, use this order. Separated by comma.")
            .hasArg()
            .build());
    options.addOption(Option.builder().longOpt("attribute-sort").desc("Sort attributes.").build());
    options.addOption(
        Option.builder()
            .longOpt(Constants.OPT_NAMESPACE_ORDER)
            .desc("When ordering attributes by namespace, use this order. Separated by comma.")
            .hasArg()
            .build());
    options.addOption(Option.builder().longOpt("namespace-sort").desc("Sort namespaces.").build());
    return options;
  }

  private void formatFile(File file, CommandLine cmd) throws Exception {
    XMLOutputter outputter =
        new AndroidXmlOutputter(
            Integer.parseInt(
                cmd.getOptionValue(Constants.OPT_INDENTION, Constants.DEFAULT_INDENTION)),
            Integer.parseInt(
                cmd.getOptionValue(Constants.OPT_ATTRIBUTE_INDENTION, Constants.DEFAULT_INDENTION)),
            cmd.getOptionValue(Constants.OPT_NAMESPACE_ORDER, Constants.DEFAULT_NAMESPACE_ORDER)
                .split(","),
            cmd.getOptionValue(Constants.OPT_ATTRIBUTE_ORDER, Constants.DEFAULT_ATTRIBUTE_ORDER)
                .split(","),
            cmd.hasOption(Constants.OPT_ATTRIBUTE_SORT),
            cmd.hasOption(Constants.OPT_NAMESPACE_SORT));

    FileUtil.writeFile(
        file,
        outputter.outputString(
            new SAXBuilder().build(new FileInputStream(FileUtil.getFilePath(file)))));
    System.out.println("Done formatting: " + FileUtil.getFilePath(file));
  }
}
