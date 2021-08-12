package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep {
  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    //Use default logger config
    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error("Failed to process files", ex);
    }
  }

  @Override
  public void process() throws IOException {
    ArrayList<String> matchedLines = new ArrayList<String>();
    for (File file: listFiles(getRootPath())){
      for (String line: readLines(file)) {
        if (containsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }
    writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    File directory = new File(rootDir);
    // list of files and directories in directory
    File[] files = directory.listFiles();
    List<File> fileList = new ArrayList<File>();
    for (File file: files) {
      if (file.isFile()) {
        fileList.add(file);
      // recursively add any files in subdirectories of directory
      } else if (file.isDirectory()) {
        fileList.addAll(listFiles(file.getAbsolutePath()));
      }
    }
    return fileList;
  }

  @Override
  public List<String> readLines(File inputFile) {
    if (!inputFile.isFile()) {
      throw new IllegalArgumentException("ERROR: inputFile is not a file!");
    }

    List<String> lines = new ArrayList<String>();
    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
      String currentLine;
      while ((currentLine = bufferedReader.readLine()) != null) {
        lines.add(currentLine);
      }
      bufferedReader.close();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    return lines;
  }

  @Override
  public boolean containsPattern(String line) {
    return line.matches(getRegex());
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(getOutFile()));){
      for (String line : lines) {
        bufferedWriter.write(line);
        bufferedWriter.newLine();
      }
      bufferedWriter.flush();
    }
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
}
