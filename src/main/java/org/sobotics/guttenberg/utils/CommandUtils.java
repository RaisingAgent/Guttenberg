package org.sobotics.guttenberg.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bhargav.h on 30-Sep-16.
 */
public class CommandUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandUtils.class);
    
    public static boolean checkForCommand(String message, String command){
        String username = "";
        
        Properties prop = new Properties();

        try{
            prop.load(new FileInputStream(FilePathUtils.loginPropertiesFile));
            username = prop.getProperty("username").substring(0,3).toLowerCase();
        }
        catch (IOException e){
            LOGGER.error("Could not load login.properties", e);
            username = "gut";
        }
        
        return message.split(" ")[0].toLowerCase().startsWith("@"+username) && message.split(" ")[1].toLowerCase().equals(command);
    }
    public static String extractData(String message){
        String parts[] = message.split(" ");
        return String.join(" ", Arrays.copyOfRange(parts,2,parts.length));
    }
    public static String checkAndRemoveMessage(String filename, String message){
        try{
            if(FileUtils.checkIfInFile(filename,message)){
                FileUtils.removeFromFile(filename,message);
                return "Done";
            }
            else {
                return ("It's not there in the file");
            }
        }
        catch (IOException e){
            LOGGER.error("ERROR", e);
            return ("Failed");
        }

    }
    public static String getAnswerId(String word){
        String parts[]= word.split("//")[1].split("/");
        if(parts[1].equals("a") || parts[1].equals("answers")){
            word = parts[2];
        }
        else if (parts[1].equals("q") || parts[1].equals("questions")){
            if (parts[4].contains("#"))
            {
                word = parts[4].split("#")[1];
            }
        }
        return word;
    }

}
