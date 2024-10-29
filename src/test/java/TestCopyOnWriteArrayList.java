import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author xiaohu
 * @Date 2024/10/29 13:46
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestCopyOnWriteArrayList
 * @Description: CopyOnWriteArrayList (具有弱一致性，高并发和弱一致性是矛盾的，需要权衡)
 * @Version 1.0
 */
@Slf4j(topic = "c.TestCopyOnwriteArrayList")
public class TestCopyOnWriteArrayList {
    public static void main(String[] args) throws InterruptedException {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        Iterator<Integer> iterator = list.iterator();

        new Thread(() -> {
            list.remove(0);
            log.debug("{}",list); // 2,3,4
        },"t1").start();

        Thread.sleep(1000);
        while (iterator.hasNext()){
            log.debug("{}",iterator.next()); // 1,2,3,4 弱一致性
        }
    }

}
