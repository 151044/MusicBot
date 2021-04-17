package com.freemyip.nopersonalinfo.musicbot;

import com.freemyip.nopersonalinfo.musicbot.commands.Command;
import com.freemyip.nopersonalinfo.musicbot.commands.Commands;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MessageHandler extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        if(!msg.startsWith("+") ){
            return;
        }
        String first = msg.substring(1, !msg.contains("\n") ? msg.length() : msg.indexOf("\n")).split(" ")[0];
        Optional<Command> get = Commands.tryGet(first);
        if(get.isEmpty()){
            Commands.sendMessage(event,"Cannot find this command! Sorry!");
            return;
        }
        get.get().action(event);
    }
}
