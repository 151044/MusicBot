package com.freemyip.nopersonalinfo.musicbot.state;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class GuildState {
    private boolean isConnected = false;
    private VoiceChannel connect;
    private AudioPlayer player;
    private TextChannel dest;
    private TrackScheduler scheduler;
    public GuildState(VoiceChannel voiceChannel,TextChannel dest){
        connect = voiceChannel;
        this.dest = dest;
        isConnected = true;
        player = GlobalState.getPlayerManager().createPlayer();
        scheduler = new TrackScheduler(this);
        player.addListener(scheduler);
    }
    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public VoiceChannel getVoiceChannel() {
        return connect;
    }

    public void setVoiceChannel(VoiceChannel connect) {
        this.connect = connect;
    }
    public void disconnect(){
        connect = null;
        isConnected = false;
        dest = null;
    }
    public TextChannel getTextChannel(){
        return dest;
    }
    public void setTextChannel(TextChannel set){
        dest = set;
    }
    public void reset(VoiceChannel channel, TextChannel text){
        connect = channel;
        this.dest = text;
        isConnected = true;
    }
    public AudioPlayer getAudioPlayer(){
        return player;
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }
}
