package org.xiaohu.juc.concurrent.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author xiaohu
 * @Date 2024/10/24 14:48
 * @PackageName:org.xiaohu.juc.concurrent.atomic
 * @ClassName: TestABA
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestABA")
public class TestABA {
    // ABA 问题 可以通过【AtomicStampedReference】这种带时间戳的原子引用解决
    static AtomicReference<String> ref = new AtomicReference<>("A");

    public static void main(String[] args) {
        log.debug("main start...");
        String prev = ref.get();
        // 在change()函数中，ref 经历了 【A -> B -> A】 但是对于主线程是不知道的，以为没修改过
        change();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //尝试改为c
        log.debug("change A->C {}",ref.compareAndSet(prev,"C"));
        System.out.println(ref.get());
    }

    public static void change() {
        new Thread(() -> {
            log.debug("change A->B {}",ref.compareAndSet(ref.get(), "B"));
        },"t1").start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            log.debug("change B->A {}",ref.compareAndSet(ref.get(), "A"));
        },"t2").start();
    }
}
