/*
 *     This file is part of KnightzAPI
 *
 *     KnightzAPI - A cross server communication library and general utility API for Minecraft Servers
 *     Copyright (C) 2018 Alexander Leslie John Wood
 *
 *     KnightzAPI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KnightzAPI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KnightzAPI.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     The author of this program, Alexander Leslie John Wood can be contacted at alexwood2403@gmail.com
 *
 */

package uk.knightz.knightzapi;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.apache.http.HttpResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import uk.knightz.knightzapi.communication.server.ServerFactory;
import uk.knightz.knightzapi.communication.server.authorisation.NotAuthorisedException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static uk.knightz.knightzapi.communication.server.SimpleServer.read;

/**
 * Debugging COMMAND for sending a fake request to the current server.
 * Allows for debugging without having multiple servers running
 */
@CommandAlias("fakereq")
public class FakeRequestCommand extends BaseCommand {

	@Default
	@CatchUnknown
	public void doCommand(CommandSender p, String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(KnightzAPI.getP(), () -> {
			Future<HttpResponse> future;
			try {
				future = ServerFactory.getInstance().getServer
						(new InetSocketAddress("127.0.0.1", 4567))
						.sendData(args[0], args[1]);
				while (true) {
					if (future.isDone()) break;
				}
				Future<HttpResponse> finalFuture = future;
				Bukkit.getScheduler().runTask(KnightzAPI.getP(), () ->
				{
					try {
						p.sendMessage("Response: " + read(finalFuture.get().getEntity().getContent()));
					} catch (IOException | InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				});
			} catch (NotAuthorisedException e) {
				e.printStackTrace();
			}
		});

	}

}