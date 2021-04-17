package com.freemyip.nopersonalinfo.musicbot.handlers;

import com.freemyip.nopersonalinfo.musicbot.Main;
import com.freemyip.nopersonalinfo.musicbot.state.GlobalState;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfDeafenEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DisconnectHandler extends ListenerAdapter {
    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        if(!(event.getMember().getUser().getIdLong() == Main.getJDA().getSelfUser().getIdLong())){
            return;
        }
        Guild disconnected = event.getGuild();
        GlobalState.disconnect(disconnected);
    }

    @Override
    public void onGuildVoiceSelfDeafen(@NotNull GuildVoiceSelfDeafenEvent event) {
        if(!event.isSelfDeafened()){
            event.getMember().deafen(true);
        }
    }
}
