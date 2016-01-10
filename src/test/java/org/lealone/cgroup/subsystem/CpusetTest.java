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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class CpusetTest extends SubSystemTest {

    public CpusetTest() {
        super(SubSystemType.cpuset);
    }

    @Test
    public void run() throws IOException {
        Cpuset cpuset = g1.getCpuset();

        int[] excepted = { 0, 1 };
        cpuset.setCpus(excepted);
        int[] actual = cpuset.getCpus();
        assertArrayEquals(excepted, actual);

        int[] excepted2 = { 0 };
        cpuset.setMems(excepted2);
        actual = cpuset.getMems();
        assertArrayEquals(excepted2, actual);

        cpuset.setMemMigrate(true);
        boolean flag = cpuset.isMemMigrate();
        assertTrue(flag);

        cpuset.setCpuExclusive(true);
        flag = cpuset.isCpuExclusive();
        assertTrue(flag);

        cpuset.setMemExclusive(true);
        flag = cpuset.isMemExclusive();
        assertTrue(flag);

        cpuset.setMemHardwall(true);
        flag = cpuset.isMemHardwall();
        assertTrue(flag);

        int pressure = cpuset.getMemPressure();
        assertTrue(pressure >= 0);

        root.getCpuset().setMemPressureEnabled(true);
        flag = root.getCpuset().isMemPressureEnabled();
        assertTrue(flag);

        cpuset.setMemSpreadPage(true);
        flag = cpuset.isMemSpreadPage();
        assertTrue(flag);

        cpuset.setMemSpreadSlab(true);
        flag = cpuset.isMemSpreadSlab();
        assertTrue(flag);

        cpuset.setSchedLoadBlance(true);
        flag = cpuset.isSchedLoadBlance();
        assertTrue(flag);

        cpuset.setSchedRelaxDomainLevel(0);
        int level = cpuset.getSchedRelaxDomainLevel();
        assertEquals(0, level);

        String output = "0-1,3";
        actual = Cpuset.parseNums(output);
        int[] excepted3 = { 0, 1, 3 };
        assertArrayEquals(excepted3, actual);
    }

}
