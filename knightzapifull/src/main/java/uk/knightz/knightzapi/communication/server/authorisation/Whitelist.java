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

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Controls a whitelist of IP addresses that should be allowed to send requests to the server
 */
public class Whitelist implements Iterable<InetAddress>, ConfigurationSerializable {

	private final List<InetAddress> whitelisted;

	private Whitelist() {
		whitelisted = new ArrayList<>();
	}

	private Whitelist(List<InetAddress> whitelisted) {
		this.whitelisted = whitelisted;
	}

	public static Whitelist deserialize(Map<String, Object> map) {
		if (!map.containsKey("whitelist")) {
			return new Whitelist();
		}
		List<String> list = (List<String>) map.get("whitelist");
		List<InetAddress> result = new ArrayList<>();
		for (String s : list) {
			InetAddress byName = null;
			try {
				byName = InetAddress.getByName(s);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			result.add(byName);
		}
		return new Whitelist(result);
	}

	public void add(InetAddress address) {
		whitelisted.add(address);
	}

	public void remove(InetAddress address) {
		whitelisted.remove(address);

	}

	public boolean contains(InetAddress address) {
		return whitelisted.contains(address);
	}

	@NotNull
	@Override
	public Iterator<InetAddress> iterator() {
		return whitelisted.iterator();
	}

	@Override
	public void forEach(Consumer<? super InetAddress> action) {
		whitelisted.forEach(action);
	}

	@Override
	public Spliterator<InetAddress> spliterator() {
		return whitelisted.spliterator();
	}

	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("whitelist", whitelisted.stream().map(InetAddress::getHostAddress).collect(Collectors.toList()));
		return map;
	}
}
