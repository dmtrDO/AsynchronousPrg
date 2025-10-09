
/* Один потік (бариста) відповідає за приготування кави. */

// Клас баристи

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Barista implements Runnable {
    private final String name;
    private final int maxOrders;

    public Barista(String name, Semaphore maxOrders) {
        this.name = name;
        this.maxOrders = maxOrders.availablePermits();
    }

    @Override
    public void run() {
        System.out.println(name + " has opened the coffee shop");
        try {
            // Поки відкрите кафе
            while (Cafe.isOpenedCafe) {
                boolean isPreparing = false;
                ArrayList<String> clients = new ArrayList<>();
                // бариста дивиться чергу замовлень
                for (int i = 0; i < maxOrders; i++) {
                    if (!Cafe.orders.isEmpty()) {
                        isPreparing = true;
                        // може готувати до maxOrders замовлень
                        clients.add(Cafe.orders.take());
                    }
                }
                if (isPreparing) {
                    Thread.sleep(1000); // готує замовлення
                    for (String client : clients) { 
                        // повідомлення про завершення
                        System.out.println(name + " has prepared the order for " + client);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

