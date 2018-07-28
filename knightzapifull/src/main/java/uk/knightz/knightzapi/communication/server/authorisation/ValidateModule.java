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

package uk.knightz.knightzapi.communication.server.authorisation;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import uk.knightz.knightzapi.communication.WebModule;
import uk.knightz.knightzapi.communication.json.JSONMessage;
import uk.knightz.knightzapi.communication.rsa.RSA;
import uk.knightz.knightzapi.communication.server.Webserver;

import java.security.GeneralSecurityException;

/**
 * In charge of
 */
public class ValidateModule extends WebModule {


    public ValidateModule() {
        super("Validate", "/validate", Verb.GET);
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            return new Gson().toJson(new JSONMessage(201, RSA.savePublicKey(Webserver.getInstance().getPair().getPublic())));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return "Exception in decoding public key!";
    }
}
