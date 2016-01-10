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

import java.io.IOException;

import org.lealone.cgroup.Group;

public class Cpuacct extends SubSystem {

    private static final String CPUACCT_STAT = "cpuacct.stat";
    private static final String CPUACCT_USAGE = "cpuacct.usage";
    private static final String CPUACCT_USAGE_PERCPU = "cpuacct.usage_percpu";

    public Cpuacct(Group group) {
        super(group);
    }

    @Override
    public SubSystemType getType() {
        return SubSystemType.cpuacct;
    }

    public static class Stat {
        public final long userTime;
        public final long systemTime;

        public Stat(String statStr) {
            String[] splits = statStr.split("\n");
            this.userTime = Integer.parseInt(splits[0].split(" ")[1]);
            this.systemTime = Integer.parseInt(splits[1].split(" ")[1]);
        }
    }

    public Stat getStat() throws IOException {
        String output = getStringParameter(CPUACCT_STAT);
        Stat stat = new Stat(output);
        return stat;
    }

    public long getUsage() throws IOException {
        return getLongParameter(CPUACCT_USAGE);
    }

    public void resetUsage() throws IOException {
        setParameter(CPUACCT_USAGE, 0);
    }

    public long[] getUsagePerCpu() throws IOException {
        String[] outputs = getStringParameter(CPUACCT_USAGE_PERCPU).split(" ");
        long[] usages = new long[outputs.length];
        for (int i = 0, l = outputs.length; i < l; i++) {
            usages[i] = Long.parseLong(outputs[i]);
        }
        return usages;
    }

}
