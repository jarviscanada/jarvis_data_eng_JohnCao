# Java Grep App

## Introduction
The Java Grep app searches for patterns in each file within a specified directory and its subdirectories. There are two implementations of the app which provide the same functionality.
One uses core java while the other uses the Stream API provided in Java 8.

Technologies used:
- Java
- Maven
- Docker

## Quick Start
1. Compile and package the Java code using Maven (Maven can be downloaded here: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi))
```
mvn clean package
```
2. Run the Jar file. The program takes three command line arguments: 
    - `regex`: search pattern
    - `rootPath`: root directory path
    - `outFile`: file name where results are written to
```
java -jar target/grep-1.0-SNAPSHOT.jar {regex} {rootPath} {outFile}
```

##Implementation
###Pseudocode
```
initialize matchedLines as empty list
for each file in listFiles(rootDir)
  for each line in file
    if line contains regex
      add line to matchedLines
write matchedLines to outFile
```

##Performance Issue
The simple implementation of the grep app returns an OutOfMemoryError when it attempts to process data that is larger than the size of the heap of the JVM.
This is because a List is being used to store intermediate values which can get extremely large if a lot of data is getting processed.
A solution would be to make use of the Stream API and change the return type of the `readLines` method to a Stream object as shown in `JavaGrepLambdaImp.java`.
Streams won't perform intermediate operations until a terminal operation is invoked on it, therefore saving memory space by not storing intermediate results.

##Test
The app was tested manually. A set of sample text files were saved in a directory to serve as test inputs for the app.
Multiple test cases were run manually through changing the arguments (i.e. different regex patterns, different directory structure).
The results were compared for correctness with the output of the Linux grep command using the same arguments.

##Deployment
A docker image of the application was created and pushed onto DockerHub. To run the application as a docker container:
```
# pull the image from DockerHub
docker pull cyjcao/grep

# run docker container
docker run --rm cyjcao/grep {regex} {rootDir} {outFile}
```

##Improvements
- Output the source file of the line
- Add an option to the user where instead of outputting all lines that matched a given pattern, output the count of all lines in a file that matched.
- Re-implement application to be more memory efficient as specified in the Performance Issue section above