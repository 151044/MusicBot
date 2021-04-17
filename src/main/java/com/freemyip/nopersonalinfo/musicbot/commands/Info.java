package com.freemyip.nopersonalinfo.musicbot.commands;

import com.freemyip.nopersonalinfo.musicbot.state.GlobalState;
import com.freemyip.nopersonalinfo.musicbot.utils.EmbedHelper;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Info implements Command{
    @Override
    public void action(GuildMessageReceivedEvent evt) {
        GlobalState.lookup(evt.getGuild()).ifPresentOrElse((g) -> {
            AudioTrack track = g.getAudioPlayer().getPlayingTrack();
            Commands.sendMessage(evt, EmbedHelper.getEmbed(EmbedHelper.toTime(track.getPosition()) + " / " + EmbedHelper.toTime(track.getDuration()) + " (" + EmbedHelper.toTime(track.getDuration() - track.getPosition()) + " Remaining)",track.getInfo().title));
        },() -> {
            Commands.sendMessage(evt,"You are not connected to a voice channel!");
        });
    }

    @Override
    public List<String> alias() {
        return List.of("i");
    }

    @Override
    public String callName() {
        return "info";
    }
}
