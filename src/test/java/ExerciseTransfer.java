import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @Author xiaohu
 * @Date 2024/10/23 14:48
 * @PackageName:PACKAGE_NAME
 * @ClassName: ExerciseTransfer
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.ExerciseTransfer")
public class ExerciseTransfer {

    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000);
        Account b = new Account(1000);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transfer(b, randomAmount());
            }
        },"t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                b.transfer(a, randomAmount());
            }
        },"t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        log.debug("total:{}",(a.getMoney() + b.getMoney()));
    }

    static Random random = new Random();

    public static int randomAmount(){
        return random.nextInt(100) + 1;
    }
}

class Account{
    private int money;

    public Account(int money){
        this.money =  money;
    }

    public int getMoney(){
        return money;
    }

    public void setMoney(int money){
        this.money = money;
    }

    public void transfer(Account target,int amount){
        synchronized (Account.class) {
            if (this.money > amount) {
                this.setMoney(this.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
        }
    }
}
