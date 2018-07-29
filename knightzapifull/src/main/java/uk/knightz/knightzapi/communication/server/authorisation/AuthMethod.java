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
