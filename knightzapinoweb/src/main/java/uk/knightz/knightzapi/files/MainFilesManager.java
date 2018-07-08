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

package uk.knightz.knightzapi.files;

import uk.knightz.knightzapi.KnightzAPI;

public class MainFilesManager implements FilesManager {
    private static MainFilesManager instance;
    private final KnightzAPI api;

    public MainFilesManager(KnightzAPI api) {
        if (instance != null) {
            this.api = instance.api;
        } else {
            this.api = api;
        }
    }

    @Override
    public PluginFile createPluginFile(String fileName) {
        return new PluginFile(KnightzAPI.getP(), fileName);
    }

    @Override
    public PluginFile createPluginFile(String fileName, String defaultsName) {
        return new PluginFile(KnightzAPI.getP(), fileName, defaultsName);
    }

    @Override
    public PluginFile createPluginFile(String fileName, String defaultsName, String directory) {
        return new PluginFile(KnightzAPI.getP(), fileName, defaultsName, directory);
    }
}
