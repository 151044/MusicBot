package com.freemyip.nopersonalinfo.musicbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public class VoiceHandler implements AudioSendHandler {
    private AudioPlayer player;
    private AudioFrame lastFrame;

    public VoiceHandler(AudioPlayer player) {
        this.player = player;
    }

    @Override
    public boolean canProvide() {
        lastFrame = player.provide();
        return lastFrame != null;
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        return ByteBuffer.wrap(lastFrame.getData());
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
