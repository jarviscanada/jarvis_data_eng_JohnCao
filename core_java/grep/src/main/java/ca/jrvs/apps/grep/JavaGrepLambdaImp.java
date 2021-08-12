package ca.jrvs.apps.grep;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class JavaGrepLambdaImp extends JavaGrepImp{

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    //creating JavaGrepLambdaImp instead of JavaGrepImp
    //JavaLambdaImp inherits all methods except two override methods
    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {
      //calling parent method,
      //but it will call override method (in this class)
      javaGrepLambdaImp.process();
    } catch (Exception ex) {
      javaGrepLambdaImp.logger.error("Failed to process", ex);
    }
  }

  @Override
  public List<String> readLines(File inputFile) {
    if (!inputFile.isFile()) {
      throw new IllegalArgumentException("ERROR: inputFile is not a file!");
    }

    try {
      return Files.lines(Paths.get(inputFile.getPath()))
          .collect(Collectors.toList());
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> files = null;
    try {
      files = Files.walk(Paths.get(rootDir))
          .filter(Files::isRegularFile)
          .map(Path::toFile)
          .collect(Collectors.toList());
    } catch (Exception ex) {
      logger.error("Failed to list files", ex);
    }

    return files;
  }
}
