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

// https://www.kernel.org/doc/Documentation/cgroups/memory.txt
public class Memory extends SubSystem {

    private static final String MEMORY_STAT = "memory.stat";
    private static final String MEMORY_USAGE_IN_BYTES = "memory.usage_in_bytes";
    private static final String MEMORY_MEMSW_USAGE_IN_BYTES = "memory.memsw.usage_in_bytes";
    private static final String MEMORY_MAX_USAGE_IN_BYTES = "memory.max_usage_in_bytes";
    private static final String MEMORY_MEMSW_MAX_USAGE_IN_BYTES = "memory.memsw.max_usage_in_bytes";
    private static final String MEMORY_LIMIT_IN_BYTES = "memory.limit_in_bytes";
    private static final String MEMORY_MEMSW_LIMIT_IN_BYTES = "memory.memsw.limit_in_bytes";
    private static final String MEMORY_FAILCNT = "memory.failcnt";
    private static final String MEMORY_MEMSW_FAILCNT = "memory.memsw.failcnt";
    private static final String MEMORY_SOFT_LIMIT_IN_BYTES = "memory.soft_limit_in_bytes";
    private static final String MEMORY_FORCE_EMPTY = "memory.force_empty";
    private static final String MEMORY_SWAPPINESS = "memory.swappiness";
    private static final String MEMORY_MOVE_CHARGE_AT_IMMIGRATE = "memory.move_charge_at_immigrate";
    private static final String MEMORY_USE_HIERARCHY = "memory.use_hierarchy";
    private static final String MEMORY_OOM_CONTROL = "memory.oom_control";

    public Memory(Group group) {
        super(group);
    }

    @Override
    public SubSystemType getType() {
        return SubSystemType.memory;
    }

    public static class Stat {
        public final long cacheSize;
        public final long rssSize;
        public final long mappedFileSize;
        public final long pgpginNum;
        public final long pgpgoutNum;
        public final long swapSize;
        public final long activeAnonSize;
        public final long inactiveAnonSize;
        public final long activeFileSize;
        public final long inactiveFileSize;
        public final long unevictableSize;
        public final long hierarchicalMemoryLimitSize;
        public final long hierarchicalMemswLimitSize;
        public final long totalCacheSize;
        public final long totalRssSize;
        public final long totalMappedFileSize;
        public final long totalPgpginNum;
        public final long totalPgpgoutNum;
        public final long totalSwapSize;
        public final long totalActiveAnonSize;
        public final long totalInactiveAnonSize;
        public final long totalActiveFileSize;
        public final long totalInactiveFileSize;
        public final long totalUnevictableSize;
        public final long totalHierarchicalMemoryLimitSize;
        public final long totalHierarchicalMemswLimitSize;

        public Stat(String output) {
            String[] splits = output.split("\n");
            this.cacheSize = Long.parseLong(splits[0]);
            this.rssSize = Long.parseLong(splits[1]);
            this.mappedFileSize = Long.parseLong(splits[2]);
            this.pgpginNum = Long.parseLong(splits[3]);
            this.pgpgoutNum = Long.parseLong(splits[4]);
            this.swapSize = Long.parseLong(splits[5]);
            this.inactiveAnonSize = Long.parseLong(splits[6]);
            this.activeAnonSize = Long.parseLong(splits[7]);
            this.inactiveFileSize = Long.parseLong(splits[8]);
            this.activeFileSize = Long.parseLong(splits[9]);
            this.unevictableSize = Long.parseLong(splits[10]);
            this.hierarchicalMemoryLimitSize = Long.parseLong(splits[11]);
            this.hierarchicalMemswLimitSize = Long.parseLong(splits[12]);
            this.totalCacheSize = Long.parseLong(splits[13]);
            this.totalRssSize = Long.parseLong(splits[14]);
            this.totalMappedFileSize = Long.parseLong(splits[15]);
            this.totalPgpginNum = Long.parseLong(splits[16]);
            this.totalPgpgoutNum = Long.parseLong(splits[17]);
            this.totalSwapSize = Long.parseLong(splits[18]);
            this.totalInactiveAnonSize = Long.parseLong(splits[19]);
            this.totalActiveAnonSize = Long.parseLong(splits[20]);
            this.totalInactiveFileSize = Long.parseLong(splits[21]);
            this.totalActiveFileSize = Long.parseLong(splits[22]);
            this.totalUnevictableSize = Long.parseLong(splits[23]);
            this.totalHierarchicalMemoryLimitSize = Long.parseLong(splits[24]);
            this.totalHierarchicalMemswLimitSize = Long.parseLong(splits[25]);
        }
    }

    public Stat getStat() {
        String output = getStringParameter(MEMORY_STAT);
        Stat stat = new Stat(output);
        return stat;
    }

    public long getUsageInBytes() {
        return getLongParameter(MEMORY_USAGE_IN_BYTES);
    }

    public long getMemswUsageInBytes() {
        return getLongParameter(MEMORY_MEMSW_USAGE_IN_BYTES);
    }

    public long getMaxUsageInBytes() {
        return getLongParameter(MEMORY_MAX_USAGE_IN_BYTES);
    }

    public long getMemswMaxUsageInBytes() {
        return getLongParameter(MEMORY_MEMSW_MAX_USAGE_IN_BYTES);
    }

    public void setLimitInBytes(long v) {
        setParameter(MEMORY_LIMIT_IN_BYTES, v);
    }

    public long getLimitInBytes() {
        return getLongParameter(MEMORY_LIMIT_IN_BYTES);
    }

    public void setMemswLimitInBytes(long v) {
        setParameter(MEMORY_MEMSW_LIMIT_IN_BYTES, v);
    }

    public long getMemswLimitInBytes() {
        return getLongParameter(MEMORY_MEMSW_LIMIT_IN_BYTES);
    }

    public int getFailcnt() {
        return getIntParameter(MEMORY_FAILCNT);
    }

    public int getMemswFailcnt() {
        return getIntParameter(MEMORY_MEMSW_FAILCNT);
    }

    public void setSoftLimitInBytes(long v) {
        setParameter(MEMORY_SOFT_LIMIT_IN_BYTES, v);
    }

    public long getSoftLimitInBytes() {
        return getLongParameter(MEMORY_SOFT_LIMIT_IN_BYTES);
    }

    public void forceEmpty() {
        setParameter(MEMORY_FORCE_EMPTY, 0);
    }

    public void setSwappiness(int v) {
        setParameter(MEMORY_SWAPPINESS, v);
    }

    public int getSwappiness() {
        return getIntParameter(MEMORY_SWAPPINESS);
    }

    public void setMoveChargeAtImmigrate(int v) {
        setParameter(MEMORY_MOVE_CHARGE_AT_IMMIGRATE, v);
    }

    public boolean isMoveChargeAtImmigrate() {
        return getIntParameter(MEMORY_MOVE_CHARGE_AT_IMMIGRATE) > 0;
    }

    public void setUseHierarchy(boolean flag) {
        int v = flag ? 1 : 0;
        setParameter(MEMORY_USE_HIERARCHY, v);
    }

    public boolean isUseHierarchy() {
        return getIntParameter(MEMORY_USE_HIERARCHY) > 0;
    }

    public void setOomControl(boolean flag) {
        int v = flag ? 1 : 0;
        setParameter(MEMORY_OOM_CONTROL, v);
    }

    public boolean isOomControl() {
        String output = getStringParameter(MEMORY_OOM_CONTROL);
        output = output.split("\n")[0].split("[\\s]")[1];
        int value = Integer.parseInt(output);
        return value > 0;
    }

}
