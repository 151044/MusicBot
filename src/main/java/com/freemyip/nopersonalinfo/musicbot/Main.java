package com.freemyip.nopersonalinfo.musicbot;

import com.freemyip.nopersonalinfo.musicbot.handlers.DisconnectHandler;
import com.freemyip.nopersonalinfo.musicbot.handlers.MessageHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static JDA jda;
    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        jda = JDABuilder.createDefault(Files.readAllLines(Path.of("token.txt")).get(0)).addEventListeners(new MessageHandler(), new DisconnectHandler()).build();
        jda.awaitReady();
    }

    public static JDA getJDA() {
        return jda;
    }
}
