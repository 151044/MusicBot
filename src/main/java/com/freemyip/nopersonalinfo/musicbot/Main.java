package com.freemyip.nopersonalinfo.musicbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {
    private static JDA jda;
    public static void main(String[] args) throws LoginException, InterruptedException {
        jda = JDABuilder.createDefault("NjkyNjUyOTY2OTA1MzgwOTI1.Xo1Jtg.N2DAwX-xKKnLGqUZwsmuKKNzJS4").addEventListeners(new MessageHandler(), new DisconnectHandler()).build();
        jda.awaitReady();
    }

    public static JDA getJDA() {
        return jda;
    }
}