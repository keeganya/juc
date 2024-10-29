package org.xiaohu.juc.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author xiaohu
 * @Date 2024/10/24 13:42
 * @PackageName:org.xiaohu.juc.concurrent.atomic
 * @ClassName: TestAtomicInteger
 * @Description: TODO
 * @Version 1.0
 */
public class TestAtomicInteger {

    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(5);
//        AtomicReference<Integer> i = new AtomicReference<>(5);

        /*
        System.out.println(i.incrementAndGet());// ++i
        System.out.println(i.getAndIncrement());// i++

        System.out.println(i); // i = 2

        System.out.println(i.decrementAndGet());// --i
        System.out.println(i.getAndDecrement());// i--

        System.out.println(i); // i = 0

        System.out.println(i.getAndAdd(5));
        System.out.println(i.addAndGet(3));
        */

//        System.out.println(i.getAndUpdate(x -> x * 5));
        System.out.println(i.updateAndGet(x -> x * 3));

        // p是 i的当前值 x的值需要人为的去设定 这里面 x 设置的是 10
        System.out.println(i.getAndAccumulate(10, (p, x) -> p + x)); // 25
        System.out.println(i.accumulateAndGet(-3, (p, x) -> p + x)); // 25-3 = 22

        System.out.println(i.get());
    }
}
