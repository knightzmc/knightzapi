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

package uk.knightz.knightzapi.module;

import spark.Request;
import spark.Response;
import uk.knightz.knightzapi.communication.rsa.RSA;
import uk.knightz.knightzapi.communication.server.Webserver;

import java.util.Base64;

/**
 * Represents an incoming web request
 */
public final class IncomingRequest {
	/**
	 * The root {@link Request} Request that has been used to construct this IncomingRequest object.
	 * Note if you use this to read parameters etc, that they may be encrypted
	 *
	 * @see Request
	 */
	private final Request root;
	/**
	 * A {@link Response} Response for the IncomingRequest. This should be edited in {@link Module#onIncomingRequest(IncomingRequest)}
	 * It will then be returned to the sender ofGlobal any IncomingRequest.
	 * Convention is to return JSON plain text, but it's up to you. However,
	 */
	private final Response response;
	private final String data;
	private final String id;

	public IncomingRequest(Request root, Response response) {
		this.root = root;
		this.response = response;
		try {
			this.data = new String((RSA.decrypt(Base64.getDecoder().decode(root.queryParams("data").getBytes()),
					Base64.getDecoder().decode(root.queryParams("aes").getBytes()),
					Webserver.getInstance().getPair().getPrivate())));
			if (root.queryParams("module") != null) {
				this.id = root.queryParams("module");
			} else {
                /*
                Either invalid, or a stock system request.
                TODO: Add some form  of identifier for system requests, rather than just "no module"
                 */
				this.id = "SYSTEM";
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error reading Incoming Request", ex);
		}
	}

	public Response getResponse() {
		return response;
	}

	public String getData() {
		return data;
	}

	public Request getRoot() {
		return root;
	}

	public String getId() {
		return id;
	}
}
