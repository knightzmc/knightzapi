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
