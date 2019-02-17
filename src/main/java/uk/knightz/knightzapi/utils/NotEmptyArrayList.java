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

import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A wrapper class of ArrayList that cannot be empty
 *
 * @param <E> The element type of the List
 */
public class NotEmptyArrayList<E> extends ArrayList<E> {

	public NotEmptyArrayList(E initial) {
		this.ensureCapacity(1);
		Validate.notNull(initial);
		add(initial);
	}

	@SafeVarargs
	public NotEmptyArrayList(E... initial) {
		super(Arrays.stream(initial).collect(Collectors.toList()));
	}

	@SafeVarargs
	public static <T> NotEmptyArrayList<T> asNotEmptyArrayList(T... args) {
		return new NotEmptyArrayList<>(args);
	}

	public E getFirst() {
		return get(0);
	}

	@Override
	public E set(int index, E e) {
		if (size() <= 1) {
			return e;
		}
		return super.set(index, e);

	}
	@Override
	public E remove(int index) {
		if (size() == 1) {
			throw new UnsupportedOperationException("Removal of element would make NotEmptyArrayList empty");
		}
		return super.remove(index);
	}
}
