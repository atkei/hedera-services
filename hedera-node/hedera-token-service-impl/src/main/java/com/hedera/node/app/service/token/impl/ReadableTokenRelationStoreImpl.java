/*
 * Copyright (C) 2022-2023 Hedera Hashgraph, LLC
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

package com.hedera.node.app.service.token.impl;

import static java.util.Objects.requireNonNull;

import com.hedera.hapi.node.base.AccountID;
import com.hedera.hapi.node.base.TokenID;
import com.hedera.hapi.node.state.token.TokenRelation;
import com.hedera.node.app.service.mono.utils.EntityNumPair;
import com.hedera.node.app.service.token.ReadableTokenRelationStore;
import com.hedera.node.app.spi.state.ReadableKVState;
import com.hedera.node.app.spi.state.ReadableStates;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Provides read-only methods for getting underlying data for working with TokenRelations.
 *
 * <p>This class is not exported from the module. It is an internal implementation detail.
 */
public class ReadableTokenRelationStoreImpl implements ReadableTokenRelationStore {
    /** The underlying data storage class that holds the token data. */
    private final ReadableKVState<EntityNumPair, TokenRelation> readableTokenRelState;

    /**
     * Create a new {@link ReadableTokenRelationStoreImpl} instance.
     *
     * @param states The state to use.
     */
    public ReadableTokenRelationStoreImpl(@NonNull final ReadableStates states) {
        this.readableTokenRelState = requireNonNull(states).get(TokenServiceImpl.TOKEN_RELS_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public TokenRelation get(@NonNull final AccountID accountId, @NonNull final TokenID tokenId) {
        requireNonNull(accountId);
        requireNonNull(tokenId);

        if (AccountID.DEFAULT.equals(accountId) || TokenID.DEFAULT.equals(tokenId)) return null;

        return readableTokenRelState.get(EntityNumPair.fromLongs(accountId.accountNum(), tokenId.tokenNum()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long sizeOfState() {
        return readableTokenRelState.size();
    }
}
