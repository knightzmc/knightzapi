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

package uk.knightz.knightzapi.communication;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.knightz.knightzapi.communication.server.Webserver;
import uk.knightz.knightzapi.module.IncomingRequest;

/**
 * The Main Spark route that handles all incoming requests and passes them to be executed by Modules
 */
public final class MainRoute implements Route {

	@Override
	public Object handle(Request request, Response response) {
		try {
			IncomingRequest ir = new IncomingRequest(request, response);
			Webserver.getInstance().callRequest(ir);
			return "Handled";
		} catch (Exception e) {
			e.printStackTrace();
//			throw new RuntimeException("Error reading Incoming Request", e);
		}
		return null;
	}
}
