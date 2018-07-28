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
	 * Prettify a given JSON String with GSON
	 * @param json The JSON to prettify
	 * @return The prettified JSON
	 */
	public static String prettifyJson(String json) {
		Validate.notNull(json, "String cannot be null");
		return KnightzAPI.gson.toJson(parser.parse(json));

	}
}
