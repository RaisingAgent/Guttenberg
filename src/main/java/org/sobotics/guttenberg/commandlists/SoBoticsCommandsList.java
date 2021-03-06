package org.sobotics.guttenberg.commandlists;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sobotics.redunda.PingService;
import org.sobotics.guttenberg.commands.*;
import org.sobotics.guttenberg.services.RunnerService;
import org.sobotics.guttenberg.utils.FilePathUtils;

import fr.tunaki.stackoverflow.chat.Message;
import fr.tunaki.stackoverflow.chat.Room;
import fr.tunaki.stackoverflow.chat.User;
import fr.tunaki.stackoverflow.chat.event.MessagePostedEvent;
import fr.tunaki.stackoverflow.chat.event.PingMessageEvent;

/**
 * Created by bhargav.h on 28-Oct-16.
 */
public class SoBoticsCommandsList {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoBoticsCommandsList.class);

    public void mention(Room room, PingMessageEvent event, boolean isReply, RunnerService instance){
        /*if(CheckUtils.checkIfUserIsBlacklisted(event.getUserId()))
            return;*/

        Message message = event.getMessage();
        LOGGER.info("Mention: "+message.getContent());
        List<SpecialCommand> commands = new ArrayList<>(Arrays.asList(
            new Alive(message),
            new Check(message),
            new ClearHelp(message),
            new OptIn(message),
            new OptOut(message),
            new Quota(message),
            new Say(message),
            new Status(message),
            new Update(message),
            new Reboot(message)
        ));

        commands.add(new Commands(message,commands));
        
        for(SpecialCommand command: commands){
            if(command.validate()){
            	boolean standbyMode = PingService.standby.get();
            	if (standbyMode == true) {
            		if (command.availableInStandby() == true) {
            			command.execute(room, instance);
            		}
            	} else {
            		command.execute(room, instance);
            	}
            }
        }
    }
    
    public void globalCommand(Room room, MessagePostedEvent event,  RunnerService instance) {
    	//only ROs (and Generic Bot) should execute global commands!
    	try {
    		User user = event.getUser().get();
    		if (!user.isModerator() && !user.isRoomOwner() && user.getId() != 7481043)
    			return;
    	} catch (Throwable e) {
    		LOGGER.warn("Could not verify privileges of that user. Don't execute the command.", e);
    		return;
    	}
    	
    	
    	Message message = event.getMessage();
        LOGGER.info("Message: "+message.getContent());
        
        int cp = Character.codePointAt(message.getPlainContent(), 0);
        
        if (PingService.standby.get() == false && (cp == 128642 || (cp>=128644 && cp<=128650))) {
        	room.send("[🚃](http://bit.ly/2nRi9kX)");
        	return;
        }
    	
    	//return immediately, if @gut is part of the message!
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
        
        boolean containsUsername = message.getPlainContent().toLowerCase().contains("@"+username);
        //LOGGER.info("containsUsername: "+containsUsername);
        if (containsUsername == true)
        	return;
    	
        List<SpecialCommand> commands = new ArrayList<>(Arrays.asList(
            new Alive(message)
        ));
        
        for(SpecialCommand command: commands){
            if(command.validate()){
            	boolean standbyMode = PingService.standby.get();
            	if (standbyMode == true) {
            		if (command.availableInStandby() == true) {
            			command.execute(room, instance);
            		}
            	} else {
            		command.execute(room, instance);
            	}
            }
        }
    }
}
