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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DevicesTest extends SubSystemTest {

    public DevicesTest() {
        super(SubSystemType.devices);
    }

    @Test
    public void run() {
        Devices devices = g1.getDevices();

        Devices.Record record = new Devices.Record(Devices.TYPE_ALL, 8, 0, 2);
        devices.setAllow(record.type, record.major, record.minor, record.accesses);

        record = new Devices.Record(Devices.TYPE_ALL, 8, 0, 2);
        devices.setDeny(record.type, record.major, record.minor, record.accesses);

        Devices.Record[] records = root.getDevices().getList();
        assertTrue(records.length > 0);
    }

}
