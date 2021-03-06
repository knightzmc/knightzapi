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

import org.bukkit.Bukkit;
import uk.knightz.knightzapi.KnightzAPI;


/**
 * Version compatibility related utility class
 */
public class VersionUtil {


    private VersionUtil() {
    }

    /**
     * @return The current server version
     */
    public static Version getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        if (name.contains("knightzapi")) {
            return Version.v1_12; //For Unit Testing
        }
        String versionPackage = name.substring(name.lastIndexOf(46) + 1) + ".";
        Version[] var2 = Version.values();
        for (Version version : var2) {
            if (version.matchesPackageName(versionPackage)) {
                return version;
            }
        }
        throw new RuntimeException("Unsupported Minecraft Version! KnightzAPI supports 1.8-1.12.2");
    }

    /**
     * Check that the current server version is supported by KnightzAPI
     * Called in {@link KnightzAPI#onEnable()}, so is unlikely it will need to be called again
     */
    public static void checkVersion() {
        try {
            getVersion();
        } catch (RuntimeException ex) {
            Bukkit.getPluginManager().disablePlugin(KnightzAPI.getP());
            throw ex;
        }
    }

    /**
     * Check that the current version is newer or equal than the given version
     *
     * @param v The version to check
     * @return if the current server version is newer or equal than the given version
     */
    public static boolean isNewerThan(Version v) {
        return getVersion().version() > v.version();
    }


    public enum Version {
        v1_8,
        v1_9,
        v1_10,
        v1_11,
        v1_12;


        public int version() {
            return ordinal();
        }

        public boolean matchesPackageName(String name) {
            return name.contains(name());
        }

        public String toString() {
            switch (this) {
                case v1_8:
                    return "1.8";
                case v1_9:
                    return "1.9";
                case v1_10:
                    return "1.10";
                case v1_11:
                    return "1.11";
                case v1_12:
                    return "1.12";
            }
            return null;
        }
    }
}
