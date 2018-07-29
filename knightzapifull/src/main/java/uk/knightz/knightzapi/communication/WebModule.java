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

import spark.Route;
import spark.Spark;

import java.util.HashSet;
import java.util.Set;

import static uk.knightz.knightzapi.communication.WebModule.Verb.GET;
import static uk.knightz.knightzapi.communication.WebModule.Verb.POST;

/**
 * A WebModule is a Spark Route wrapper that can handle simple Web related content.
 * If you are looking for something more dynamic and interactive with the server, consider {@link uk.knightz.knightzapi.module.Module}
 */
public abstract class WebModule implements Route {
	private static final Set<WebModule> allModules = new HashSet<>();
	private final String name;
	private final String path;

	private final Verb verb;

	public WebModule(String name, String path, Verb verb) {
		this.name = name;
		this.path = path;
		this.verb = verb;
		allModules.add(this);
	}

	public static Set<WebModule> getAllModules() {
		return allModules;
	}

	/**
	 * Register this WebModule in Spark
	 */
	public void exec() {
		if (verb.equals(GET)) {
			Spark.get(path, this);
		} else {
			if (verb.equals(POST)) {
				Spark.post(path, this);
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public enum Verb {
		GET, POST
	}
}
