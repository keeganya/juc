package org.xiaohu.juc.concurrent.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Author xiaohu
 * @Date 2024/10/24 15:03
 * @PackageName:org.xiaohu.juc.concurrent.atomic
 * @ClassName: TestAtomicStampedReference
 * @Description: 带版本戳的原子引用
 * @Version 1.0
 */
@Slf4j(topic = "c.TestAtomicStampedReference")
public class TestAtomicStampedReference {

    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A",0);
    public static void main(String[] args) {
        log.debug("main start...");
        // 获取值 A
        String prev = ref.getReference();
        // 获取版本号
        int stamp = ref.getStamp();
        log.debug("版本 {}",stamp);
        // 如果中间有其他线程干扰，发生了 ABA 现象
        change();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("change A->C {}",ref.compareAndSet(prev,"C", stamp, stamp + 1));
    }

    private static void change(){
        new Thread(() -> {
            log.debug("change A->B {}",ref.compareAndSet(ref.getReference(),"B", ref.getStamp(), ref.getStamp() + 1));
            log.debug("更新版本为 {}，", ref.getStamp());
        },"t1").start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            log.debug("change B->A {}",ref.compareAndSet(ref.getReference(),"A", ref.getStamp(), ref.getStamp() + 1));
            log.debug("更新的版本为 {}",ref.getStamp());
        },"t2").start();
    }
}
