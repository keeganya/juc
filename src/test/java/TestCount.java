import lombok.extern.slf4j.Slf4j;

/**
 * @Author xiaohu
 * @Date 2024/10/23 10:45
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestCount
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestCount")
public class TestCount {

    static int counter = 0;
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        Room room = new Room();

        Thread t1 = new Thread(() -> {
                for (int i = 0; i < 500000; i++) {
                   /* synchronized (lock) {
                    counter++;
                }*/
                room.incrment();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
                for (int i = 0; i < 500000; i++) {
                    /*synchronized (lock) {
                    counter--;
                }*/
                    room.decrment();
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        log.debug("{}",room.getCounter());
    }

    static class Room{
        public void incrment(){
            synchronized (this){
                counter++;
            }
        }

        public void decrment(){
            synchronized (this){
                counter--;
            }
        }

        public int getCounter(){
            synchronized (this){
                return counter;
            }
        }
    }
}
