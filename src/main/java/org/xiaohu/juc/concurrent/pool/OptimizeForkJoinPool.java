package org.xiaohu.juc.concurrent.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @Author xiaohu
 * @Date 2024/10/28 10:23
 * @PackageName:org.xiaohu.juc.concurrent.pool
 * @ClassName: OptimizeForkJoinPool
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.OptimizeForkJoinPool")
public class OptimizeForkJoinPool {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(4);
        System.out.println(pool.invoke(new AddTask1(1, 5)));
    }
}
@Slf4j(topic = "c.AddTask1")
class AddTask1 extends RecursiveTask<Integer>{
    int begin;
    int end;
    public AddTask1(int begin,int end){
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return "AddTask1{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }

    @Override
    protected Integer compute() {
        /* 比如 拆分到 begin = 5，end = 5
        * */
        if (begin == end) {
            log.debug("join() {}", begin);
            return begin;
        }
        /*比如 拆分到 begin = 4，end = 5
        * */
        if (end - begin == 1) {
            log.debug("join() {} + {} = {}",begin, end, end + begin);
        }

        // begin = 1,end = 5
        int mid = (end + begin) / 2;

        AddTask1 a1 = new AddTask1(begin, mid); // begin=1, mid=2
        a1.fork();

        AddTask1 a2 = new AddTask1(mid+1, end); // mid+1=3, end=5
        a2.fork();
        log.debug("fork() {} + {} = ?",a1,a2);

        int result = a1.join() + a2.join();
        log.debug("join() {} + {} = {}",a1,a2,result);
        return result;
    }
}
