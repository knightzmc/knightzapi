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

package uk.knightz.knightzapi.user;

import java.util.HashMap;
import java.util.Map;

public class UserData {

    /**
     * Non-Persistent data that will be erased after a reload.
     */
    private transient final Map<String, Object> temporaryData = new HashMap<>();
    /**
     * Persistent data that won't be erased after a reload and is stored to the User's data file.
     */
    private final Map<String, Object> persistentData = new HashMap<>();

    public Object getTemporaryData(String s) {
        return temporaryData.get(s);
    }

    public void addTemporaryData(String s, Object o) {
        temporaryData.put(s, o);
    }

    public Object getPersistentData(String s) {
        return persistentData.get(s);
    }

    public void addPersistentData(String s, Object o) {
        persistentData.put(s, o);
    }

}