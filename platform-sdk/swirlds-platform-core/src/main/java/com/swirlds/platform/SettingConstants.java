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

package com.swirlds.platform;

import java.util.Set;

/**
 * @deprecated will be replaced by the {@link com.swirlds.config.api.Configuration} API in near future. If you need
 * 		to use this class please try to do as less static access as possible.
 */
@Deprecated(forRemoval = true)
public final class SettingConstants {

    /** name of the settings used file */
    static final String SETTING_USED_FILENAME = "settingsUsed.txt";

    static final String DATA_STRING = "data";
    static final String SAVED_STRING = "saved";
    static final int NUM_CRYPTO_THREADS_DEFAULT_VALUE = 32;
    static final int THROTTLE_TRANSACTION_QUEUE_SIZE_DEFAULT_VALUE = 100_000;
    static final int NUM_CONNECTIONS_DEFAULT_VALUE = 40;
    static final int MAX_OUTGOING_SYNCS_DEFAULT_VALUE = 2;
    static final int MAX_INCOMING_SYNCS_INC_DEFAULT_VALUE = 1;
    static final int BUFFER_SIZE_DEFAULT_VALUE = 8 * 1024;
    static final int SOCKET_IP_TOS_DEFAULT_VALUE = -1;
    static final boolean LOG_STACK_DEFAULT_VALUE = true;
    static final boolean USE_TLS_DEFAULT_VALUE = true;
    static final boolean DO_UPNP_DEFAULT_VALUE = true;
    static final boolean USE_LOOPBACK_IP_DEFAULT_VALUE = true;
    static final boolean TCP_NO_DELAY_DEFAULT_VALUE = true;
    static final int TIMEOUT_SYNC_CLIENT_SOCKET_DEFAULT_VALUE = 5_000;
    static final int TIMEOUT_SYNC_CLIENT_CONNECT_DEFAULT_VALUE = 5_000;
    static final int TIMEOUT_SERVER_ACCEPT_CONNECT_DEFAULT_VALUE = 5_000;
    static final int DEADLOCK_CHECK_PERIOD_DEFAULT_VALUE = 1000;
    static final int SLEEP_HEARTBEAT_DEFAULT_VALUE = 500;
    static final boolean VERIFY_EVENT_SIGS_DEFAULT_VALUE = true;
    static final boolean SHOW_INTERNAL_STATS_DEFAULT_VALUE = false;
    static final boolean VERBOSE_STATISTICS_DEFAULT_VALUE = false;
    static final int DELAY_SHUFFLE_DEFAULT_VALUE = 200;
    static final int CALLER_SKIPS_BEFORE_SLEEP_DEFAULT_VALUE = 30;
    static final int SLEEP_CALLER_SKIPS_DEFAULT_VALUE = 50;
    static final int STATS_BUFFER_SIZE_DEFAULT_VALUE = 100;
    static final int STATS_RECENT_SECONDS_DEFAULT_VALUE = 63;
    static final int STATS_SKIP_SECONDS_DEFAULT_VALUE = 60;
    static final int TRANSACTION_MAX_BYTES_DEFAULT_VALUES = 6144;
    static final int MAX_ADDRESS_SIZE_ALLOWED_DEFAULT_VALUE = 1024;
    static final int THREAD_PRIORITY_SYNC_DEFAULT_VALUE = Thread.NORM_PRIORITY;
    static final int THREAD_PRIORITY_NON_SYNC_DEFAULT_VALUE = Thread.NORM_PRIORITY;
    static final int FREEZE_SECONDS_AFTER_STARTUP_DEFAULT_VALUE = 10;
    static final boolean LOAD_KEYS_FROM_PFX_FILES_DEFAULT_VALUE = true;
    static final int MAX_TRANSACTION_BYTES_PER_EVENT_DEFAULT_VALUE = 245760;
    static final int MAX_TRANSACTION_COUNT_PER_EVENT_DEFAULT_VALUE = 245760;
    static final int THREAD_DUMP_PERIOD_MS_DEFAULT_VALUE = 0;
    static final String THREAD_DUMP_LOG_DIR_DEFAULT_VALUE = "data/threadDump";
    static final int JVM_PAUSE_DETECTOR_SLEEP_MS_DEFAULT_VALUE = 1000;
    static final int JVM_PAUSE_REPORT_MS_DEFAULT_VALUE = 1000;
    static final boolean GOSSIP_WITH_DIFFERENT_VERSIONS_DEFAULT_VALUE = false;

