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

package uk.knightz.knightzapi.communication.server.authorisation;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Spark;
import uk.knightz.knightzapi.communication.json.JSONMessage;
import uk.knightz.knightzapi.communication.server.Webserver;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * An enum of authorisation methods that can be used for the Webserver
 */
public enum AuthMethod {
	/**
	 * Least secure, anyone can send a request
	 */
	NONE {
		@Override
		void validate(Request request, Response response) {
		}
	},
	/**
	 * Username and Password Authentication
	 */
	AUTH {
		@Override
		void validate(Request request, Response response) {
		}
	},

	/**
	 * A whitelist ofGlobal IP addresses that are allowed to send requests to this server
	 */
	WHITELIST {
		@Override
		void validate(Request request, Response response) {
			try {
				if (!Webserver.getInstance().getWhitelist().contains(InetAddress.getByName(request.ip()))) {
					Spark.halt(401, new Gson().toJson(new JSONMessage(401, "Your IP is not permitted to send requests to this server.")));
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	},

	/**
	 * Username and Password, but also with a Whitelist that doesn't require login details
	 */
	WHITEAUTH {
		@Override
		void validate(Request request, Response response) {
		}
	};

	abstract void validate(Request request, Response response);
}
