package com.freemyip.nopersonalinfo.musicbot.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Commands {
    private Commands(){
        throw new AssertionError();
    }
    private static Map<String,Command> commandMap;
    private static Map<List<String>,String> aliasMap;
    static{
        commandMap = new HashMap<>();
        commandMap.put("play",new Play());
        commandMap.put("list",new ListTracks());
        commandMap.put("next", new Next());
        commandMap.put("info", new Info());
        aliasMap = commandMap.values().stream().collect(Collectors.toMap(cmd -> cmd.alias(),cmd -> cmd.callName()));
    }
    public static Optional<Command> tryGet(String byName){
        if(commandMap.containsKey(byName)){
            return Optional.of(commandMap.get(byName));
        }
        if(aliasMap.keySet().stream().flatMap(list -> list.stream()).anyMatch(str -> str.equals(byName))){
            return Optional.of(commandMap.get(aliasMap.entrySet().stream().filter(ent -> ent.getKey().contains(byName)).findFirst().map(ent -> ent.getValue()).get()));
        }else{
            return Optional.empty();
        }
    }
    public static void sendMessage(GuildMessageReceivedEvent evt, String msg){
        sendMessage(evt.getChannel(),msg);
    }
    public static void sendMessage(TextChannel text, String msg){
        if(msg.length() > 1990){
            boolean isCode = false, isFirst = true;
            if(msg.endsWith("```")){
                isCode = true;
            }
            String op = msg;
            while(op.length() > 1980){
                StringBuilder out = new StringBuilder(op.substring(0,op.lastIndexOf("\n",1960)));
                if(isFirst){
                    isFirst = false;
                }else{
                    if(isCode) {
                        out = out.insert(0, "```java\n");
                    }
                }
                if(isCode){
                    text.sendMessage(out.append("```").toString()).queue();
                }
                op = op.substring(op.lastIndexOf("\n",1960) + 1);
            }
            text.sendMessage((isCode ? "```java\n" : "") + op).queue();
        }else {
            text.sendMessage(msg).queue();
        }
    }
    public static void sendMessage(TextChannel text, MessageEmbed toSend){
        text.sendMessage(toSend).queue();
    }
    public static void sendMessage(GuildMessageReceivedEvent evt, MessageEmbed toSend){
        sendMessage(evt.getChannel(),toSend);
    }
}
