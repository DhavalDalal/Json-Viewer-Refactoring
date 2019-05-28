package com.tsys.viewer;

import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.util.List;

public class Main {

  public static void main(String[] args) throws Exception {
    // create Options object
    Options options = new Options();
    Option help = new Option("h", "help", false, "print this message");
    Option version = new Option("v", "version", false, "print the version information and exit");
    Option lineNumbers = new Option("l", "lines", false, "Show line numbers");
    Option raw = new Option("r", "raw", false, "Show JSON as received");

    options.addOption(help);
    options.addOption(version);
    options.addOption(lineNumbers);
    options.addOption(raw);

    CommandLine line = null;
    try {
      CommandLineParser parser = new DefaultParser();
      line = parser.parse(options, args);
    } catch (ParseException exp) {
      // oops, something went wrong
      System.err.println("On Command Line, " + exp.getMessage());
      System.exit(1);
    }

    if (line.hasOption('h')) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("prettyjson <file>", "Pretty JSON formatter", options, "", true);
      System.exit(0);
    }

    if (line.hasOption('v')) {
      HelpFormatter formatter = new HelpFormatter();
      final PrintWriter writer = new PrintWriter(System.out);
      formatter.printWrapped(writer, 100, "Name: prettyjson\nVersion: 1.0-SNAPSHOT");
      writer.flush();
      System.exit(0);
    }
    // This gives what remains after parsing of the options
    // In our case, filename is a non-optional command,
    // hence must be passed on the cmdline.  If it is not
    // passed, below argList would be empty.
    final List<String> argList = line.getArgList();
    if (args.length == 0 || argList.isEmpty()) {
      HelpFormatter formatter = new HelpFormatter();
      final PrintWriter writer = new PrintWriter(System.out);
      formatter.printUsage(writer, 80, "prettyjson <file>", options);
      writer.flush();
      System.exit(0);
    }

    String filename = argList.get(0);
//        System.out.println("filename = " + filename);
    final JsonViewer jsonViewer = new JsonViewer(filename);

    if (line.hasOption('r') && line.hasOption('l')) {
      jsonViewer.withLineNumbers().showRaw();
      return;
    }

    if (line.hasOption('r')) {
      jsonViewer.showRaw();
      return;
    }

    if (line.hasOption('l')) {
      jsonViewer.withLineNumbers().showPrettyJson();
      return;
    }
    jsonViewer.showPrettyJson();
  }
}
