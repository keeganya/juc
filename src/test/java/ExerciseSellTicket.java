import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * @Author xiaohu
 * @Date 2024/10/23 14:18
 * @PackageName:PACKAGE_NAME
 * @ClassName: ExerciseSellTicket
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.ExerciseSellTicket")
public class ExerciseSellTicket {

    public static void main(String[] args) {
        TicketWindow ticketWindow = new TicketWindow(1000);
        List<Thread> list = new ArrayList<>();

        List<Integer> sellCount = new Vector<>();
        for (int i = 0; i < 2000; i++) {
            Thread t = new Thread(() -> {
                int count = ticketWindow.sell(randomAmount());
                try {
                    Thread.sleep(randomAmount());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sellCount.add(count);
            });
            list.add(t);
            t.start();
        }
        list.forEach((t) -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // 卖出去的票求和
        log.debug("selled count:{}",sellCount.stream().mapToInt(c->c).sum());
        // 剩余票数
        log.debug("remainder count:{}",ticketWindow.getCount());

    }

    static Random random = new Random();

    public static int randomAmount(){
        return random.nextInt(5) + 1;
    }
}



class TicketWindow{
    private int count ;
    public TicketWindow(int count){
        this.count = count;
    }

    public int getCount(){
        return count ;
    }

    public int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}
