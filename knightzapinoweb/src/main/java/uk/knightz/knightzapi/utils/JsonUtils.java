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

package uk.knightz.knightzapi.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.commons.lang.Validate;
import uk.knightz.knightzapi.KnightzAPI;

/**
 * JSON related Utility class
 */
public class JsonUtils {
	private static final JsonParser parser = new JsonParser();

	private static final Gson compactor = new Gson();

	private JsonUtils() {
	}

	/**
	 * Prettify a given JSON String with Gson
	 * @param json The JSON to prettify
	 * @return The prettified JSON
	 */
	public static String prettifyJson(String json) {
		Validate.notNull(json, "String cannot be null");
		return KnightzAPI.gson.toJson(parser.parse(json));

	}
}
