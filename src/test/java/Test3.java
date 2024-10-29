import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author xiaohu
 * @Date 2024/10/22 17:27
 * @PackageName:PACKAGE_NAME
 * @ClassName: Test3
 * @Description:  实现callable接口方式创建线程
 * @Version 1.0
 */
@Slf4j(topic = "c.test3")
public class Test3 implements Callable {
    @Override
    public Object call() throws Exception {
        log.debug("实现Callable接口开启线程");
        return 100;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Object> futureTask = new FutureTask<>(new Test3());

        Thread t1 = new Thread(futureTask, "t1");
        t1.start();
        log.debug("接口返回值 {}" ,futureTask.get());

        FutureTask<Integer> futureTask1 = new FutureTask<>(() -> {
            log.debug("lamabda表达式实现call接口开启线程");
            return 200;
        });

        Thread t2 = new Thread(futureTask1, "t2");
        t2.start();

        log.debug("main线程running");


    }
}
