/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.cgroup.subsystem;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CpuTest extends SubSystemTest {

    public CpuTest() {
        super(SubSystemType.cpu);
    }

    @Test
    public void run() {
        Cpu cpu = g1.getCpu();
        cpu.setShares(100);
        assertEquals(100, cpu.getShares());

        cpu.setCfsPeriodUs(1000);
        assertEquals(1000, cpu.getCfsPeriodUs());

        cpu.setCfsQuotaUs(1000);
        assertEquals(1000, cpu.getCfsQuotaUs());

        cpu.setRtPeriodUs(100000);
        assertEquals(100000, cpu.getRtPeriodUs());

        cpu.setRtRuntimeUs(1000);
        assertEquals(1000, cpu.getRtRuntimeUs());

        Cpu.Stat stat = new Cpu.Stat("nr_periods 0\nnr_throttled 0\nthrottled_time 0");
        assertEquals(0, stat.nrPeriods);
        assertEquals(0, stat.nrThrottled);
        assertEquals(0, stat.throttledTime);

        stat = cpu.getStat();
        assertEquals(0, stat.nrPeriods);
        assertEquals(0, stat.nrThrottled);
        assertEquals(0, stat.throttledTime);

        // cpu.addTask(Threads.getThreadId());
    }

}
