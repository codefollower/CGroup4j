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

import java.util.LinkedList;

import org.lealone.cgroup.Group;

public class Cpuset extends SubSystem {

    private static final String CPUSET_CPUS = "cpuset.cpus";
    private static final String CPUSET_MEMS = "cpuset.mems";
    private static final String CPUSET_MEMORY_MIGRATE = "cpuset.memory_migrate";
    private static final String CPUSET_CPU_EXCLUSIVE = "cpuset.cpu_exclusive";
    private static final String CPUSET_MEM_EXCLUSIVE = "cpuset.mem_exclusive";
    private static final String CPUSET_MEM_HARDWALL = "cpuset.mem_hardwall";
    private static final String CPUSET_MEMORY_PRESSURE = "cpuset.memory_pressure";
    private static final String CPUSET_MEMORY_PRESSURE_ENABLED = "cpuset.memory_pressure_enabled";
    private static final String CPUSET_MEMORY_SPREAD_PAGE = "cpuset.memory_spread_page";
    private static final String CPUSET_MEMORY_SPREAD_SLAB = "cpuset.memory_spread_slab";
    private static final String CPUSET_SCHED_LOAD_BALANCE = "cpuset.sched_load_balance";
    private static final String CPUSET_SCHED_RELAX_DOMAIN_LEVEL = "cpuset.sched_relax_domain_level";

    public Cpuset(Group group) {
        super(group);
    }

    @Override
    public SubSystemType getType() {
        return SubSystemType.cpuset;
    }

    public void setCpus(int... nums) {
        StringBuilder sb = new StringBuilder();
        for (int num : nums) {
            sb.append(num);
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        setParameter(CPUSET_CPUS, sb);
    }

    public int[] getCpus() {
        String output = getStringParameter(CPUSET_CPUS);
        return parseNums(output);
    }

    public void setMems(int... nums) {
        StringBuilder sb = new StringBuilder();
        for (int num : nums) {
            sb.append(num);
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        setParameter(CPUSET_MEMS, sb);
    }

    public int[] getMems() {
        String output = getStringParameter(CPUSET_MEMS);
        return parseNums(output);
    }

    public void setMemMigrate(boolean flag) {
        int v = flag ? 1 : 0;
        setParameter(CPUSET_MEMORY_MIGRATE, v);
    }

    public boolean isMemMigrate() {
        return getIntParameter(CPUSET_MEMORY_MIGRATE) > 0;
    }

    public void setCpuExclusive(boolean flag) {
        int v = flag ? 1 : 0;
        setParameter(CPUSET_CPU_EXCLUSIVE, v);
    }

    public boolean isCpuExclusive() {
        return getIntParameter(CPUSET_CPU_EXCLUSIVE) > 0;
    }

    public void setMemExclusive(boolean flag) {
        int v = flag ? 1 : 0;
        setParameter(CPUSET_MEM_EXCLUSIVE, v);
    }

    public boolean isMemExclusive() {
        return getIntParameter(CPUSET_MEM_EXCLUSIVE) > 0;
    }

    public void setMemHardwall(boolean flag) {
        int v = flag ? 1 : 0;
        setParameter(CPUSET_MEM_HARDWALL, v);
    }

    public boolean isMemHardwall() {
        return getIntParameter(CPUSET_MEM_HARDWALL) > 0;
    }

    public int getMemPressure() {
        return getIntParameter(CPUSET_MEMORY_PRESSURE);
    }

    public void setMemPressureEnabled(boolean flag) {
        int v = flag ? 1 : 0;
        setParameter(CPUSET_MEMORY_PRESSURE_ENABLED, v);
    }

    public boolean isMemPressureEnabled() {
        return getIntParameter(CPUSET_MEMORY_PRESSURE_ENABLED) > 0;
    }

    public void setMemSpreadPage(boolean flag) {
        int v = flag ? 1 : 0;
        setParameter(CPUSET_MEMORY_SPREAD_PAGE, v);
    }

    public boolean isMemSpreadPage() {
        return getIntParameter(CPUSET_MEMORY_SPREAD_PAGE) > 0;
    }

    public void setMemSpreadSlab(boolean flag) {
        int v = flag ? 1 : 0;
        setParameter(CPUSET_MEMORY_SPREAD_SLAB, v);
    }

    public boolean isMemSpreadSlab() {
        return getIntParameter(CPUSET_MEMORY_SPREAD_SLAB) > 0;
    }

    public void setSchedLoadBlance(boolean flag) {
        int v = flag ? 1 : 0;
        setParameter(CPUSET_SCHED_LOAD_BALANCE, v);
    }

    public boolean isSchedLoadBlance() {
        return getIntParameter(CPUSET_SCHED_LOAD_BALANCE) > 0;
    }

    public void setSchedRelaxDomainLevel(int v) {
        setParameter(CPUSET_SCHED_RELAX_DOMAIN_LEVEL, v);
    }

    public int getSchedRelaxDomainLevel() {
        return getIntParameter(CPUSET_SCHED_RELAX_DOMAIN_LEVEL);
    }

    public static int[] parseNums(String outputStr) {
        char[] output = outputStr.toCharArray();
        LinkedList<Integer> numList = new LinkedList<Integer>();
        int value = 0;
        int start = 0;
        boolean isHyphen = false;
        for (char ch : output) {
            if (ch == ',') {
                if (isHyphen) {
                    for (; start <= value; start++) {
                        numList.add(start);
                    }
                    isHyphen = false;
                } else {
                    numList.add(value);
                }
                value = 0;
            } else if (ch == '-') {
                isHyphen = true;
                start = value;
                value = 0;
            } else {
                value = value * 10 + (ch - '0');
            }
        }
        if (output[output.length - 1] != ',') {
            if (isHyphen) {
                for (; start <= value; start++) {
                    numList.add(start);
                }
            } else {
                numList.add(value);
            }
        }

        int[] nums = new int[numList.size()];
        int index = 0;
        for (int num : numList) {
            nums[index] = num;
            index++;
        }

        return nums;
    }

}
