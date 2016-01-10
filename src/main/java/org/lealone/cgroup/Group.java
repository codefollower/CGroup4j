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
package org.lealone.cgroup;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.lealone.cgroup.subsystem.Blkio;
import org.lealone.cgroup.subsystem.Cpu;
import org.lealone.cgroup.subsystem.Cpuacct;
import org.lealone.cgroup.subsystem.Cpuset;
import org.lealone.cgroup.subsystem.Devices;
import org.lealone.cgroup.subsystem.Freezer;
import org.lealone.cgroup.subsystem.Memory;
import org.lealone.cgroup.subsystem.NetCls;
import org.lealone.cgroup.subsystem.NetPrio;
import org.lealone.cgroup.subsystem.SubSystem;
import org.lealone.cgroup.subsystem.SubSystemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Group {

    private final Group parent;
    private final String name;
    private final Shell shell;
    private final int subSystems;

    private final List<SubSystem> subSystemsList = new LinkedList<>();
    private final List<Group> subGroups = new LinkedList<>();

    private Blkio blkio;
    private Cpu cpu;
    private Cpuacct cpuacct;
    private Cpuset cpuset;
    private Devices devices;
    private Freezer freezer;
    private Memory memory;
    private NetCls netCls;
    private NetPrio netPrio;

    private volatile boolean umounted;

    protected Group(Group parent, String name, Shell shell, int subSystems) throws IOException {
        this.parent = parent;
        this.name = name;
        this.shell = shell;
        this.subSystems = subSystems;
        if (parent != null) {
            shell.mkdir(getFullName());
            parent.subGroups.add(this);
        }
    }

    public Group createSubGroup(String name, SubSystemType... subSystemTypes) throws IOException {
        int subSystems = 0;
        for (SubSystemType type : subSystemTypes)
            subSystems |= type.value;

        subSystems |= this.subSystems;
        return new Group(this, name, shell, subSystems);
    }

    public void umount() throws IOException {
        Group[] subGroups = new Group[this.subGroups.size()];
        this.subGroups.toArray(subGroups);

        for (Group group : subGroups) {
            group.umount();
        }
        if (parent != null) {
            synchronized (parent.subGroups) {
                parent.subGroups.remove(this);
            }

            shell.rmdir(getFullName());
        } else {
            shell.umount(name);
            shell.rmdir(name);
        }
        umounted = true;
    }

    public boolean isUmounted() {
        return umounted;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        if (parent == null)
            return name;
        else
            return parent.getFullName() + "/" + name;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    public List<SubSystem> getSubSystemList() {
        return subSystemsList;
    }

    public Blkio getBlkio() {
        if (blkio == null) {
            check(SubSystemType.blkio);
            blkio = new Blkio(this);
        }
        return blkio;
    }

    public Cpu getCpu() {
        if (cpu == null) {
            check(SubSystemType.cpu);
            cpu = new Cpu(this);
        }
        return cpu;
    }

    public Cpuacct getCpuacct() {
        if (cpuacct == null) {
            check(SubSystemType.cpuacct);
            cpuacct = new Cpuacct(this);
        }

        return cpuacct;
    }

    public Cpuset getCpuset() {
        if (cpuset == null) {
            check(SubSystemType.cpuset);
            cpuset = new Cpuset(this);
        }

        return cpuset;
    }

    public Devices getDevices() {
        if (devices == null) {
            check(SubSystemType.devices);
            devices = new Devices(this);
        }

        return devices;
    }

    public Freezer getFreezer() {
        if (freezer == null) {
            check(SubSystemType.freezer);
            freezer = new Freezer(this);
        }

        return freezer;
    }

    public Memory getMemory() {
        if (memory == null) {
            check(SubSystemType.memory);
            memory = new Memory(this);
        }

        return memory;
    }

    public NetCls getNetCls() {
        if (netCls == null) {
            check(SubSystemType.net_cls);
            netCls = new NetCls(this);
        }

        return netCls;
    }

    public NetPrio getNetPrio() {
        if (netPrio == null) {
            check(SubSystemType.net_prio);
            netPrio = new NetPrio(this);
        }

        return netPrio;
    }

    private void check(SubSystemType type) {
        if ((subSystems & type.value) == 0) {
            String msg = "SubSystem '" + type.name() + "' not attach to group '" + getFullName() + "'";
            throw new IllegalStateException(msg);
        }
    }

    public void setParameter(String parameterName, Object parameterValue) throws IOException {
        shell.set(getFullName(), parameterName, parameterValue.toString());
    }

    public String getParameter(String parameterName) throws IOException {
        return shell.get(getFullName(), parameterName);
    }

    private static final String CGROUP_CONFIG_FILE = "cgroup.properties";
    private static final String PASSWORD = "password";
    private static final String SUDO = "sudo";

    public static Group createRootGroup(String name, SubSystemType... subSystemTypes) throws IOException {
        boolean sudo = false;
        String password = null;
        try {
            Properties prop = new Properties();
            prop.load(Group.class.getClassLoader().getResourceAsStream(CGROUP_CONFIG_FILE));
            sudo = Boolean.parseBoolean(prop.getProperty(SUDO));
            if (sudo) {
                password = prop.getProperty(PASSWORD);
            }
        } catch (Exception e) {
            // ignore
        }
        if (sudo && password == null) {
            password = readPassword();
        }

        int subSystems = 0;
        for (SubSystemType type : subSystemTypes)
            subSystems |= type.value;

        Shell shell = new Shell(password);
        shell.mkdir(name);
        shell.mount(name, subSystems);
        return new Group(null, name, shell, subSystems);
    }

    private static String readPassword() throws IOException {
        Console console = System.console();
        if (console == null) { //In Eclipse
            System.out.print("Password: ");
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            return stdin.readLine();
        } else { //Outside Eclipse
            char[] pwd = console.readPassword("Password: ");
            return new String(pwd);
        }
    }

    private static class Shell {

        private static final Logger LOG = LoggerFactory.getLogger(Shell.class);

        private static final String CGROUP_DIR_PREFIX = "/cgroup/%s";

        private static final String SHELL_MKDIR = "mkdir -p %s";
        private static final String SHELL_RMDIR = "rmdir %s";
        private static final String SHELL_MOUNT = "mount -t cgroup -o %s %s %s";
        private static final String SHELL_UMOUNT = "umount %s";

        private static final String SHELL_CAT = "cat " + CGROUP_DIR_PREFIX + "/%s";
        private static final String SHELL_ECHO = "echo \"%s\" > " + CGROUP_DIR_PREFIX + "/%s";

        private final String cmdPrefix;

        public Shell(String password) {
            if (password != null)
                cmdPrefix = "echo " + password + " | sudo -S ";
            else
                cmdPrefix = "";
        }

        public String exec(String cmd) throws IOException {
            return exec(cmd, false);
        }

        public String exec(String cmd, boolean isPrivilege) throws IOException {
            if (isPrivilege) {
                cmd = cmdPrefix + cmd;
            }
            LOG.info("Shell cmd: " + cmd);
            Process process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", cmd });
            try {
                process.waitFor();
                if (process.exitValue() != 0) {
                    String errorOutput = toString(process.getErrorStream());
                    LOG.error("Shell Error Output: " + errorOutput);
                    throw new IOException(errorOutput);
                }
                String output = toString(process.getInputStream());
                //LOG.info("Shell Output: " + output);
                return output;
            } catch (InterruptedException ie) {
                throw new IOException(ie.toString());
            }
        }

        private static String toString(InputStream input) throws IOException {
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            return new String(buffer);

        }

        public void mount(String name, int subSystems) throws IOException {
            String path = String.format(CGROUP_DIR_PREFIX, name);

            if (subSystems > 0) {
                String flag = SubSystemType.getTypeNames(subSystems);
                String cmd = String.format(SHELL_MOUNT, flag, name, path);
                exec(cmd, true);
            }
        }

        public void umount(String name) throws IOException {
            String path = String.format(CGROUP_DIR_PREFIX, name);
            String cmd = String.format(SHELL_UMOUNT, path);
            exec(cmd, true);
        }

        public void mkdir(String path) throws IOException {
            path = String.format(CGROUP_DIR_PREFIX, path);
            String cmd = String.format(SHELL_MKDIR, path);
            exec(cmd, true);
        }

        public void rmdir(String path) throws IOException {
            path = String.format(CGROUP_DIR_PREFIX, path);
            String cmd = String.format(SHELL_RMDIR, path);
            exec(cmd, true);
        }

        public void set(String group, String p, String value) throws IOException {
            String cmd = String.format(SHELL_ECHO, value, group, p);
            exec(cmd, true);
        }

        public String get(String group, String p) throws IOException {
            String cmd = String.format(SHELL_CAT, group, p);
            String result = exec(cmd);
            result = result.trim();
            return result;
        }

    }

}
