package uk.knightz.knightzapi;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.command.CommandSender;
import uk.knightz.knightzapi.communication.server.ServerFactory;
import uk.knightz.knightzapi.communication.server.SimpleServer;
import uk.knightz.knightzapi.communication.server.authorisation.NotAuthorisedException;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
@CommandAlias("d")
public class Test extends BaseCommand {

    @Default
    public void input(CommandSender sender) {
        try {
            System.out.println(SimpleServer.read(ServerFactory
                    .getInstance().
                            getServer(
                                    InetSocketAddress
                                            .createUnresolved(
                                                    "localhost", 4567))
                    .callModule("test"
                    ).getEntity().getContent()));
        } catch (NotAuthorisedException | IOException e) {
            e.printStackTrace();
        }
    }
}
