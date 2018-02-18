package uk.knightz.knightzapi.communication.server.authorisaton;

import com.google.common.net.InetAddresses;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import uk.knightz.knightzapi.utils.CollectionUtils;

import java.net.Inet4Address;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This class was created by AlexL (Knightz) on 16/02/2018 at 13:49.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class Whitelist implements Iterable<Inet4Address>, ConfigurationSerializable {

    private final List<Inet4Address> whitelisted;

    private Whitelist() {
        whitelisted = new ArrayList<>();
    }

    private Whitelist(List<Inet4Address> whitelisted) {
        this.whitelisted = whitelisted;
    }

    public static Whitelist deserialize(Map<String, Object> map) {
        if (!map.containsKey("whitelist")) {
            return new Whitelist();
        }
        List<String> list = (List<String>) map.get("whitelist");
        List<Inet4Address> result = CollectionUtils.changeListType(list, InetAddresses::forString).stream().filter(i -> i instanceof Inet4Address).map(i -> (Inet4Address) i).collect(Collectors.toList());
        return new Whitelist(result);
    }

    public void add(Inet4Address address) {
        whitelisted.add(address);
    }

    public void remove(Inet4Address address) {
        whitelisted.remove(address);

    }

    public boolean contains(Inet4Address address) {
        return whitelisted.contains(address);
    }

    @NotNull
    @Override
    public Iterator<Inet4Address> iterator() {
        return whitelisted.iterator();
    }

    @Override
    public void forEach(Consumer<? super Inet4Address> action) {
        whitelisted.forEach(action);
    }

    @Override
    public Spliterator<Inet4Address> spliterator() {
        return whitelisted.spliterator();
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("whitelist", CollectionUtils.changeListType(whitelisted, Inet4Address::getHostAddress));
        return map;
    }
}
