package uk.knightz.knightzapi.communication.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import uk.knightz.knightzapi.communication.json.JSONMessage;
import uk.knightz.knightzapi.communication.server.authorisation.NotAuthorisedException;
import uk.knightz.knightzapi.module.Module;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static uk.knightz.knightzapi.communication.rsa.RSA.*;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:33.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class SimpleServer implements Server {
	private final InetSocketAddress address;
	private final HttpClient client;
	private final String pubKey;
	private final String validURL;
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private UsernamePasswordCredentials credentials;

	public SimpleServer(InetSocketAddress address) throws NotAuthorisedException {
		synchronized (this) {
			//Some methods ofGlobal getting an URL put a slash at the start
			validURL = address.toString().replace("/", "");
			HttpClient client1;
			String pubKey1;
			try {
				client1 = HttpClientBuilder.create().build();
				HttpGet get = new HttpGet("http://" + validURL + "/validate");
				HttpResponse response = client1.execute(get);
				JSONMessage message = null;
				try {
					message = new Gson().fromJson(read(response.getEntity().getContent()), JSONMessage.class);
					if (response.getStatusLine().getStatusCode() == 401) {
						throw new NotAuthorisedException(message.getMessage());
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				pubKey1 = message == null ? null : message.getMessage();
			} catch (IOException e) {
				client1 = null;
				pubKey1 = null;
				e.printStackTrace();
			}
			client = client1;
			this.pubKey = pubKey1;
			this.address = address;
		}
	}

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

	@Override
	public InetSocketAddress getAddress() {
		return address;
	}


	@Override
	public Future<HttpResponse> sendData(String data) {
		return sendData(null, data);
	}

	public Future<HttpResponse> sendData(String requestID, String data) {
		@SuppressWarnings("UnusedAssignment") Future<HttpResponse> future = new CompletableFuture<>();
		HttpPost post = new HttpPost("http://" + validURL + "/requests");
		try {
			PublicKey key = loadPublicKey((pubKey));
			Holder byteData = encrypt(data, key);
			post.setEntity(new UrlEncodedFormEntity(new ArrayList<NameValuePair>() {{
				if (requestID != null) add(new BasicNameValuePair("module", requestID));

				add(new BasicNameValuePair("data", new String(Base64.getEncoder().encode(byteData.getByteCipherText()))));
				add(new BasicNameValuePair("aes", new String(Base64.getEncoder().encode(byteData.getEncryptedKey()))));

				if (credentials != null) {
					add(new BasicNameValuePair("username", new String(Base64.getEncoder().encode(credentials.getUserName().getBytes()))));
					add(new BasicNameValuePair("password", new String(Base64.getEncoder().encode(credentials.getPassword().getBytes()))));
				}
			}}));
			Future<HttpResponse> submit = executor.submit(() -> client.execute(post));
			((CompletableFuture<HttpResponse>) future).complete(submit.get());
		} catch (Exception e) {
			throw new RuntimeException("Error sending request to " + address.toString(), e);
		}
		return future;
	}

	@Override
	public Future<HttpResponse> callModule(Module m) {
		return sendData(m.getRequestID(), null);
	}

	@Override
	public Future<HttpResponse> callModule(Module m, String data) {
		return sendData(m.getRequestID(), data);
	}

	@Override
	public Future<HttpResponse> callModule(String requestID) {
		return sendData("modulebyname", requestID);
	}


	@Override
	public String getPubKey() {
		return pubKey;
	}

	/**
	 * Register credentials to use when sending data to this server
	 * TODO: Possible multiple credential support for users?
	 *
	 * @param e The credentials to use
	 */
	public void addCredentials(UsernamePasswordCredentials e) {
		this.credentials = e;
	}
}
