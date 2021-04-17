package com.freemyip.nopersonalinfo.musicbot.commands;

import com.freemyip.nopersonalinfo.musicbot.state.GlobalState;
import com.freemyip.nopersonalinfo.musicbot.state.GuildState;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Play implements Command{
    @Override
    public void action(GuildMessageReceivedEvent evt) {
        List<String> of = List.of(evt.getMessage().getContentRaw().split(" "));
        if(of.size() < 2){
            Commands.sendMessage(evt, "Insufficient arguments.");
            return;
        }
        if(!GlobalState.isConnected(evt.getGuild())){
            GlobalState.connect(evt);
        }
        GlobalState.getPlayerManager().loadItem(of.get(1), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                Commands.sendMessage(evt,"Adding track " + track.getInfo().title + " to the queue!");
                GuildState state = GlobalState.lookup(evt.getGuild()).get();
                state.getScheduler().queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                Commands.sendMessage(evt,"Adding playlist " + playlist.getName() + " to the queue!");
                playlist.getTracks().forEach(t -> GlobalState.lookup(evt.getGuild()).get().getScheduler().queue(t));
            }

            @Override
            public void noMatches() {
                Commands.sendMessage(evt, "Failed to find a match for " + of.get(1) + "!");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                Commands.sendMessage(evt, "An error occurred when loading " + of.get(1) + "!\n" + exception.getMessage());
            }
        });
    }

    @Override
    public List<String> alias() {
        return List.of("p");
    }

    @Override
    public String callName() {
        return "play";
    }
}
