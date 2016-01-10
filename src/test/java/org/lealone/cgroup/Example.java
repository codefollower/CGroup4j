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

import java.io.IOException;

import org.lealone.cgroup.subsystem.SubSystemType;

public class Example {

    private static Group root;
    private static Group g1;
    private static Group g2;

    public static void main(String[] args) throws IOException {
        try {
            // 把cpu和cpuset两个子系统挂载到/cgroup/example
            root = Group.createRootGroup("example", SubSystemType.cpu, SubSystemType.cpuset);

            g1 = root.createSubGroup("g1"); // 创建子分组/cgroup/example/g1
            g2 = root.createSubGroup("g2"); // 创建子分组/cgroup/example/g2

            run();

        } finally {
            if (root != null)
                root.umount();
        }
    }

    private static void run() throws IOException {
        g1.getCpuset().setCpus(0);
        g2.getCpuset().setCpus(0);

        g1.getCpuset().setMems(0);
        g2.getCpuset().setMems(0);

        g1.getCpu().setShares(512);
        g2.getCpu().setShares(2048);

        MyThread t1 = new MyThread(g1);
        MyThread t2 = new MyThread(g2);

        t1.start();
        t2.start();

        try {
            Thread.sleep(3000);
            t1.end();
            t2.end();
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class MyThread extends Thread {
        private final Group g;
        private volatile boolean end;

        public MyThread(Group g) {
            super(g.getFullName());
            this.g = g;
            setDaemon(true);
        }

        public void end() {
            end = true;
        }

        @Override
        public void run() {
            int id = Threads.getThreadId();
            System.out.println("Thread id:" + id);
            try {
                g.getCpu().addTask(id);
                while (!end)
                    ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
