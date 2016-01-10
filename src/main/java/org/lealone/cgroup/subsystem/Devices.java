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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Devices extends SubSystem {

    private static final Logger LOG = LoggerFactory.getLogger(Devices.class);

    private static final String DEVICES_ALLOW = "devices.allow";
    private static final String DEVICES_DENY = "devices.deny";
    private static final String DEVICES_LIST = "devices.list";

    public static final char TYPE_ALL = 'a';
    public static final char TYPE_BLOCK = 'b';
    public static final char TYPE_CHAR = 'c';

    public static final int ACCESS_READ = 1;
    public static final int ACCESS_WRITE = 2;
    public static final int ACCESS_CREATE = 4;

    public static final char ACCESS_READ_CH = 'r';
    public static final char ACCESS_WRITE_CH = 'w';
    public static final char ACCESS_CREATE_CH = 'm';

    public Devices(Group group) {
        super(group);
    }

    @Override
    public SubSystemType getType() {
        return SubSystemType.devices;
    }

    public static class Record {

        char type;
        int major;
        int minor;
        int accesses;

        public Record(char type, int major, int minor, int accesses) {
            this.type = type;
            this.major = major;
            this.minor = minor;
            this.accesses = accesses;
        }

        public Record(String output) {
            if (output.contains("*")) {
                LOG.debug("Pre:" + output);
                output = output.replaceAll("\\*", "-1");
                LOG.debug("After:" + output);
            }
            String[] splits = output.split("[: ]");
            type = splits[0].charAt(0);
            major = Integer.parseInt(splits[1]);
            minor = Integer.parseInt(splits[2]);
            accesses = 0;
            for (char c : splits[3].toCharArray()) {
                if (c == ACCESS_READ_CH) {
                    accesses |= ACCESS_READ;
                }
                if (c == ACCESS_CREATE_CH) {
                    accesses |= ACCESS_CREATE;
                }
                if (c == ACCESS_WRITE_CH) {
                    accesses |= ACCESS_WRITE;
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(type);
            sb.append(' ');
            sb.append(major);
            sb.append(':');
            sb.append(minor);
            sb.append(' ');
            sb.append(getAccessesFlag(accesses));

            return sb.toString();
        }

        public static Record[] parseRecordList(String output) {
            String[] splits = output.split("/n");
            Record[] records = new Record[splits.length];
            for (int i = 0, l = splits.length; i < l; i++) {
                records[i] = new Record(splits[i]);
            }

            return records;
        }

        public static StringBuilder getAccessesFlag(int accesses) {
            StringBuilder sb = new StringBuilder();
            if ((accesses & ACCESS_READ) != 0) {
                sb.append(ACCESS_READ_CH);
            }
            if ((accesses & ACCESS_WRITE) != 0) {
                sb.append(ACCESS_WRITE_CH);
            }
            if ((accesses & ACCESS_CREATE) != 0) {
                sb.append(ACCESS_CREATE_CH);
            }
            return sb;
        }
    }

    private void setPermission(String p, char type, int major, int minor, int accesses) {
        Record record = new Record(type, major, minor, accesses);
        setParameter(p, record);
    }

    public void setAllow(char type, int major, int minor, int accesses) {
        setPermission(DEVICES_ALLOW, type, major, minor, accesses);
    }

    public void setDeny(char type, int major, int minor, int accesses) {
        setPermission(DEVICES_DENY, type, major, minor, accesses);
    }

    public Record[] getList() {
        String output = getStringParameter(DEVICES_LIST);
        return Record.parseRecordList(output);
    }

}
