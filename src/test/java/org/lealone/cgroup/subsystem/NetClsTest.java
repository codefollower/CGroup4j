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

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;

import org.junit.Test;

public class NetClsTest extends SubSystemTest {

    public NetClsTest() {
        super(SubSystemType.net_cls);
    }

    @Test
    public void run() throws IOException {
        NetCls netCls = g1.getNetCls();
        int[] excepted = { 10, 1 };
        netCls.setClassId(excepted[0], excepted[1]);
        int[] actual = netCls.getClassId();
        assertArrayEquals(excepted, actual);
    }

}
