package uk.knightz.knightzapi.communication;

import spark.Route;
import spark.Spark;

import java.util.HashSet;
import java.util.Set;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:14.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * A WebModule can dynamically open a web mapping to receive custom requests. It is a glorified wrapper for {@link Route}
 * <p>
 * An overloaded constructor that fills all necessary parameters should be generated, as it is instantiated with Reflection
 * that looks for a no argument constructor
 **/
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

	public void exec() {
		if (verb.equals(Verb.GET)) {
			Spark.get(path, this);
		} else {
			if (verb.equals(Verb.POST)) {
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
