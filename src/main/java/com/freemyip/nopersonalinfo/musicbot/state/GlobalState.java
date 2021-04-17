package com.freemyip.nopersonalinfo.musicbot.state;

import com.freemyip.nopersonalinfo.musicbot.commands.Commands;
import com.freemyip.nopersonalinfo.musicbot.handlers.VoiceHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GlobalState {
    private static Map<Guild, GuildState> global = new HashMap<>();
    private static AudioPlayerManager playerManager;
    static{
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
    }
    public static boolean isConnected(Guild guild){
        return global.containsKey(guild) && global.get(guild).isConnected();
    }
    public static void connect(GuildMessageReceivedEvent evt){
        Member author = evt.getMember();
        Optional<VoiceChannel> optVc = evt.getGuild().getVoiceChannelCache().stream().filter(chan -> chan.getMembers().contains(author)).findFirst();
        if(optVc.isEmpty()){
            Commands.sendMessage(evt, "You are not currently connected to a voice channel!");
            return;
        }
        VoiceChannel vc = optVc.get();
        AudioManager audioMan = vc.getGuild().getAudioManager();
        if(global.containsKey(evt.getGuild())){
            GuildState reset = global.get(evt.getGuild());
            reset.reset(vc,evt.getChannel());
            audioMan.setSendingHandler(new VoiceHandler(reset.getAudioPlayer()));
        }else {
            GuildState state = new GuildState(vc, evt.getChannel());
            audioMan.setSendingHandler(new VoiceHandler(state.getAudioPlayer()));
            global.put(evt.getGuild(), state);
        }
        audioMan.openAudioConnection(vc);
    }
    public static void disconnect(Guild g){
        if(global.containsKey(g)){
            GuildState state = global.get(g);
            Commands.sendMessage(state.getTextChannel(),"Goodbye!");
            state.disconnect();
        }
    }

    public static AudioPlayerManager getPlayerManager() {
        return playerManager;
    }
    public static Optional<GuildState> lookup(Guild query){
        return Optional.ofNullable(global.get(query));
    }
}
