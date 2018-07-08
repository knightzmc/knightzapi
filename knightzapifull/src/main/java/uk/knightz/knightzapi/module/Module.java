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

package uk.knightz.knightzapi.module;


import java.util.jar.JarFile;

public abstract class Module implements IncomingRequestListener {

    /**
     * The id that a request should send in order for it to be interpreted by this module.
     * It should be overridden in an implementation ofGlobal the constructor, as it is null by default, causing the module to never be called
     */
    private final String requestID;
    private final String name;
    private final JarFile file;
    private final ModuleInfo info;

    protected Module(String requestID, String name, JarFile file, ModuleInfo info) {
        this.requestID = requestID;
        this.name = name;
        this.file = file;
        this.info = info;
    }

    public static Module forName(String name) {
        return ModuleManager.forName(name);
    }

    public JarFile getFile() {
        return file;
    }

    public ModuleInfo getInfo() {
        return info;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getName() {
        return name;
    }


}
