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

public class Freezer extends SubSystem {

    private static final String FREEZER_STATE = "freezer.state";

    public static final String STATE_FROZEN = "FROZEN";
    public static final String STATE_FREEZING = "FREEZING";
    public static final String STATE_THAWED = "THAWED";

    public Freezer(Group group) {
        super(group);
    }

    @Override
    public SubSystemType getType() {
        return SubSystemType.freezer;
    }

    public void setState(String state) throws IOException {
        setParameter(FREEZER_STATE, state);
    }

    public String getState() throws IOException {
        return getStringParameter(FREEZER_STATE);
    }

}
