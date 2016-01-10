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

public class NetCls extends SubSystem {

    private static final String NET_CLS_CLASSID = "net_cls.classid";

    public NetCls(Group group) {
        super(group);
    }

    @Override
    public SubSystemType getType() {
        return SubSystemType.net_cls;
    }

    private StringBuilder toHex(int num) {
        String hex = num + "";
        StringBuilder sb = new StringBuilder();
        int l = hex.length();
        if (l > 4) {
            hex = hex.substring(l - 4 - 1, l);
        }
        for (; l < 4; l++) {
            sb.append('0');
        }
        sb.append(hex);
        return sb;
    }

    public void setClassId(int major, int minor) throws IOException {
        StringBuilder sb = new StringBuilder("0x");
        sb.append(toHex(major));
        sb.append(toHex(minor));
        setParameter(NET_CLS_CLASSID, sb);
    }

    public int[] getClassId() throws IOException {
        String output = getStringParameter(NET_CLS_CLASSID);
        output = Integer.toHexString(Integer.parseInt(output));
        int major = Integer.parseInt(output.substring(0, output.length() - 4));
        int minor = Integer.parseInt(output.substring(output.length() - 4));
        int[] classId = { major, minor };
        return classId;
    }

}
