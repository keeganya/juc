package org.xiaohu.juc.concurrent.atomic;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @Author xiaohu
 * @Date 2024/10/24 15:29
 * @PackageName:org.xiaohu.juc.concurrent.atomic
 * @ClassName: TestAtomicMarkableReference
 * @Description: 带标记的原子引用
 * @Version 1.0
 */
@Slf4j(topic = "c.TestAtomicMarkableReference")
public class TestAtomicMarkableReference {
    public static void main(String[] args) throws InterruptedException {
        GarbageBag bag = new GarbageBag("装满了垃圾");
        // 第二个参数 看作一个标记 ，true表示垃圾满了
        AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag,true);
        log.debug("主线程 start...");
        GarbageBag prev = ref.getReference();
        log.debug(prev.toString());

        new Thread(() -> {
            log.debug("打扫卫生的线程 start...");
            bag.setDesc("空垃圾袋"); // 改变垃圾袋的属性 变成一个空垃圾袋
            while (!ref.compareAndSet(bag, bag, true, false)) { // 第一次改成功之后，mark变成false，下次就进不去循环内了

            }
            log.debug(bag.toString());
        },"保洁阿姨").start();

        Thread.sleep(1000);
        log.debug("主线程想换一只新的垃圾袋？");
        boolean success = ref.compareAndSet(prev, new GarbageBag("空垃圾袋"), true, false);

        log.debug("换了吗 {}",success);

        log.debug(ref.getReference().toString());
    }

}

class GarbageBag {
    String desc;
    public GarbageBag(String desc){
        this.desc = desc;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }

    @Override
    public String toString() {
        return super.toString() + " " + desc;
    }
}
