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
package com.codahale.xsalsa20poly1305.benchmarks;

import java.util.concurrent.TimeUnit;
import org.abstractj.kalium.crypto.SecretBox;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class KaliumBenchmarks {

  private final SecretBox box = new SecretBox(new byte[32]);

  @Param({"100", "1024", "10240"})
  private int size = 100;

  private byte[] nonce = new byte[24];
  private byte[] plaintext = new byte[size];
  private byte[] ciphertext = box.encrypt(nonce, plaintext);

  @Benchmark
  public byte[] encrypt() {
    return box.encrypt(nonce, plaintext);
  }

  @Benchmark
  public byte[] decrypt() {
    return box.decrypt(nonce, ciphertext);
  }
}
