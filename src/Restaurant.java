import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Restaurant {
    private static final int TOTAL_TABLES = 5;
    private static final int TOTAL_ORDERS = 30;
    private static final int MAX_CONCURRENT_ORDERS = 3;

    private final Semaphore tables = new Semaphore(TOTAL_TABLES);
    private final Semaphore kitchen = new Semaphore(MAX_CONCURRENT_ORDERS);
    private final Statistics statistics = new Statistics();

    public void simulate() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook activated. Printing statistics...");
            statistics.printStatistics();
        }));

        ExecutorService customerPool = Executors.newCachedThreadPool();

        for (int i = 0; i < TOTAL_ORDERS; i++) {
            customerPool.execute(new Customer(i + 1, tables, kitchen, statistics));
        }

        customerPool.shutdown();

        try {
            customerPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Теперь статистика будет выведена также и здесь, после завершения работы всех потоков
            statistics.printStatistics();
        }
    }
}
