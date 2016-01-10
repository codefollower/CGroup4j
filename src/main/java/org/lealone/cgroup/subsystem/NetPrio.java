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
import java.util.HashMap;
import java.util.Map;

import org.lealone.cgroup.Group;

public class NetPrio extends SubSystem {

    private static final String NET_PRIO_PRIOIDX = "net_prio.prioidx";
    private static final String NET_PRIO_IFPRIOMAP = "net_prio.ifpriomap";

    public NetPrio(Group group) {
        super(group);
    }

    @Override
    public SubSystemType getType() {
        return SubSystemType.net_prio;
    }

    public int getPrioId() throws IOException {
        return getIntParameter(NET_PRIO_PRIOIDX);
    }

    public Map<String, Integer> getIfPrioMap() throws IOException {
        String output = getStringParameter(NET_PRIO_IFPRIOMAP);
        String[] splits = output.split("\n");
        Map<String, Integer> ifPrioMap = new HashMap<String, Integer>();
        for (String split : splits) {
            String[] tmpSplits = split.split(" ");
            ifPrioMap.put(tmpSplits[0].trim(), Integer.parseInt(tmpSplits[1]));
        }
        return ifPrioMap;
    }

    public void addIfPrioMap(String iface, int priority) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(iface);
        sb.append(' ');
        sb.append(priority);
        setParameter(NET_PRIO_IFPRIOMAP, sb);
    }

}
