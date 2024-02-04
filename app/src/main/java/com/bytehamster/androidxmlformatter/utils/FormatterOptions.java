package com.bytehamster.androidxmlformatter.utils;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class FormatterOptions {
  public static final String OPT_INDENTION = "indention";
  public static final String OPT_ATTRIBUTE_INDENTION = "attribute-indention";
  public static final String OPT_ATTRIBUTE_ORDER = "attribute-order";
  public static final String OPT_ATTRIBUTE_SORT = "attribute-sort";
  public static final String OPT_NAMESPACE_ORDER = "namespace-order";
  public static final String OPT_NAMESPACE_SORT = "namespace-sort";

  public static final String DEFAULT_INDENTION = "2";
  public static final String DEFAULT_ATTRIBUTE_ORDER = "id,layout_width,layout_height";
  public static final String DEFAULT_NAMESPACE_ORDER = "android,app";

  public static Options getOptions(String[] args) {
    Options options = new Options();
    options.addOption(Option.builder().longOpt(OPT_INDENTION).desc("Indention.").hasArg().build());
    options.addOption(Option.builder().longOpt(OPT_ATTRIBUTE_INDENTION).desc("Indention of attributes.").hasArg().build());
    options.addOption(Option.builder().longOpt(OPT_ATTRIBUTE_ORDER).desc("When ordering attributes by name, use this order. Separated by comma.").hasArg().build());
    options.addOption(Option.builder().longOpt(OPT_ATTRIBUTE_SORT).desc("Sort attributes.").build());
    options.addOption(Option.builder().longOpt(OPT_NAMESPACE_ORDER).desc("When ordering attributes by namespace, use this order. Separated by comma.").hasArg().build());
    options.addOption(Option.builder().longOpt(OPT_NAMESPACE_SORT).desc("Sort namespaces.").build());
    return options;
  }
}
