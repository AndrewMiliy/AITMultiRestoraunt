import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Customer implements Runnable {
    private final int customerId;
    private final Semaphore tables;
    private final Semaphore kitchen;
    private final Statistics statistics;

    public Customer(int customerId, Semaphore tables, Semaphore kitchen, Statistics statistics) {
        this.customerId = customerId;
        this.tables = tables;
        this.kitchen = kitchen;
        this.statistics = statistics;
    }

    @Override
    public void run() {
        try {
            tables.acquire();
            System.out.println("Customer " + customerId + " got a table.");

            kitchen.acquire();
            int serviceTime = 1000 + ThreadLocalRandom.current().nextInt(4000);
            Thread.sleep(serviceTime);
            kitchen.release();

            statistics.customerServed(serviceTime);

            System.out.println("Customer " + customerId + " served in " + serviceTime + "ms and left the table.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            tables.release();
        }
    }
}
