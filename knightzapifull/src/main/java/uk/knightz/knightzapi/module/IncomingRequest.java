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
	 * It will then be returned to the sender of any IncomingRequest.
	 * Convention is to return JSON plain text, but it's up to you.
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
