
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

package uk.knightz.knightzapi.communication.rsa;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Controls RSA key IO operations
 **/
public class RSAIO {
	/**
	 * Saves the key pair to disk.
	 *
	 * @param directory
	 *            The directory to save to
	 * @param keyPair
	 *            The key pair to save
	 * @throws Exception
	 *             If an error occurs saving the key pair
	 */
	public static void save(File directory, KeyPair keyPair) throws Exception {
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		// Store the public key.
		X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		FileOutputStream out = new FileOutputStream(directory + "/public.key");
		out.write(DatatypeConverter.printBase64Binary(publicSpec.getEncoded())
				.getBytes());
		out.close();

		// Store the private key.
		PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		out = new FileOutputStream(directory + "/private.key");
		out.write(DatatypeConverter.printBase64Binary(privateSpec.getEncoded())
				.getBytes());
		out.close();
	}
	/**
	 * Loads an RSA key pair from a directory. The directory must contain the files
	 * "public.key" and "private.key".
	 *
	 * @param directory
	 *            The directory to load from
	 * @return The key pair
	 * @throws Exception If an error occurs loading in the files
	 */
	public static KeyPair load(File directory) throws Exception {
		// Read the public key file.
		File publicKeyFile = new File(directory + "/public.key");
		FileInputStream in = new FileInputStream(directory + "/public.key");
		byte[] encodedPublicKey = new byte[(int) publicKeyFile.length()];
		in.read(encodedPublicKey);
		encodedPublicKey = DatatypeConverter.parseBase64Binary(new String(
				encodedPublicKey));
		in.close();

		// Read the private key file.
		File privateKeyFile = new File(directory + "/private.key");
		in = new FileInputStream(directory + "/private.key");
		byte[] encodedPrivateKey = new byte[(int) privateKeyFile.length()];
		in.read(encodedPrivateKey);
		encodedPrivateKey = DatatypeConverter.parseBase64Binary(new String(
				encodedPrivateKey));
		in.close();

		// Instantiate and return the key pair.
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		return new KeyPair(publicKey, privateKey);
	}
}
