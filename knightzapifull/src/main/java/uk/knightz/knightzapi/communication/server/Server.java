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
