/*
 * Copyright © 2017 Coda Hale (coda.hale@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codahale.xsalsa20poly1305;

import java.security.SecureRandom;
import okio.ByteString;
import org.bouncycastle.math.ec.rfc7748.X25519;

/** Utility methods for generating XSalsa20Poly1305 keys. */
public class Keys {
  private Keys() {
    // singleton
  }

  /**
   * Generates a 32-byte secret key.
   *
   * @return a 32-byte secret key
   */
  public static ByteString generateSecretKey() {
    final byte[] k = new byte[32];
    final SecureRandom random = new SecureRandom();
    random.nextBytes(k);
    return ByteString.of(k);
  }

  /**
   * generates a Curve25519 private key.
   *
   * @return a Curve25519 private key
   */
  public static ByteString generatePrivateKey() {
    final byte[] k = generateSecretKey().toByteArray();
    k[0] &= (byte) 248;
    k[31] &= (byte) 127;
    k[31] |= (byte) 64;
    return ByteString.of(k);
  }

  /**
   * Generates a Curve25519 public key given a Curve25519 private key.
   *
   * @param privateKey a Curve25519 private key
   * @return the public key matching {@code privateKey}
   */
  public static ByteString generatePublicKey(ByteString privateKey) {
    final byte[] publicKey = new byte[32];
    X25519.scalarMultBase(privateKey.toByteArray(), 0, publicKey, 0);
    return ByteString.of(publicKey);
  }

  /**
   * Calculate the X25519/HSalsa20 shared secret for the given public key and private key.
   *
   * @param publicKey the recipient's public key
   * @param privateKey the sender's private key
   * @return a 32-byte secret key only re-calculable by the sender and recipient
   */
  public static ByteString sharedSecret(ByteString publicKey, ByteString privateKey) {
    final byte[] s = new byte[32];
    X25519.scalarMult(privateKey.toByteArray(), 0, publicKey.toByteArray(), 0, s, 0);
    final byte[] k = new byte[32];
    HSalsa20.hsalsa20(k, new byte[16], s);
    return ByteString.of(k);
  }
}