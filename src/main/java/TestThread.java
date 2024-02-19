import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

public class TestThread {
    public static void main(String[] args){
        ThreadPoolExecutor executor = new ThreadPoolTaskExecutor().getThreadPoolExecutor();
        System.out.println("executing thrreads...");
        Runnable r = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + ". Now Sleeping 10 seconds");
                Thread.sleep(10000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        };

        for(int i = 0 ; i < 10 ; i++){
            executor.execute(r);
        }

    }
}
