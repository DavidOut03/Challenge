package com.davidout.ChallengeAPI;

import com.davidout.ChallengeAPI.Types.ChallengeType;
import com.davidout.Main;
import com.davidout.Utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CMD_Challenge implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(!commandSender.isOp()) {
            commandSender.sendMessage(Chat.format("&cYoure not allowed to start or stop a challenge."));
            return true;
        }

        if(args.length != 2) {
            if(args.length > 0 && args[0].equalsIgnoreCase("stop")) {
                if(Main.getInstance().getChallengeManager().getCurrentChallenges().stream().findFirst().isPresent()) {
                    Challenge challenge = Main.getInstance().getChallengeManager().getCurrentChallenges().stream().findFirst().get();
                    Main.getInstance().getChallengeManager().stopChallenge(challenge.getChallengeID());
                }
                return true;
            }

            if(args.length > 0 && args[0].equalsIgnoreCase("skipround")) {
                if(Main.getInstance().getChallengeManager().getCurrentChallenges().stream().findFirst().isPresent()) {
                    Challenge challenge = Main.getInstance().getChallengeManager().getCurrentChallenges().stream().findFirst().get();
                    challenge.skipRound();
                    return true;
                }
            }

            if(args.length > 0 && args[0].equalsIgnoreCase("eliminate") && commandSender instanceof  Player) {
                Player p = (Player) commandSender;
                ChallengePlayer cp = Main.getInstance().getChallengeManager().getChallengePlayer(p.getUniqueId());
                Challenge challenge = Main.getInstance().getChallengeManager().getChallenge(cp.getChallengeID());

                if(cp == null || cp.getPlayerUUID() == null || challenge == null) {
                    p.sendMessage(Chat.format("&cYou are not in an event."));
                    return false;
                }

                challenge.eliminatePlayer(p);
            }

            commandSender.sendMessage(Chat.format("&7Use: /challenge [start/stop/skipround] [challengetype]"));
            return false;
        }


            String cmd = args[0];
            String challengetype = args[1];

            if(cmd.equalsIgnoreCase("start")) {
                if(!(commandSender instanceof Player)) return false;

                if(Bukkit.getOnlinePlayers().size() < 2 && !challengetype.equalsIgnoreCase(ChallengeType.RANDOM_ITEM.name()) && !challengetype.equalsIgnoreCase(ChallengeType.BLOCK_FALL.name())) {
                    commandSender.sendMessage(Chat.format("&cYou need more players before you can start a challenge."));
                    return false;
                }

                if(Main.getInstance().getChallengeManager().getCurrentChallenges().size() >= 1) {
                    commandSender.sendMessage(Chat.format("&cYou first have to stop the current challenge to proceed."));
                    return false;
                }
                    ChallengeType type = ChallengeType.valueOf(challengetype);

                    if(type == null) {
                        commandSender.sendMessage(Chat.format("&cCould not start a challenge by the name of" + challengetype + "."));
                        return false;
                    }
                    Player p = (Player) commandSender;
                    Main.getInstance().getChallengeManager().startChallenge(type, p.getWorld());
                    return true;

            }
        return false;
    }

    private static String[] COMMANDS = { "start", "stop" ,"skipround"};

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        final List<String> completions = new ArrayList<>();

        if(args.length == 1) {
            if(!commandSender.isOp()) {
                COMMANDS = new String[]{"start", "stop"};
            }
            StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
        } else if(args[0].equalsIgnoreCase("start")){
            ArrayList<String> types = ChallengeType.getChallengeTypes();
            StringUtil.copyPartialMatches(args[1], types, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
