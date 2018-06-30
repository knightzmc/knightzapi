package uk.knightz.knightzapi.module;

import spark.Request;
import spark.Response;
import uk.knightz.knightzapi.communication.rsa.RSA;
import uk.knightz.knightzapi.communication.server.Webserver;

import java.util.Base64;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
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
			this.data = new String((RSA.decrypt(Base64.getDecoder().decode(root
							.queryParams("data").getBytes()),
					Base64.getDecoder(

					).decode
							(root.queryParams("aes").
									getBytes()),
					Webserver.getInstance().getPair().getPrivate())));
			if (root.queryParams("module") != null) {
				this.id = root.queryParams("module");
			} else {
                /*
                Either invalid, or a stock system request.
                TODO: Add some form ofGlobal identifier for system requests, rather than just "no module"
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
