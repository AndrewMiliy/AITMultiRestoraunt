import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    private final AtomicInteger servedCustomers = new AtomicInteger(0);
    private final AtomicInteger totalServiceTime = new AtomicInteger(0);

    public void customerServed(int serviceTime) {
        servedCustomers.incrementAndGet();
        totalServiceTime.addAndGet(serviceTime);
    }

    public void printStatistics() {
        System.out.println("Restaurant closed. Total served customers: " + servedCustomers.get());
        System.out.println("Average service time: " + (totalServiceTime.get() / servedCustomers.get()) + "ms");
    }
}
