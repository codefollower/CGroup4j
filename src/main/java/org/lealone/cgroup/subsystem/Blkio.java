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

// https://www.kernel.org/doc/Documentation/cgroups/blkio-controller.txt
public class Blkio extends SubSystem {

    // 总共22个
    private static final String BLKIO_THROTTLE_IO_SERIVICE_BYTES = "blkio.throttle.io_service_bytes";
    private static final String BLKIO_THROTTLE_IO_SERIVICED = "blkio.throttle.io_serviced";
    private static final String BLKIO_THROTTLE_READ_BPS_DEVICE = "blkio.throttle.read_bps_device";
    private static final String BLKIO_THROTTLE_READ_IOPS_DEVICE = "blkio.throttle.read_iops_device";
    private static final String BLKIO_THROTTLE_WRITE_BPS_DEVICE = "blkio.throttle.write_bps_device";
    private static final String BLKIO_THROTTLE_WRITE_IOPS_DEVICE = "blkio.throttle.write_iops_device";

    private static final String BLKIO_WEIGHT = "blkio.weight";
    private static final String BLKIO_WEIGHT_DEVICE = "blkio.weight_device";

    private static final String BLKIO_IO_MERGED = "blkio.io_merged";
    private static final String BLKIO_IO_QUEUED = "blkio.io_queued";
    private static final String BLKIO_IO_SERIVICE_BYTES = "blkio.io_service_bytes";
    private static final String BLKIO_IO_SERIVICED = "blkio.io_serviced";
    private static final String BLKIO_IO_SERIVICE_TIME = "blkio.io_service_time";
    private static final String BLKIO_IO_WAIT_TIME = "blkio.io_wait_time";

    private static final String BLKIO_TIME = "blkio.time";
    private static final String BLKIO_RESET_STATS = "blkio.reset_stats";
    private static final String BLKIO_SECTORS = "blkio.sectors";

    // 下面5个设置CONFIG_DEBUG_BLK_CGROUP=y时才能见到
    private static final String BLKIO_AVG_QUEUE_SIZE = "blkio.avg_queue_size";
    private static final String BLKIO_GROUP_WAIT_TIME = "blkio.group_wait_time";
    private static final String BLKIO_EMPTY_TIME = "blkio.empty_time";
    private static final String BLKIO_IDLE_TIME = "blkio.idle_time";
    private static final String BLKIO_DEQUEUE = "blkio.dequeue";

    public Blkio(Group group) {
        super(group);
    }

    @Override
    public SubSystemType getType() {
        return SubSystemType.blkio;
    }

    public static class Record {
        public final int major;
        public final int minor;
        public final String operation;
        public final long value;

        public Record(int major, int minor, String operation, long value) {
            this.major = major;
            this.minor = minor;
            this.operation = operation;
            this.value = value;
        }

        public Record(String output) {
            String[] splits = output.split("[:\\s]");
            if (splits.length == 2) { // blkio.io_merged or blkio.io_queued
                major = -1;
                minor = -1;
                operation = splits[1];
                value = Long.parseLong(splits[0]);
            } else {
                major = Integer.parseInt(splits[0]);
                minor = Integer.parseInt(splits[1]);
                if (splits.length > 3) {
                    operation = splits[2];
                    value = Long.parseLong(splits[3]);
                } else {
                    operation = null;
                    value = Long.parseLong(splits[2]);
                }
            }
        }

        private static Record[] parseRecordList(String output) {
            output = output.trim();
            if (output.isEmpty())
                return new Record[0];
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

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + major;
            result = prime * result + minor;
            result = prime * result + ((operation == null) ? 0 : operation.hashCode());
            result = prime * result + (int) (value ^ (value >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Record other = (Record) obj;
            if (major != other.major)
                return false;
            if (minor != other.minor)
                return false;
            if (operation == null) {
                if (other.operation != null)
                    return false;
            } else if (!operation.equals(other.operation))
                return false;
            if (value != other.value)
                return false;
            return true;
        }

    }

    private void setThrottle(String p, int major, int minor, long v) {
        Record record = new Record(major, minor, null, v);
        setParameter(p, record);
    }

    private Record[] getThrottle(String p) {
        return parseRecordList(p);
    }

    private Record[] parseRecordList(String p) {
        String output = getStringParameter(p);
        return Record.parseRecordList(output);
    }

    public void setThrottleReadBpsDevice(int major, int minor, long bps) {
        setThrottle(BLKIO_THROTTLE_READ_BPS_DEVICE, major, minor, bps);
    }

    public Record[] getThrottleReadBpsDevice() {
        return getThrottle(BLKIO_THROTTLE_READ_BPS_DEVICE);
    }

    public void setThrottleReadIopsDevice(int major, int minor, long iops) {
        setThrottle(BLKIO_THROTTLE_READ_IOPS_DEVICE, major, minor, iops);
    }

    public Record[] getThrottleReadIopsDevice() {
        return getThrottle(BLKIO_THROTTLE_READ_IOPS_DEVICE);
    }

    public void setThrottleWriteBpsDevice(int major, int minor, long bps) {
        setThrottle(BLKIO_THROTTLE_WRITE_BPS_DEVICE, major, minor, bps);
    }

    public Record[] getThrottleWriteBpsDevice() {
        return getThrottle(BLKIO_THROTTLE_WRITE_BPS_DEVICE);
    }

    public void setThrottleWriteIopsDevice(int major, int minor, long iops) {
        setThrottle(BLKIO_THROTTLE_WRITE_IOPS_DEVICE, major, minor, iops);
    }

    public Record[] getThrottleWriteIopsDevice() {
        return getThrottle(BLKIO_THROTTLE_WRITE_IOPS_DEVICE);
    }

    public Record[] getThrottleIoServiced() {
        return getThrottle(BLKIO_THROTTLE_IO_SERIVICED);
    }

    public Record[] getThrottleIoServiceBytes() {
        return getThrottle(BLKIO_THROTTLE_IO_SERIVICE_BYTES);
    }

    public void setWeight(int v) {
        setParameter(BLKIO_WEIGHT, v);
    }

    public int getWeight() {
        return getIntParameter(BLKIO_WEIGHT);
    }

    public void setWeightDevice(int major, int minor, int weight) {
        StringBuilder s = new StringBuilder();
        s.append(major).append(':').append(minor).append(' ').append(weight);
        setParameter(BLKIO_WEIGHT_DEVICE, s);
    }

    public String getWeightDevice() {
        return getStringParameter(BLKIO_WEIGHT_DEVICE);
    }

    public Record[] getIoServiced() {
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

    public Record[] getIoMerged() {
        return parseRecordList(BLKIO_IO_MERGED);
    }

    public Record[] getIoQueued() {
        return parseRecordList(BLKIO_IO_QUEUED);
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

    public Record[] getDequeue() {
        return parseRecordList(BLKIO_DEQUEUE);
    }

}
