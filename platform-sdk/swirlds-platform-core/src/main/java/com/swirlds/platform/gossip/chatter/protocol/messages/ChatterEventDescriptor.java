/*
 * Copyright (C) 2016-2023 Hedera Hashgraph, LLC
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

package com.swirlds.platform.gossip.chatter.protocol.messages;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import com.swirlds.common.crypto.Hash;
import com.swirlds.common.io.streams.SerializableDataInputStream;
import com.swirlds.common.io.streams.SerializableDataOutputStream;
import com.swirlds.common.system.NodeId;
import com.swirlds.common.utility.CommonUtils;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.IOException;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A stripped down description of a chatter event.
 */
public class ChatterEventDescriptor implements EventDescriptor {

    public static final long CLASS_ID = 0x825e17f25c6e2566L;

    private static final class ClassVersion {
        public static final int ORIGINAL = 1;
        /**
         * The creator field is serialized as a self serializable node id.
         * @since 0.40.0
         */
        public static final int SELF_SERIALIZABLE_NODE_ID = 2;
    }

    private Hash hash;
    private NodeId creator;
    private long generation;

    private int hashCode;

    public ChatterEventDescriptor() {}

    /**
     * Create a new gossip event descriptor.
     *
     * @param hash       the hash of the event
     * @param creator    the creator of the event
     * @param generation the age of an event, smaller is older
     */
    public ChatterEventDescriptor(@NonNull final Hash hash, @NonNull final NodeId creator, final long generation) {
        this.hash = Objects.requireNonNull(hash, "hash must not be null");
        this.creator = Objects.requireNonNull(creator, "creator must not be null");
        this.generation = generation;

        hashCode = Objects.hash(hash, creator, generation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getClassId() {
        return CLASS_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final SerializableDataOutputStream out) throws IOException {
        out.writeSerializable(hash, false);
        out.writeSerializable(creator, false);
        out.writeLong(generation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deserialize(final SerializableDataInputStream in, final int version) throws IOException {
        hash = in.readSerializable(false, Hash::new);
        if (version < ClassVersion.SELF_SERIALIZABLE_NODE_ID) {
            creator = new NodeId(in.readLong());
        } else {
            creator = in.readSerializable(false, NodeId::new);
        }
        generation = in.readLong();

        hashCode = Objects.hash(hash, creator, generation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVersion() {
        return ClassVersion.SELF_SERIALIZABLE_NODE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinimumSupportedVersion() {
        return ClassVersion.ORIGINAL;
    }

    /**
     * {@inheritDoc}
     */
    public Hash getHash() {
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public NodeId getCreator() {
        return creator;
    }

    /**
     * {@inheritDoc}
     */
    public long getGeneration() {
        return generation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ChatterEventDescriptor that = (ChatterEventDescriptor) o;

        if (this.hashCode != that.hashCode) {
            return false;
        }

        return Objects.equals(creator, that.creator) && generation == that.generation && hash.equals(that.hash);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
                .append("creator", creator)
                .append("generation", generation)
                .append("hash", CommonUtils.hex(hash.getValue()))
                .toString();
    }
}