    static final Set<String> REMOVED_SETTINGS = Set.of(
            "reconnect.active",
            "reconnect.reconnectWindowSeconds",
            "reconnect.fallenBehindThreshold",
            "reconnect.asyncStreamTimeoutMilliseconds",
            "reconnect.asyncOutputStreamFlushMilliseconds",
            "reconnect.asyncStreamBufferSize",
            "reconnect.asyncStreams",
            "reconnect.maxAckDelayMilliseconds",
            "reconnect.maximumReconnectFailuresBeforeShutdown",
            "reconnect.minimumTimeBetweenReconnects",
            "chatter.useChatter",
            "chatter.attemptedChatterEventPerSecond",
            "chatter.chatteringCreationThreshold",
            "chatter.chatterIntakeThrottle",
            "chatter.otherEventDelay",
            "chatter.selfEventQueueCapacity",
            "chatter.otherEventQueueCapacity",
            "chatter.descriptorQueueCapacity",
            "chatter.processingTimeInterval",
            "chatter.heartbeatInterval",
            "chatter.futureGenerationLimit",
            "chatter.criticalQuorumSoftening",
            "chatter.sleepAfterFailedNegotiation",
            "fcHashMap.maximumGCQueueSize",
            "fcHashMap.gCQueueThresholdPeriod",
            "fcHashMap.archiveEnabled",
            "fcHashMap.rebuildSplitFactor",
            "fcHashMap.rebuildThreadCount",
            "jasperDb.maxNumOfKeys",
            "jasperDb.hashesRamToDiskThreshold",
            "jasperDb.mediumMergeCutoffMb",
            "jasperDb.smallMergeCutoffMb",
            "jasperDb.mergePeriodUnit",
            "jasperDb.maxNumberOfFilesInMerge",
            "jasperDb.minNumberOfFilesInMerge",
            "jasperDb.mergeActivatePeriod",
            "jasperDb.mediumMergePeriod",
            "jasperDb.fullMergePeriod",
            "jasperDb.maxDataFileBytes",
            "jasperDb.moveListChunkSize",
            "jasperDb.maxRamUsedForMergingGb",
            "jasperDb.iteratorInputBufferBytes",
            "jasperDb.writerOutputBufferBytes",
            "jasperDb.reconnectKeyLeakMitigationEnabled",
            "jasperDb.keySetBloomFilterHashCount",
            "jasperDb.keySetBloomFilterSizeInBytes",
            "jasperDb.keySetHalfDiskHashMapSize",
            "jasperDb.keySetHalfDiskHashMapBuffer",
            "jasperDb.indexRebuildingEnforced",
            "jasperDb.leafRecordCacheSize",
            "virtualMap.percentHashThreads",
            "virtualMap.numHashThreads",
            "virtualMap.percentCleanerThreads",
            "virtualMap.numCleanerThreads",
            "virtualMap.maximumVirtualMapSize",
            "virtualMap.virtualMapWarningThreshold",
            "virtualMap.virtualMapWarningInterval",
            "virtualMap.flushInterval",
            "virtualMap.copyFlushThreshold",
            "virtualMap.familyThrottleThreshold",
            "virtualMap.preferredFlushQueueSize",
            "virtualMap.flushThrottleStepSize",
            "virtualMap.maximumFlushThrottlePeriod",
            "state.savedStateDirectory",
            "state.mainClassNameOverride",
            "state.cleanSavedStateDirectory",
            "state.stateSavingQueueSize",
            "state.saveStatePeriod",
            "state.saveReconnectStateToDisk",
            "state.signedStateDisk",
            "state.dumpStateOnAnyISS",
            "state.dumpStateOnFatal",
            "state.haltOnAnyIss",
            "state.automatedSelfIssRecovery",
            "state.haltOnCatastrophicIss",
            "state.secondsBetweenISSDumps",
            "state.secondsBetweenIssLogs",
            "state.stateDeletionErrorLogFrequencySeconds",
            "state.enableHashStreamLogging",
            "state.debugHashDepth",
            "state.maxAgeOfFutureStateSignatures",
            "state.roundsToKeepForSigning",
            "state.roundsToKeepAfterSigning",
            "state.suspiciousSignedStateAge",
            "state.stateHistoryEnabled",
            "state.debugStackTracesEnabled",
            "state.requireStateLoad",
            "state.emergencyStateFileName",
            "state.checkSignedStateFromDisk",
            "signedStateFreq",
            "maxEventQueueForCons",
            "eventIntakeQueueThrottleSize",
            "eventIntakeQueueSize",
            "randomEventProbability",
            "staleEventPreventionThreshold",
            "rescueChildlessInverseProbability",
            "eventStreamQueueCapacity",
            "eventsLogPeriod",
            "eventsLogDir",
            "enableEventStreaming",
            "event.maxEventQueueForCons",
            "event.eventIntakeQueueThrottleSize",
            "event.eventIntakeQueueSize",
            "event.randomEventProbability",
            "event.staleEventPreventionThreshold",
            "event.rescueChildlessInverseProbability",
            "event.eventStreamQueueCapacity",
            "event.eventsLogPeriod",
            "event.eventsLogDir",
            "event.enableEventStreaming",
            "halfLife",
            "csvWriteFrequency",
            "csvOutputFolder",
            "csvFileName",
            "csvAppend",
            "prometheusEndpointEnabled",
            "prometheusEndpointPortNumber",
            "prometheusEndpointMaxBacklogAllowed",
            "disableMetricsOutput",
            "metrics.halfLife",
            "metrics.csvWriteFrequency",
            "metrics.csvOutputFolder",
            "metrics.csvFileName",
            "metrics.csvAppend",
            "metrics.disableMetricsOutput",
            "prometheus.endpointEnabled",
            "prometheus.endpointPortNumber",
            "prometheus.endpointMaxBacklogAllowed",
            "configPath",
            "settingsPath",
            "settingsUsedDir",
            "keysDirPath",
            "appsDirPath",
            "logPath",
            "paths.configPath",
            "paths.settingsPath",
            "paths.settingsUsedDir",
            "paths.keysDirPath",
            "paths.appsDirPath",
            "paths.logPath");

    private SettingConstants() {}
}
