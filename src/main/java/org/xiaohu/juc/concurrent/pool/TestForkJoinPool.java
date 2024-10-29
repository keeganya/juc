package org.xiaohu.juc.concurrent.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @Author xiaohu
 * @Date 2024/10/28 9:46
 * @PackageName:org.xiaohu.juc.concurrent.pool
 * @ClassName: TestForkJoinPool
 * @Description: ForkJoinPool 求和
 * @Version 1.0
 */
@Slf4j(topic = "c.TestForkJoinPool")
public class TestForkJoinPool {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(4);
        System.out.println(pool.invoke(new Mytask(5)));
    }
}

// 计算 1-5 之间整数的和
@Slf4j(topic = "c.Mytask")
class Mytask extends RecursiveTask<Integer> {
    private int n;
    public Mytask(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "Mytask{" +
                "n=" + n +
                '}';
    }

    @Override
    protected Integer compute() {
        // 如果 n 已经为 1，可以求得结果了
        if (n == 1) {
            log.debug("join() {}",n);
            return n;
        }

        // 将任务进行拆分（fork）
        Mytask mytask = new Mytask(n - 1);
        mytask.fork();
        log.debug("fork() {} + {}", n, mytask);

        // 合并（join）结果
        int result = n + mytask.join();
        log.debug("join() {} + {} = {}", n, mytask, result);
        return result;
    }
}
