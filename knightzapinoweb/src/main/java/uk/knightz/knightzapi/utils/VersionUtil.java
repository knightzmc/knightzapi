package uk.knightz.knightzapi.utils;

import org.bukkit.Bukkit;
import uk.knightz.knightzapi.KnightzAPI;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class VersionUtil {


	private VersionUtil() {}

	public static Version getVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String versionPackage = name.substring(name.lastIndexOf(46) + 1) + ".";
		Version[] var2 = Version.values();
		for (Version version : var2) {
			if (version.matchesPackageName(versionPackage)) {
				return version;
			}
		}
		throw new RuntimeException("Unsupported Minecraft Version! KnightzAPI supports 1.8-1.12.2");
	}

	public static void checkVersion() {
		try {
			getVersion();
		} catch (RuntimeException ex) {
			Bukkit.getPluginManager().disablePlugin(KnightzAPI.getP());
			throw ex;
		}
	}

	public static boolean isNewerThan(Version v) {
		return getVersion().version() >= v.version();
	}


	public enum Version {
		v1_8(1),
		v1_9(2),
		v1_10(3),
		v1_11(4),
		v1_12(5);

		private final int v;

		Version(int v) {this.v = v;}

		public int version() {return v;}

		public boolean matchesPackageName(String name) {
			return name.contains(name());
		}
	}
}
