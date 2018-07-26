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

package uk.knightz.knightzapi.lang.placeholder;

import org.bukkit.entity.Player;

import java.util.function.Function;

/**
 * A Placeholder that requires a Player in order to give a response
 *
 * @param <T> The type of response that will be given
 */
public class PlayerDependentPlaceholder<T> extends ObjectPlaceholder<T> {
	private final Function<Player, T> getReplacementFromPlayer;

	public PlayerDependentPlaceholder(String placeholder, T replaceWith, Function<Player, T> getReplacementFromPlayer) {
		super(placeholder, replaceWith);
		this.getReplacementFromPlayer = getReplacementFromPlayer;
	}

	public T apply(Player p) {
		return getReplacementFromPlayer.apply(p);
	}
}
