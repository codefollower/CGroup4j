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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MemoryTest extends SubSystemTest {

    public MemoryTest() {
        super(SubSystemType.memory);
    }

    @Test
    public void run() {
        Memory memory = g1.getMemory();

        long excepted = 200704l;
        memory.setLimitInBytes(excepted);
        long actual = memory.getLimitInBytes();
        assertEquals(excepted, actual);

        // excepted = 2 * 1024;
        // memory.setMemswLimitInBytes(excepted);
        // actual = memory.getMemswLimitInBytes();
        // assertEquals(excepted, actual);

        excepted = 60;
        memory.setSwappiness((int) excepted);
        actual = memory.getSwappiness();
        assertEquals(excepted, actual);

        memory.setUseHierarchy(true);
        assertTrue(memory.isUseHierarchy());

        memory.setOomControl(true);
        assertTrue(memory.isOomControl());
    }

}
