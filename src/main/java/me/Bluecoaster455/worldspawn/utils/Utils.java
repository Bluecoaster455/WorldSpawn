package me.bluecoaster455.worldspawn.utils;

public class Utils {
  
  public static boolean parseBool(String text) {

    switch(text){
      case "1":
      case "true":
      case "on":
      case "yes":
        return true;
      case "0":
      case "false":
      case "off":
      case "no":
        return false;
      default:
        throw new IllegalArgumentException("\""+text+"\" is not a valid option.");
    }

  }

}
