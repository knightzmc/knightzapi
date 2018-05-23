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