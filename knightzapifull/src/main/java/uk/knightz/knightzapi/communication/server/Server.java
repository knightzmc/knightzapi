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

package uk.knightz.knightzapi.communication.server;

import org.apache.http.HttpResponse;
import uk.knightz.knightzapi.module.Module;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;

/**
 * Represents an external server, running the KnightzAPI Web API.
 */
public interface Server {

	/**
	 * Get the IP address of this server.
	 *
	 * @return The IP address of this server
	 * Will possibly return null in future updates if server aliases are added
	 */
	InetSocketAddress getAddress();

	/**
	 * Send data to this server. It will be automatically encrypted, and any necessary authorisation will also be done
	 *
	 * @param data The data to send
	 * @return A Future of the HttpResponse that the server returned
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

	/**
	 * Execute a module on this server.
	 *
	 * @param m    The module to call
	 * @param data Any other data to provide
	 * @return A Future of the HttpResponse that the server returned
	 */
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
