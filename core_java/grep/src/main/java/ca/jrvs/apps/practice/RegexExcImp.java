package ca.jrvs.apps.practice;

public class RegexExcImp implements RegexExc{
  public boolean matchJpeg(String filename) {
    String pattern = ".*[.]jpe?g";
    return filename.toLowerCase().matches(pattern);
  }

  public boolean matchIp(String ip) {
    String pattern = "^(\\d{1,3}[.]){3}\\d{1,3}$";
    return ip.matches(pattern);
  }

  public boolean isEmptyLine(String line) {
    String pattern = "^\\s+$";
    return line.matches(pattern);
  }
}
