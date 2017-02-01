package org.sobotics.guttenberg.finders;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Properties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sobotics.guttenberg.utils.ApiUtils;
import org.sobotics.guttenberg.utils.FilePathUtils;
import org.sobotics.guttenberg.utils.StatusUtils;

import com.google.gson.JsonArray;

/**
 * Fetches the most recent answers
 * */
public class NewAnswersFinder {    
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NewAnswersFinder.class);
    
    public static JsonArray findRecentAnswers() {
        //long unixTime = (long)System.currentTimeMillis()/1000;
        Instant time = Instant.now().minusSeconds(59+1);
        
        //Use time of last execution-start to really get ALL answers
        if (StatusUtils.lastSucceededExecutionStarted != null)
            time = StatusUtils.lastSucceededExecutionStarted;
        
        
        Properties prop = new Properties();

        try{
            prop.load(new FileInputStream(FilePathUtils.loginPropertiesFile));
        }
        catch (IOException e){
            LOGGER.error("Could not load login.properties", e);
            return new JsonArray();
        }
        
        try {
            com.google.gson.JsonObject apiResult = ApiUtils.getFirstPageOfAnswers(time, "stackoverflow", prop.getProperty("apikey", ""));
            //fetched answers
            
            JsonArray items = apiResult.get("items").getAsJsonArray();
            //System.out.println(items);
            LOGGER.info("findRecentAnswers() done with "+items.size()+" items");
            
            return items;
            
            
        } catch (IOException e) {
            LOGGER.error("Could not load recent answers", e);
            return new JsonArray();
        }
    }
    
}
