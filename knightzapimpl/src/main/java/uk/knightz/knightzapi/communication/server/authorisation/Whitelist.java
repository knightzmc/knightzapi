package uk.knightz.knightzapi.communication.server.authorisation;

import co.aikar.commands.annotation.HelpCommand;
import com.google.common.net.InetAddresses;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import uk.knightz.knightzapi.utils.CollectionUtils;

import java.net.InetAddress;
import java.util.*;
import java.util.function.Consumer;

/**
 * This class was created by AlexL (Knightz) on 16/02/2018 at 13:49.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
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
        List<InetAddress> result = new ArrayList<>(CollectionUtils.changeListType(list, InetAddresses::forString));
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
        map.put("whitelist", CollectionUtils.changeListType(whitelisted, InetAddress::getHostAddress));
        return map;
    }
}
