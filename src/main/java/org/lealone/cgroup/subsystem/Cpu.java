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

public class Cpu extends SubSystem {

    private static final String CPU_STAT = "cpu.stat";
    private static final String CPU_SHARES = "cpu.shares";

    private static final String CPU_CFS_PERIOD_US = "cpu.cfs_period_us";
    private static final String CPU_CFS_QUOTA_US = "cpu.cfs_quota_us";
    private static final String CPU_RT_PERIOD_US = "cpu.rt_period_us";
    private static final String CPU_RT_RUNTIME_US = "cpu.rt_runtime_us";

    public Cpu(Group group) {
        super(group);
    }

    @Override
    public SubSystemType getType() {
        return SubSystemType.cpu;
    }

    public static class Stat {
        public final int nrPeriods;
        public final int nrThrottled;
        public final int throttledTime;

        public Stat(String statStr) {
            String[] splits = statStr.split("\n");
            this.nrPeriods = Integer.parseInt(splits[0].split(" ")[1]);
            this.nrThrottled = Integer.parseInt(splits[1].split(" ")[1]);
            this.throttledTime = Integer.parseInt(splits[2].split(" ")[1]);
        }
    }

    public Stat getStat() {
        String output = getStringParameter(CPU_STAT);
        Stat stat = new Stat(output);
        return stat;
    }

    public void setShares(int shares) {
        setParameter(CPU_SHARES, shares);
    }

    public int getShares() {
        return getIntParameter(CPU_SHARES);
    }

    public void setCfsPeriodUs(long v) {
        setParameter(CPU_CFS_PERIOD_US, v);
    }

    public long getCfsPeriodUs() {
        return getLongParameter(CPU_CFS_PERIOD_US);
    }

    public void setCfsQuotaUs(long v) {
        setParameter(CPU_CFS_QUOTA_US, v);
    }

    public long getCfsQuotaUs() {
        return getLongParameter(CPU_CFS_QUOTA_US);
    }

    public void setRtPeriodUs(long v) {
        setParameter(CPU_RT_PERIOD_US, v);
    }

    public long getRtPeriodUs() {
        return getLongParameter(CPU_RT_PERIOD_US);
    }

    public void setRtRuntimeUs(long v) {
        setParameter(CPU_RT_RUNTIME_US, v);
    }

    public long getRtRuntimeUs() {
        return getLongParameter(CPU_RT_RUNTIME_US);
    }

}
