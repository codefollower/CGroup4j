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

public enum SubSystemType {

    blkio(1),
    cpu(2),
    cpuacct(4),
    cpuset(8),
    devices(16),
    freezer(32),
    memory(64),
    net_cls(128),
    net_prio(256);

    public final int value;

    private SubSystemType(int value) {
        this.value = value;
    }

    public static String getTypeNames(int subSystems) {
        StringBuilder buff = new StringBuilder();
        for (SubSystemType type : SubSystemType.values()) {
            if ((subSystems & type.value) != 0) {
                if (buff.length() != 0)
                    buff.append(',');
                buff.append(type.name());
            }
        }

        return buff.toString();
    }

}