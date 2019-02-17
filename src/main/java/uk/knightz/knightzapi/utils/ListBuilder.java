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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListBuilder<T> {

    private static final int INITIAL_CAPACITY = 0;
    private T[] arr;

    public ListBuilder() {
        arr = (T[]) new Object[INITIAL_CAPACITY];
    }

    public ListBuilder<T> append(T t) {
        T[] newArr = Arrays.copyOf(arr, arr.length + 1);
        newArr[arr.length] = t;
        this.arr = newArr;
        return this;
    }

    public ListBuilder<T> backspace() {
        if (arr.length == 1) {
            return this;
        }
        this.arr = Arrays.copyOf(arr, arr.length - 1);
        return this;
    }

    public List<T> toList() {
        return new ArrayList<>(Arrays.asList(arr));
    }

    public <L extends List<T>> List<T> addAllToList(L list) {
        Collections.addAll(list, arr);
        return list;
    }
}
