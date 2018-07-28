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
