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
import java.util.LinkedList;
import java.util.List;

import org.lealone.cgroup.Group;
import org.lealone.cgroup.Threads;

public abstract class SubSystem {

    private static final String CGROUP_EVENT_CONTROL = "cgroup.event_control";
    private static final String NOTIFY_ON_RELEASE = "notify_on_release";
    private static final String RELEASE_AGENT = "release_agent";
    private static final String TASKS = "tasks";

    private final List<Integer> taskList = new LinkedList<>();
    private final Group group;

    public SubSystem(Group group) {
        this.group = group;
        List<SubSystem> subSystemList = group.getSubSystemList();
        synchronized (subSystemList) {
            subSystemList.add(this);
        }
    }

    public abstract SubSystemType getType();

    public void addCurrentTask() throws IOException {
        addTask(Threads.getThreadId());
    }

    public void addTask(int task) throws IOException {
        setParameter(TASKS, task);
        taskList.add(task);
    }

    public void setEventControl(String eventFd, String controlFd, String... args) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(eventFd);
        sb.append(' ');
        sb.append(controlFd);
        for (String arg : args) {
            sb.append(' ');
            sb.append(arg);
        }

        setParameter(CGROUP_EVENT_CONTROL, sb);
    }

    public void setNotifyOnRelease(boolean flag) throws IOException {
        int v = flag ? 1 : 0;
        setParameter(NOTIFY_ON_RELEASE, v);
    }

    public boolean isNotifyOnRelease() throws IOException {
        return getIntParameter(NOTIFY_ON_RELEASE) > 0;
    }

    public void setReleaseAgent(String cmd) throws IOException {
        setParameter(RELEASE_AGENT, cmd);
    }

    public String getReleaseAgent() throws IOException {
        return getStringParameter(RELEASE_AGENT);
    }

    protected void setParameter(String parameterName, Object parameterValue) throws IOException {
        group.setParameter(parameterName, parameterValue);
    }

    protected String getStringParameter(String parameterName) throws IOException {
        return group.getParameter(parameterName);
    }

    protected long getLongParameter(String parameterName) throws IOException {
        return Long.parseLong(getStringParameter(parameterName));
    }

    protected int getIntParameter(String parameterName) throws IOException {
        return Integer.parseInt(getStringParameter(parameterName));
    }

    @Override
    public String toString() {
        return getType().name();
    }

}
