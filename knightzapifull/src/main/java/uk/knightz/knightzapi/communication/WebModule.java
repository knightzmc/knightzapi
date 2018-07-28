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
