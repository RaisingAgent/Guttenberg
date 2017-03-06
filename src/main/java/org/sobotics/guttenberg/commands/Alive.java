package org.sobotics.guttenberg.commands;

import org.sobotics.guttenberg.utils.CommandUtils;
import org.sobotics.guttenberg.clients.Guttenberg;
import org.sobotics.guttenberg.services.RunnerService;

import fr.tunaki.stackoverflow.chat.Message;
import fr.tunaki.stackoverflow.chat.Room;

/**
 * Created by bhargav.h on 30-Sep-16.
 */
public class Alive implements SpecialCommand {


    private final Message message;

    public Alive(Message message) {
        this.message = message;
    }

    @Override
    public boolean validate() {
        return CommandUtils.checkForCommand(message.getPlainContent(),"alive");
    }

    @Override
    public void execute(Room room, RunnerService instance) {
        room.replyTo(message.getId(), "Not sure. Ask someone else.");
    }

    @Override
    public String description() {
        return "Returns a test reply to inform that the bot is alive";
    }

    @Override
    public String name() {
        return "alive";
    }
}
