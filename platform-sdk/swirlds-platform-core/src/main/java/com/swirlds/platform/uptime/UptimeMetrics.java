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

package com.swirlds.platform.uptime;

import com.swirlds.common.metrics.FunctionGauge;
import com.swirlds.common.metrics.Metrics;
import com.swirlds.common.metrics.RunningAverageMetric;
import com.swirlds.common.system.NodeId;
import com.swirlds.common.system.address.Address;
import com.swirlds.common.system.address.AddressBook;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Metrics that track node uptime.
 */
class UptimeMetrics {

    private static final String CATEGORY = "platform";

    private final Metrics metrics;

    /**
     * A map from node to the time since the last consensus event was observed from that node.
     */
    private final Map<NodeId, RunningAverageMetric> timeSinceLastConsensusEvent = new HashMap<>();

    /**
     * A map from node to the number of rounds since the last consensus event was observed from that node.
     */
    private final Map<NodeId, RunningAverageMetric> roundsSinceLastConsensusEvent = new HashMap<>();

    /**
     * A map from node to the time since the last judge was observed from that node.
     */
    private final Map<NodeId, RunningAverageMetric> timeSinceLastJudge = new HashMap<>();

    /**
     * A map from node to the number of rounds since the last judge was observed from that node.
     */
    private final Map<NodeId, RunningAverageMetric> roundsSinceLastJudge = new HashMap<>();

    private static final RunningAverageMetric.Config HEALTHY_NETWORK_FRACTION_CONFIG = new RunningAverageMetric.Config(
                    CATEGORY, "healthyNetworkFraction")
            .withUnit("fraction")
            .withDescription(
                    "The fraction (out of 1.0) of the network that is alive and healthy, weighted by consensus weight.");
    private final RunningAverageMetric healthyNetworkFraction;

    private static final RunningAverageMetric.Config UPTIME_COMPUTATION_TIME = new RunningAverageMetric.Config(
                    CATEGORY, "uptimeComputationTime")
            .withUnit("microseconds")
            .withDescription("The time, in microseconds, required to compute uptime information each round.");
    private final RunningAverageMetric uptimeComputationTime;

    private static final String TIME_SINCE_LAST_CONSENSUS_EVENT = "timeSinceLastConsensusEvent-";
    private static final String TIME_SINCE_LAST_JUDGE = "timeSinceLastJudge-";
    private static final String ROUNDS_SINCE_LAST_CONSENSUS_EVENT = "roundsSinceLastConsensusEvent-";
    private static final String ROUNDS_SINCE_LAST_JUDGE = "roundsSinceLastJudge-";

    /**
     * Construct a new uptime metrics object.
     *
     * @param metrics     the metrics for this node
     * @param addressBook the address book
     * @param isDegraded  a supplier that returns true if this node is degraded, false otherwise
     */
    public UptimeMetrics(
            @NonNull final Metrics metrics,
            @NonNull final AddressBook addressBook,
            @NonNull final Supplier<Boolean> isDegraded) {

        this.metrics = Objects.requireNonNull(metrics);
        Objects.requireNonNull(addressBook);
        Objects.requireNonNull(isDegraded);

        healthyNetworkFraction = metrics.getOrCreate(HEALTHY_NETWORK_FRACTION_CONFIG);

        final FunctionGauge.Config<Boolean> degradedConfig = new FunctionGauge.Config<>(
                        CATEGORY, "degraded", Boolean.class, isDegraded)
                .withUnit("boolean")
                .withDescription("False if this node is healthy, true if this node is degraded.");
        metrics.getOrCreate(degradedConfig);

        uptimeComputationTime = metrics.getOrCreate(UPTIME_COMPUTATION_TIME);

        for (final Address address : addressBook) {
            addMetricsForNode(address.getNodeId());
        }
    }

    /**
     * Add the metrics for a node.
     *
     * @param nodeId the id of the node
     */
    public void addMetricsForNode(@NonNull final NodeId nodeId) {
        Objects.requireNonNull(nodeId, "nodeId must not be null");
        final RunningAverageMetric.Config timeSinceLastConensusEventConfig = new RunningAverageMetric.Config(
                        CATEGORY, TIME_SINCE_LAST_CONSENSUS_EVENT + nodeId)
                .withUnit("seconds")
                .withDescription("The consensus time in seconds since the "
                        + "last consensus event created by this node was observed");
        timeSinceLastConsensusEvent.put(nodeId, metrics.getOrCreate(timeSinceLastConensusEventConfig));

        final RunningAverageMetric.Config roundsSinceLastConensusEventConfig = new RunningAverageMetric.Config(
                        CATEGORY, ROUNDS_SINCE_LAST_CONSENSUS_EVENT + nodeId)
                .withUnit("rounds")
                .withDescription(
                        "The number of rounds since the last consensus event created by this node was observed");
        roundsSinceLastConsensusEvent.put(nodeId, metrics.getOrCreate(roundsSinceLastConensusEventConfig));

        final RunningAverageMetric.Config timeSinceLastJudgeConfig = new RunningAverageMetric.Config(
                        CATEGORY, TIME_SINCE_LAST_JUDGE + nodeId)
                .withUnit("seconds")
                .withDescription(
                        "The consensus time in seconds since the last judge created by this node was observed");
        timeSinceLastJudge.put(nodeId, metrics.getOrCreate(timeSinceLastJudgeConfig));

        final RunningAverageMetric.Config roundsSinceLastJudgeConfig = new RunningAverageMetric.Config(
                        CATEGORY, ROUNDS_SINCE_LAST_JUDGE + nodeId)
                .withUnit("rounds")
                .withDescription("The number of rounds since the last judge created by this node was observed");
        roundsSinceLastJudge.put(nodeId, metrics.getOrCreate(roundsSinceLastJudgeConfig));
    }

