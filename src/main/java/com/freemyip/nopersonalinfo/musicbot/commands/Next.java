package com.freemyip.nopersonalinfo.musicbot.commands;

import com.freemyip.nopersonalinfo.musicbot.state.GlobalState;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Next implements Command{
    @Override
    public void action(GuildMessageReceivedEvent evt) {
        GlobalState.lookup(evt.getGuild()).ifPresentOrElse((g) -> g.getScheduler().next(),() -> {
            Commands.sendMessage(evt,"You are not connected to a voice channel!");
        });
    }

    @Override
    public List<String> alias() {
        return List.of("n");
    }

    @Override
    public String callName() {
        return "next";
    }
}
