/*
 * #%L
 * servo
 * %%
 * Copyright (C) 2011 - 2012 Netflix
 * %%
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
 * #L%
 */
package com.netflix.servo.publish.graphite;


import com.netflix.servo.Metric;
import com.netflix.servo.publish.JmxMetricPoller;
import com.netflix.servo.publish.LocalJmxConnector;
import com.netflix.servo.publish.MetricPoller;
import com.netflix.servo.publish.RegexMetricFilter;
import com.netflix.servo.tag.SortedTagList;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import java.util.List;
import java.util.regex.Pattern;

import static com.netflix.servo.publish.BasicMetricFilter.MATCH_NONE;
import static org.testng.Assert.assertEquals;

public class BasicGraphiteNamingConventionTest {

    @Test
    public void testJmxNaming() throws Exception{
        Metric m = getOSMetric("AvailableProcessors");

        GraphiteNamingConvention convention = new BasicGraphiteNamingConvention();
        String name = convention.getName(m);

        assertEquals(name, "java.lang.OperatingSystem.AvailableProcessors");
    }

    @Test
    public void testMetricNaming() throws Exception{
        Metric m = new Metric("simpleMonitor", SortedTagList.EMPTY, 0, 1000.0);

        GraphiteNamingConvention convention = new BasicGraphiteNamingConvention();
        String name = convention.getName(m);

        assertEquals(name, "simpleMonitor");
    }

    public static Metric getOSMetric(String name)  throws Exception{
        MetricPoller poller = new JmxMetricPoller(
                new LocalJmxConnector(),
                new ObjectName("java.lang:type=OperatingSystem"),
                MATCH_NONE);

        List<Metric> metrics = poller.poll(new RegexMetricFilter(null, Pattern.compile(name), false, false));
        assertEquals(metrics.size(), 1);
        return metrics.get(0);
    }
}
