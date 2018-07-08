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

import org.bukkit.Bukkit;
import uk.knightz.knightzapi.KnightzAPI;

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
