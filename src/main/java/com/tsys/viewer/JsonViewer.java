package com.tsys.viewer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class JsonViewer {
  private static final int INDENT_FACTOR = 2;
  private final Path path;
  private int lineNumber = 0;
  private boolean showLineNumbers;

  public JsonViewer(String pathToFile) {
    path = Paths.get(pathToFile);
  }

  private void write(String line) {
    System.out.println(line);
  }

  public void writePrettyJson(String json) {
    JSONTokener jt = new JSONTokener(json);
    writePrettyJson(jt.nextValue());
  }

  private void writePrettyJson(Object json) {
    String name = json.getClass().getSimpleName();
    if ("JSONArray".equals(name))
      writeJsonLines(((JSONArray) json).toString(INDENT_FACTOR));
    else if ("JSONObject".equals(name))
      writeJsonLines(((JSONObject) json).toString(INDENT_FACTOR));
    else
      System.out.println("Invalid Json");
  }

  private void writeJsonLines(String lines) {
    Stream.of(lines.split("\n")).forEach(line -> {
      if (showLineNumbers)
        write(String.format("%4d %s", ++lineNumber, line));
      else
        write(line);
    });
  }

  public void showPrettyJson() throws IOException {
    try (Stream<String> lines = Files.lines(path)) {
      lines.forEach(line -> writePrettyJson(line));
    } catch (IOException e) {
      throw e;
    }
  }

  public void showRaw() throws IOException {
    try (Stream<String> lines = Files.lines(path)) {
      if (showLineNumbers) {
        lines.forEach(line -> write(String.format("%4d %s", ++lineNumber, line)));
      } else {
        lines.forEach(line -> write(line));
      }
    } catch (IOException e) {
      throw e;
    }
  }

  public JsonViewer withLineNumbers() {
    showLineNumbers = true;
    return this;
  }
}
