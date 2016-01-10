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

import org.lealone.cgroup.Group;

public class Blkio extends SubSystem {

    private static final String BLKIO_THROTTLE_READ_BPS_DEVICE = "blkio.throttle.read_bps_device";
    private static final String BLKIO_THROTTLE_WRITE_BPS_DEVICE = "blkio.throttle.write_bps_device";
    private static final String BLKIO_THROTTLE_READ_IOPS_DEVICE = "blkio.throttle.read_iops_device";
    private static final String BLKIO_THROTTLE_WRITE_IOPS_DEVICE = "blkio.throttle.write_iops_device";
    private static final String BLKIO_THROTTLE_IO_SERIVICED = "blkio.throttle.io_serviced";
    private static final String BLKIO_THROTTLE_IO_SERIVICE_BYTES = "blkio.throttle.io_service_bytes";
    private static final String BLKIO_THROTTLE_IO_QUEUED = "blkio.throttle.io_queued";

    private static final String BLKIO_RESET_STATS = "blkio.reset_stats";
    private static final String BLKIO_TIME = "blkio.time";
    private static final String BLKIO_SECTORS = "blkio.sectors";
    private static final String BLKIO_AVG_QUEUE_SIZE = "blkio.avg_queue_size";
    private static final String BLKIO_GROUP_WAIT_TIME = "blkio.group_wait_time";
    private static final String BLKIO_EMPTY_TIME = "blkio.empty_time";
    private static final String BLKIO_IDLE_TIME = "blkio.idle_time";
    private static final String BLKIO_DEQUEUE = "blkio.dequeue";
    private static final String BLKIO_IO_SERIVICED = "blkio.io_serviced";
    private static final String BLKIO_IO_SERIVICE_BYTES = "blkio.io_service_bytes";
    private static final String BLKIO_IO_SERIVICE_TIME = "blkio.io_service_time";
    private static final String BLKIO_IO_WAIT_TIME = "blkio.io_wait_time";
    private static final String BLKIO_IO_MERGED = "blkio.io_merged";
    private static final String BLKIO_IO_QUEUED = "blkio.io_queued";

    public Blkio(Group group) {
        super(group);
    }

    @Override
    public SubSystemType getType() {
        return SubSystemType.blkio;
    }

    private void setThrottle(String prop, int major, int minor, int speed) {
        Record record = new Record(major, minor, null, speed);
        setParameter(prop, record);
    }

    private Record[] getThrottle(String prop) {
        return parseRecordList(prop);
    }

    public static class Record {
        int major;
        int minor;
        String operation;
        long value;

        public Record(int major, int minor, String operation, long value) {
            this.major = major;
            this.minor = minor;
            this.operation = operation;
            this.value = value;
        }

        public Record(String output) {
            String[] splits = output.split("[:\\s]");
            major = Integer.parseInt(splits[0]);
            minor = Integer.parseInt(splits[1]);
            if (splits.length > 3) {
                operation = splits[2];
                value = Long.parseLong(splits[3]);
            } else {
                value = Long.parseLong(splits[2]);
            }
        }

        private static Record[] parseRecordList(String output) {
            String[] splits = output.split("\n");
            // Skip Last Total Item.
            int len = splits.length;
            if (splits[splits.length - 1].trim().indexOf("Total") == 0) {
                len--;
            }
            Record[] records = new Record[len];
            for (int i = 0, l = len; i < l; i++) {
                records[i] = new Record(splits[i].trim());
            }

            return records;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(major);
            sb.append(':');
            sb.append(minor);
            if (operation != null) {
                sb.append(' ');
                sb.append(operation);
            }
            sb.append(' ');
            sb.append(value);

            return sb.toString();
        }
    }

    public void setReadBpsThrottle(int major, int minor, int speed) {
        setThrottle(BLKIO_THROTTLE_READ_BPS_DEVICE, major, minor, speed);
    }

    public Record[] getReadBpsThrottle() {
        return getThrottle(BLKIO_THROTTLE_READ_BPS_DEVICE);
    }

    public void setWriteBpsThrottle(int major, int minor, int speed) {
        setThrottle(BLKIO_THROTTLE_WRITE_BPS_DEVICE, major, minor, speed);
    }

    public Record[] getWriteBpsThrottle() {
        return getThrottle(BLKIO_THROTTLE_WRITE_BPS_DEVICE);
    }

    public void setReadIopsThrottle(int major, int minor, int speed) {
        setThrottle(BLKIO_THROTTLE_READ_IOPS_DEVICE, major, minor, speed);
    }

    public Record[] getReadIopsThrottle() {
        return getThrottle(BLKIO_THROTTLE_READ_IOPS_DEVICE);
    }

    public void setWriteIopsThrottle(int major, int minor, int speed) {
        setThrottle(BLKIO_THROTTLE_WRITE_IOPS_DEVICE, major, minor, speed);
    }

    public Record[] getWriteIopsThrottle() {
        return getThrottle(BLKIO_THROTTLE_WRITE_IOPS_DEVICE);
    }

    public Record[] getIoQueueCountThrottle() {
        return getThrottle(BLKIO_THROTTLE_IO_QUEUED);
    }

    public Record[] getIoServiceCountThrottle() {
        return getThrottle(BLKIO_THROTTLE_IO_SERIVICED);
    }

    public Record[] getIoServiceBytesThrottle() {
        return getThrottle(BLKIO_THROTTLE_IO_SERIVICE_BYTES);
    }

    public void resetStats(int v) {
        setParameter(BLKIO_RESET_STATS, v);
    }

    public Record[] getIoTime() {
        return parseRecordList(BLKIO_TIME);
    }

    public Record[] getSectors() {
        return parseRecordList(BLKIO_SECTORS);
    }

    public int getAvgQueueSize() {
        return getIntParameter(BLKIO_AVG_QUEUE_SIZE);
    }

    public long getGroupWaitTime() {
        return getLongParameter(BLKIO_GROUP_WAIT_TIME);
    }

    public long getEmptyTime() {
        return getLongParameter(BLKIO_EMPTY_TIME);
    }

    public long getIdleTime() {
        return getLongParameter(BLKIO_IDLE_TIME);
    }

    public Record getDequeueCount() {
        String output = getStringParameter(BLKIO_DEQUEUE);
        return new Record(output);
    }

    public Record[] getIoServiceCount() {
        return parseRecordList(BLKIO_IO_SERIVICED);
    }

    public Record[] getIoServiceBytes() {
        return parseRecordList(BLKIO_IO_SERIVICE_BYTES);
    }

    public Record[] getIoServiceTime() {
        return parseRecordList(BLKIO_IO_SERIVICE_TIME);
    }

    public Record[] getIoWaitTime() {
        return parseRecordList(BLKIO_IO_WAIT_TIME);
    }

    public Record[] getIoMergeCount() {
        return parseRecordList(BLKIO_IO_MERGED);
    }

    public Record[] getIoQueueCount() {
        return parseRecordList(BLKIO_IO_QUEUED);
    }

    private Record[] parseRecordList(String p) {
        String output = getStringParameter(p);
        return Record.parseRecordList(output);
    }

}