    /**
     * Remove the metrics for a node.
     *
     * @param nodeId the id of the node
     */
    public void removeMetricsForNode(@NonNull final NodeId nodeId) {
        Objects.requireNonNull(nodeId, "nodeId must not be null");
        timeSinceLastConsensusEvent.remove(nodeId);
        metrics.remove(new RunningAverageMetric.Config(CATEGORY, TIME_SINCE_LAST_CONSENSUS_EVENT + nodeId));

        roundsSinceLastConsensusEvent.remove(nodeId);
        metrics.remove(new RunningAverageMetric.Config(CATEGORY, ROUNDS_SINCE_LAST_CONSENSUS_EVENT + nodeId));

        timeSinceLastJudge.remove(nodeId);
        metrics.remove(new RunningAverageMetric.Config(CATEGORY, TIME_SINCE_LAST_JUDGE + nodeId));

        roundsSinceLastJudge.remove(nodeId);
        metrics.remove(new RunningAverageMetric.Config(CATEGORY, ROUNDS_SINCE_LAST_JUDGE + nodeId));
    }

    /**
     * Get the metric that tracks the time since the last consensus event was observed from a node.
     *
     * @param id the id of the node
     * @return the metric
     * @throws NoSuchElementException if no metric for the node is found
     */
    public @NonNull RunningAverageMetric getTimeSinceLastConsensusEventMetric(@NonNull final NodeId id) {
        Objects.requireNonNull(id, "id must not be null");
        final RunningAverageMetric metric = timeSinceLastConsensusEvent.get(id);
        if (metric == null) {
            throw new NoSuchElementException("No metric for node " + id + " found.");
        }
        return metric;
    }

    /**
     * Get the metric that tracks the number of rounds since the last consensus event was observed from a node.
     *
     * @param id the id of the node
     * @return the metric
     * @throws NoSuchElementException if no metric for the node is found
     */
    public @NonNull RunningAverageMetric getRoundsSinceLastConsensusEventMetric(@NonNull final NodeId id) {
        Objects.requireNonNull(id, "id must not be null");
        final RunningAverageMetric metric = roundsSinceLastConsensusEvent.get(id);
        if (metric == null) {
            throw new NoSuchElementException("No metric for node " + id + " found.");
        }
        return metric;
    }

    /**
     * Get the metric that tracks the time since the last judge was observed from a node.
     *
     * @param id the id of the node
     * @return the metric
     * @throws NoSuchElementException if no metric for the node is found
     */
    public @NonNull RunningAverageMetric getTimeSinceLastJudgeMetric(@NonNull final NodeId id) {
        Objects.requireNonNull(id, "id must not be null");
        final RunningAverageMetric metric = timeSinceLastJudge.get(id);
        if (metric == null) {
            throw new NoSuchElementException("No metric for node " + id + " found.");
        }
        return metric;
    }

    /**
     * Get the metric that tracks the number of rounds since the last judge was observed from a node.
     *
     * @param id the id of the node
     * @return the metric
     * @throws NoSuchElementException if no metric for the node is found
     */
    public @NonNull RunningAverageMetric getRoundsSinceLastJudgeMetric(@NonNull final NodeId id) {
        Objects.requireNonNull(id, "id must not be null");
        final RunningAverageMetric metric = roundsSinceLastJudge.get(id);
        if (metric == null) {
            throw new NoSuchElementException("No metric for node " + id + " found.");
        }
        return metric;
    }

    /**
     * Get the metric that tracks the fraction of the network that is alive.
     *
     * @return the metric
     */
    public @NonNull RunningAverageMetric getHealthyNetworkFraction() {
        return healthyNetworkFraction;
    }

    /**
     * Get the metric that tracks the time required to compute uptime information each round.
     *
     * @return the metric
     */
    public @NonNull RunningAverageMetric getUptimeComputationTimeMetric() {
        return uptimeComputationTime;
    }
}
