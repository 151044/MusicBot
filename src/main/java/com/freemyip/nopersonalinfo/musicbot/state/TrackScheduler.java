package com.freemyip.nopersonalinfo.musicbot.state;

import com.freemyip.nopersonalinfo.musicbot.commands.Commands;
import com.freemyip.nopersonalinfo.musicbot.utils.EmbedHelper;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import java.util.ArrayList;
import java.util.List;

public class TrackScheduler extends AudioEventAdapter {
    private List<AudioTrack> tracks = new ArrayList<>();
    private GuildState state;
    private int counter = 0;

    public TrackScheduler(GuildState guild){
        this.state = guild;
    }
    public void queue(AudioTrack toQueue){
        tracks.add(toQueue);
        if(state.getAudioPlayer().getPlayingTrack() == null){
            next();
        }
    }
    @Override
    public void onPlayerPause(AudioPlayer player) {
        Commands.sendMessage(state.getTextChannel(), EmbedHelper.getEmbed("Paused!","Object Extends Object"));
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        Commands.sendMessage(state.getTextChannel(), EmbedHelper.getEmbed("Resuming!","Object Extends Object"));
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        Commands.sendMessage(state.getTextChannel(), "Next Track: " + track.getInfo().title + "\n" + track.getInfo().uri);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext){
            if(tracks.isEmpty()){
                return;
            }
            AudioTrack nextTrack = tracks.get(counter);
            player.playTrack(nextTrack);
            counter++;
        }else{
            Commands.sendMessage(state.getTextChannel(), endReason.name());
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {

    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        Commands.sendMessage(state.getTextChannel(),"Umm....\nThe track got stuck?\n" + track.getInfo().title + " got stuck!");
    }
    public void next(){
        if(tracks.isEmpty()){
            Commands.sendMessage(state.getTextChannel(),"The track lists are empty!");
            state.getAudioPlayer().startTrack(null,false);
            return;
        }
        state.getAudioPlayer().playTrack(tracks.get(counter));
        if(counter + 1 > tracks.size()){
            counter = 0;
        }else{
            counter++;
        }
    }
    public void remove(int i){
        if(tracks.isEmpty()){
            Commands.sendMessage(state.getTextChannel(),"The track lists are empty!");
            state.getAudioPlayer().startTrack(null,false);
            return;
        }
        if(i < 0 || i > tracks.size()){
            Commands.sendMessage(state.getTextChannel(),"Track number " + i + " is out of range!");
            return;
        }
        if(i == counter - 1){
            next();
        }
        tracks.remove(i - 1);
    }
    public void list(){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < tracks.size(); i++){
            builder.append(i + 1 + ": ");
            AudioTrackInfo info = tracks.get(i).getInfo();
            builder.append(info.title).append("\t\t\t").append(EmbedHelper.toTime(info.length)).append("\n");
        }
        Commands.sendMessage(state.getTextChannel(), "```nimrod\n" + builder.toString() + "\n```");
    }
}
