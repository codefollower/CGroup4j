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

import java.io.IOException;

import org.junit.Test;

public class BlkioTest extends SubSystemTest {

    public BlkioTest() {
        super(SubSystemType.blkio);
    }

    @Test
    public void testSetReadBpsThrottle() {
        try {
            Blkio.Record excepted = new Blkio.Record(8, 0, null, 200);
            g1.getBlkio().setReadBpsThrottle(excepted.major, excepted.minor, (int) excepted.value);
            Blkio.Record actual = g1.getBlkio().getReadBpsThrottle()[0];
            assertEquals(actual, excepted);
        } catch (IOException e) {
            log.error("Set throttle.read_bps_device failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testSetWriteBpsThrottle() {
        try {
            Blkio.Record excepted = new Blkio.Record(8, 0, null, 200);
            g1.getBlkio().setWriteBpsThrottle(excepted.major, excepted.minor, (int) excepted.value);
            Blkio.Record actual = g1.getBlkio().getWriteBpsThrottle()[0];
            assertEquals(actual, excepted);
        } catch (IOException e) {
            log.error("Set throttle.write_bps_device failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testSetReadIopsThrottle() {
        try {
            Blkio.Record excepted = new Blkio.Record(8, 0, null, 200);
            g1.getBlkio().setReadIopsThrottle(excepted.major, excepted.minor, (int) excepted.value);
            Blkio.Record actual = g1.getBlkio().getReadIopsThrottle()[0];
            assertEquals(actual, excepted);
        } catch (IOException e) {
            log.error("Set throttle.read_bps_device failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testSetWriteIopsThrottle() {
        try {
            Blkio.Record excepted = new Blkio.Record(8, 0, null, 200);
            g1.getBlkio().setWriteIopsThrottle(excepted.major, excepted.minor, (int) excepted.value);
            Blkio.Record actual = g1.getBlkio().getWriteIopsThrottle()[0];
            assertEquals(actual, excepted);
        } catch (IOException e) {
            log.error("Set throttle.read_bps_device failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testGetIoQueueCountThrottle() {
        try {
            Blkio.Record[] records = root.getBlkio().getIoQueueCountThrottle();
            assertTrue(records.length > 0);
        } catch (IOException e) {
            log.error("Get throttle.io_queued failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testGetIoServiceCountThrottle() {
        try {
            Blkio.Record[] records = root.getBlkio().getIoServiceCountThrottle();
            assertTrue(records.length > 0);
        } catch (IOException e) {
            log.error("Get throttle.io_serviced failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testGetIoServiceBytesThrottle() {
        try {
            Blkio.Record[] records = root.getBlkio().getIoServiceBytesThrottle();
            assertTrue(records.length > 0);
        } catch (IOException e) {
            log.error("Get throttle.io_service_bytes failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testResetStats() {
        try {
            root.getBlkio().resetStats(0);
        } catch (IOException e) {
            log.error("Reset reset_stats failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testGetIoTime() {
        try {
            Blkio.Record[] records = root.getBlkio().getIoTime();
            assertTrue(records.length > 0);
        } catch (IOException e) {
            log.error("Get time failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testGetSectors() {
        try {
            Blkio.Record[] records = root.getBlkio().getSectors();
            assertTrue(records.length > 0);
        } catch (IOException e) {
            log.error("Get sectors failed.", e);
            assertTrue(false);
        }
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
        try {
            Blkio.Record[] records = root.getBlkio().getIoServiceCount();
            assertTrue(records.length > 0);
        } catch (IOException e) {
            log.error("Get io_serviced failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testGetIoServiceBytes() {
        try {
            Blkio.Record[] records = root.getBlkio().getIoServiceBytes();
            assertTrue(records.length > 0);
        } catch (IOException e) {
            log.error("Get io_service_bytes failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testGetIoServiceTime() {
        try {
            Blkio.Record[] records = root.getBlkio().getIoServiceTime();
            assertTrue(records.length > 0);
        } catch (IOException e) {
            log.error("Get io_service_time failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testGetIoWaitTime() {
        try {
            Blkio.Record[] records = root.getBlkio().getIoWaitTime();
            assertTrue(records.length > 0);
        } catch (IOException e) {
            log.error("Get io_wait_time failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testGetIoMergeCount() {
        try {
            Blkio.Record[] records = root.getBlkio().getIoMergeCount();
            assertTrue(records.length > 0);
        } catch (IOException e) {
            log.error("Get io_merged failed.", e);
            assertTrue(false);
        }
    }

    @Test
    public void testGetIoQueueCount() {
        try {
            Blkio.Record[] records = root.getBlkio().getIoQueueCount();
            assertTrue(records.length > 0);
        } catch (IOException e) {
            log.error("Get io_queued failed.", e);
            assertTrue(false);
        }
    }

}
