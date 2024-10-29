package org.xiaohu.juc.concurrent.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.SynchronousQueue;

/**
 * @Author xiaohu
 * @Date 2024/10/25 14:36
 * @PackageName:org.xiaohu.juc.concurrent.pool
 * @ClassName: TestSynchronousQueue
 * @Description: 测试【synchronousQueue】 没有容量的队列 必须有来取 才会往里放
 * @Version 1.0
 */
@Slf4j(topic = "c.TestSynchronousQueue")
public class TestSynchronousQueue {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> integers = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                log.debug("putting... {}",1);
                integers.put(1);
                log.debug("{} putted...",1);

                log.debug("putting... {}",2);
                integers.put(2);
                log.debug("{} putted...",2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();

        Thread.sleep(1000);

        new Thread(() -> {
            try {
                log.debug("taking... {}",1);
                integers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2").start();


        Thread.sleep(1000);

        new Thread(() -> {
            try {
                log.debug("taking... {}",2);
                integers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t3").start();
    }
}
