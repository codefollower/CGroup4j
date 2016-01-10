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
    public void testSetReadBpsThrottle() {
        Blkio.Record excepted = new Blkio.Record(8, 0, null, 200);
        g1.getBlkio().setReadBpsThrottle(excepted.major, excepted.minor, (int) excepted.value);
        Blkio.Record actual = g1.getBlkio().getReadBpsThrottle()[0];
        assertEquals(actual, excepted);
    }

    @Test
    public void testSetWriteBpsThrottle() {
        Blkio.Record excepted = new Blkio.Record(8, 0, null, 200);
        g1.getBlkio().setWriteBpsThrottle(excepted.major, excepted.minor, (int) excepted.value);
        Blkio.Record actual = g1.getBlkio().getWriteBpsThrottle()[0];
        assertEquals(actual, excepted);
    }

    @Test
    public void testSetReadIopsThrottle() {
        Blkio.Record excepted = new Blkio.Record(8, 0, null, 200);
        g1.getBlkio().setReadIopsThrottle(excepted.major, excepted.minor, (int) excepted.value);
        Blkio.Record actual = g1.getBlkio().getReadIopsThrottle()[0];
        assertEquals(actual, excepted);
    }

    @Test
    public void testSetWriteIopsThrottle() {
        Blkio.Record excepted = new Blkio.Record(8, 0, null, 200);
        g1.getBlkio().setWriteIopsThrottle(excepted.major, excepted.minor, (int) excepted.value);
        Blkio.Record actual = g1.getBlkio().getWriteIopsThrottle()[0];
        assertEquals(actual, excepted);
    }

    @Test
    public void testGetIoQueueCountThrottle() {
        Blkio.Record[] records = root.getBlkio().getIoQueueCountThrottle();
        assertTrue(records.length > 0);
    }

    @Test
    public void testGetIoServiceCountThrottle() {
        Blkio.Record[] records = root.getBlkio().getIoServiceCountThrottle();
        assertTrue(records.length > 0);
    }

    @Test
    public void testGetIoServiceBytesThrottle() {
        Blkio.Record[] records = root.getBlkio().getIoServiceBytesThrottle();
        assertTrue(records.length > 0);
    }

    @Test
    public void testResetStats() {
        root.getBlkio().resetStats(0);
    }

    @Test
    public void testGetIoTime() {
        Blkio.Record[] records = root.getBlkio().getIoTime();
        assertTrue(records.length > 0);
    }

    @Test
    public void testGetSectors() {
        Blkio.Record[] records = root.getBlkio().getSectors();
        assertTrue(records.length > 0);
    }

    // TODO
    // @Test
    public void testGetAvgQueueSize() {
    }

    // TODO
    // @Test
    public void testGetGroupWaitTime() {
    }

    // TODO
    // @Test
    public void testGetEmptyTime() {
    }

    // TODO
    // @Test
    public void testGetIdleTime() {
    }

    // TODO
    // @Test
    public void testGetDequeueCount() {
    }

    @Test
    public void testGetIoServiceCount() {
        Blkio.Record[] records = root.getBlkio().getIoServiceCount();
        assertTrue(records.length > 0);
    }

    @Test
    public void testGetIoServiceBytes() {
        Blkio.Record[] records = root.getBlkio().getIoServiceBytes();
        assertTrue(records.length > 0);
    }

    @Test
    public void testGetIoServiceTime() {
        Blkio.Record[] records = root.getBlkio().getIoServiceTime();
        assertTrue(records.length > 0);
    }

    @Test
    public void testGetIoWaitTime() {
        Blkio.Record[] records = root.getBlkio().getIoWaitTime();
        assertTrue(records.length > 0);
    }

    @Test
    public void testGetIoMergeCount() {
        Blkio.Record[] records = root.getBlkio().getIoMergeCount();
        assertTrue(records.length > 0);
    }

    @Test
    public void testGetIoQueueCount() {
        Blkio.Record[] records = root.getBlkio().getIoQueueCount();
        assertTrue(records.length > 0);
    }

}
