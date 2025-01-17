/*
 * Copyright (C) 2023 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hedera.node.app.signature.impl;

import com.hedera.hapi.node.base.Key;
import com.hedera.hapi.node.base.KeyList;
import com.hedera.hapi.node.base.SignaturePair;
import com.hedera.hapi.node.base.ThresholdKey;
import com.hedera.node.app.AppTestBase;
import com.hedera.node.app.signature.ExpandedSignaturePair;
import com.hedera.node.app.spi.fixtures.Scenarios;
import com.hedera.pbj.runtime.io.buffer.Bytes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Measures the time it takes to expand {@link SignaturePair}s.
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ExpansionBenchmark extends AppTestBase implements Scenarios {
    @Param({"1", "2", "5", "10"})
    public int numSigPairs;

    @Param({"ED25519", "ECDSA_SECP256K1"})
    public String keyType;

    @Param({"10", "20", "30"})
    public int prefixLength;

    @Param({"key", "keyList", "thresholdKey"})
    public String scenario;

    private Key key;
    private SignatureExpanderImpl subject;
    private List<SignaturePair> sigPairs = new ArrayList<>(numSigPairs);

    @Setup(Level.Invocation)
    public void setUp() {
        key = createKey();
        fillSigPairs();
        subject = new SignatureExpanderImpl();
    }

    @Benchmark
    public void expandBench(Blackhole blackhole) {
        final var expanded = new HashSet<ExpandedSignaturePair>();
        subject.expand(key, sigPairs, expanded);
        blackhole.consume(expanded);
    }

    private Key createKey() {
        return switch (scenario) {
            case "key" -> createCryptographicKey();
            case "keyList" -> Key.newBuilder()
                    .keyList(KeyList.newBuilder()
                            .keys(createCryptographicKey(), createCryptographicKey(), createCryptographicKey()))
                    .build();
            case "thresholdKey" -> Key.newBuilder()
                    .thresholdKey(ThresholdKey.newBuilder()
                            .threshold(2)
                            .keys(KeyList.newBuilder()
                                    .keys(
                                            createCryptographicKey(),
                                            createCryptographicKey(),
                                            createCryptographicKey())))
                    .build();
            default -> throw new IllegalArgumentException("Unknown scenario: " + scenario);
        };
    }

    private void fillSigPairs() {
        // The sigPairs are preloaded with the all the cryptographic keys from the key.
        // If we have more pairs than we should, then trim them. If we have fewer than
        // we should, then add some (and shuffle them a few times to randomize).
        if (sigPairs.size() > numSigPairs) {
            sigPairs = sigPairs.subList(0, numSigPairs);
        }

        final var numToAdd = numSigPairs - sigPairs.size();
        for (int i = 0; i < numToAdd; i++) {
            createCryptographicKey(); // ignore the return value, side effect is to populate the list
        }

        final var rand = new Random(10893126253L);
        for (int i = 0; i < 3; i++) {
            Collections.shuffle(sigPairs, rand);
        }
    }

    private Key createCryptographicKey() {
        return switch (keyType) {
            case "ED25519" -> {
                final var keyBytes = randomBytes(32);
                sigPairs.add(SignaturePair.newBuilder()
                        .ed25519(keyBytes.slice(0, prefixLength))
                        .build());
                yield Key.newBuilder().ed25519(keyBytes).build();
            }
            case "ECDSA_SECP256K1" -> {
                final var bytes = randomByteArray(33);
                bytes[0] = 0x02;
                final var keyBytes = Bytes.wrap(bytes);
                sigPairs.add(SignaturePair.newBuilder()
                        .ecdsaSecp256k1(keyBytes.slice(0, prefixLength))
                        .build());
                yield Key.newBuilder().ecdsaSecp256k1(keyBytes).build();
            }
            default -> throw new AssertionError("Unknown key type: " + keyType);
        };
    }
}
