package uk.knightz.knightzapi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.communicationapi.authorisation.NotAuthorisedException;

import java.net.InetSocketAddress;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 17:33.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class CommTestCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        InetSocketAddress address = InetSocketAddress.createUnresolved(args[0].split(":")[0], Integer.valueOf(args[0].split(":")[1]));
        try {
            KnightzAPI.getServerFactory().getServer(address).sendData(args[1]);
        } catch (NotAuthorisedException e) {
            commandSender.sendMessage(e.getMessage());
        }
        return false;
    }
}
