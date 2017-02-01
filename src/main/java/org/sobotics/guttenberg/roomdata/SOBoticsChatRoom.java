package org.sobotics.guttenberg.roomdata;

import fr.tunaki.stackoverflow.chat.Room;
import fr.tunaki.stackoverflow.chat.event.UserMentionedEvent;

import java.util.function.Consumer;

import org.sobotics.guttenberg.clients.Guttenberg;
import org.sobotics.guttenberg.commandlists.SoBoticsCommandsList;
import org.sobotics.guttenberg.printers.PostPrinter;
import org.sobotics.guttenberg.printers.SoBoticsPostPrinter;

/**
 * Created by bhargav.h on 28-Dec-16.
 */
public class SOBoticsChatRoom implements BotRoom{
    @Override
    public int getRoomId() {
        return 111347;
    }

    @Override
    public Consumer<UserMentionedEvent> getMention(Room room, Guttenberg instance) {
        return event->new SoBoticsCommandsList().mention(room, event, true, instance);
    }

    /*@Override
    public Consumer<MessageReplyEvent> getReply(Room room) {
        return event-> PostUtils.reply(room, event, true);
    }*/

    @Override
    public PostPrinter getPostPrinter() {
        return new SoBoticsPostPrinter();
    }

    @Override
    public boolean getIsLogged() {
        return true;
    }


}
