/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@CommandAlias("fakereq")
public class FakeRequestCommand extends BaseCommand {
	public static String read(InputStream stream) {
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

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