/*
 * Copyright (c) 2015 The Jupiter Project
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jupiter.rpc.metric;

import com.codahale.metrics.*;

import java.io.File;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.jupiter.common.util.JConstants.*;
import static org.jupiter.common.util.Preconditions.checkNotNull;

/**
 * 指标度量
 *
 * jupiter
 * org.jupiter.rpc.metric
 *
 * @author jiachun.fjc
 */
public class Metrics {

    private static final MetricRegistry metricRegistry = new MetricRegistry();
    private static final ScheduledReporter scheduledReporter;
    static {
        if (METRIC_CSV_REPORTER) {
            scheduledReporter = CsvReporter.forRegistry(metricRegistry).build(new File(METRIC_CSV_REPORTER_DIRECTORY));
        } else {
            ScheduledReporter _reporter;
            try {
                _reporter = Slf4jReporter.forRegistry(metricRegistry)
                                            .withLoggingLevel(Slf4jReporter.LoggingLevel.WARN)
                                            .build();
            } catch (NoClassDefFoundError e) {
                // No Slf4j
                _reporter = ConsoleReporter.forRegistry(metricRegistry).build();
            }
            scheduledReporter = _reporter;
        }
        scheduledReporter.start(METRIC_REPORT_PERIOD, MINUTES);
    }

    /**
     * Return the global registry of metric instances.
     */
    public static MetricRegistry metricRegistry() {
        return metricRegistry;
    }

    /**
     * Return the {@link Meter} registered under this name; or create and register
     * a new {@link Meter} if none is registered.
     */
    public static Meter meter(String name) {
        return metricRegistry.meter(checkNotNull(name, "name"));
    }

    /**
     * Return the {@link Meter} registered under this name; or create and register
     * a new {@link Meter} if none is registered.
     */
    public static Meter meter(Class<?> clazz, String... names) {
        return metricRegistry.meter(MetricRegistry.name(clazz, names));
    }

    /**
     * Return the {@link Timer} registered under this name; or create and register
     * a new {@link Timer} if none is registered.
     */
    public static Timer timer(String name) {
        return metricRegistry.timer(checkNotNull(name, "name"));
    }

    /**
     * Return the {@link Timer} registered under this name; or create and register
     * a new {@link Timer} if none is registered.
     */
    public static Timer timer(Class<?> clazz, String... names) {
        return metricRegistry.timer(MetricRegistry.name(clazz, names));
    }

    /**
     * Return the {@link Counter} registered under this name; or create and register
     * a new {@link Counter} if none is registered.
     */
    public static Counter counter(String name) {
        return metricRegistry.counter(checkNotNull(name, "name"));
    }

    /**
     * Return the {@link Counter} registered under this name; or create and register
     * a new {@link Counter} if none is registered.
     */
    public static Counter counter(Class<?> clazz, String... names) {
        return metricRegistry.counter(MetricRegistry.name(clazz, names));
    }

    /**
     * Return the {@link Histogram} registered under this name; or create and register
     * a new {@link Histogram} if none is registered.
     */
    public static Histogram histogram(String name) {
        return metricRegistry.histogram(checkNotNull(name, "name"));
    }

    /**
     * Return the {@link Histogram} registered under this name; or create and register
     * a new {@link Histogram} if none is registered.
     */
    public static Histogram histogram(Class<?> clazz, String... names) {
        return metricRegistry.histogram(MetricRegistry.name(clazz, names));
    }

    private Metrics() {}
}
