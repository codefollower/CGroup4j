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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BlkioTest extends SubSystemTest {

    public BlkioTest() {
        super(SubSystemType.blkio);
    }

    @Test
    public void run() {
        testThrottle();
        testWeight();
        testCommonParameters();
    }

    private void testThrottle() {
        Blkio blkio = g1.getBlkio();

        Blkio.Record excepted = new Blkio.Record(8, 0, null, 200);
        blkio.setThrottleReadBpsDevice(excepted.major, excepted.minor, excepted.value);
        Blkio.Record actual = blkio.getThrottleReadBpsDevice()[0];
        assertEquals(excepted, actual);

        blkio.setThrottleReadIopsDevice(excepted.major, excepted.minor, excepted.value);
        actual = blkio.getThrottleReadIopsDevice()[0];
        assertEquals(excepted, actual);

        blkio.setThrottleWriteBpsDevice(excepted.major, excepted.minor, excepted.value);
        actual = blkio.getThrottleWriteBpsDevice()[0];
        assertEquals(excepted, actual);

        blkio.setThrottleWriteIopsDevice(excepted.major, excepted.minor, excepted.value);
        actual = blkio.getThrottleWriteIopsDevice()[0];
        assertEquals(excepted, actual);

        Blkio.Record[] records = root.getBlkio().getThrottleIoServiced();
        assertTrue(records.length > 0);

        records = root.getBlkio().getThrottleIoServiceBytes();
        assertTrue(records.length > 0);
    }

    private void testWeight() {
        Blkio blkio = g1.getBlkio();
        blkio.setWeight(500);
        assertEquals(500, blkio.getWeight());

        // TODO
        // blkio.setWeightDevice(8, 0, 500);
        // assertEquals("8:0 500", blkio.getWeightDevice());
    }

    private void testCommonParameters() {
        Blkio blkio = root.getBlkio();

        Blkio.Record[] records = blkio.getIoServiced();
        assertTrue(records.length == 0);

        records = blkio.getIoServiceBytes();
        assertTrue(records.length == 0);

        records = blkio.getIoServiceTime();
        assertTrue(records.length == 0);

        records = blkio.getIoWaitTime();
        assertTrue(records.length == 0);

        records = blkio.getIoMerged();
        assertTrue(records.length == 0);

        records = blkio.getIoQueued();
        assertTrue(records.length == 0);

        blkio.resetStats(0);

        records = blkio.getIoTime();
        assertTrue(records.length == 0);

        records = blkio.getSectors();
        assertTrue(records.length == 0);

    }

    // TODO
    // @Test
    public void testDebugParameters() {
        Blkio blkio = g1.getBlkio();
        blkio.getAvgQueueSize();
        blkio.getGroupWaitTime();
        blkio.getEmptyTime();
        blkio.getIdleTime();
        blkio.getDequeue();
    }

}
