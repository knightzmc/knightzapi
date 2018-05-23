package uk.knightz.knightzapi.communication.server;

import org.apache.http.HttpResponse;
import uk.knightz.knightzapi.module.Module;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:29.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public interface Server {

	/**
	 * Get the IP address of this server.
	 *
	 * @return The IP address of this server
	 * @apiNote Will possibly return null in future updates if server aliases are added
	 */
	InetSocketAddress getAddress();

	/**
	 * Send data to this server. It will be automatically encrypted, and any neccessary authorisation will also be done
	 *
	 * @param data The data to send
	 */
	Future<HttpResponse> sendData(String data);


	/**
	 * Execute a module on this server.
	 * If the server does not have the module installed, one of two things will occur
	 * NOT YET IMPLEMENTED 1) If the server has auto-download set in their config the module will be downloaded and run (potentially dangerous!).
	 * 2) Otherwise, nothing
	 *
	 * @param m The module to call
	 * @return The server's HTTP Response after sending the request
	 * @see Module#forName(String)
	 */
	Future<HttpResponse> callModule(Module m);

	Future<HttpResponse> callModule(Module m, String data);

	Future<HttpResponse> sendData(String reqID, String data);
	/**
	 * Searches for a module with the given request ID on this server, if it is present execute it.
	 *
	 * @param requestID The module request ID to search for, and if present, execute.
	 * @return The server's HTTP Response after sending the request
	 * @see Server#callModule(Module)
	 */
	Future<HttpResponse> callModule(String requestID);

	/**
	 * Get the public RSA used for this Server to encrypt data.
	 *
	 * @return The public key in plain text format
	 */
	String getPubKey();

}
