
/* Кілька клієнтів (потоки) приходять до кав'ярні та роблять
замовлення на каву */

public class Client implements Runnable {
    private final String name;

    public Client(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Cafe.maxOrders.acquire();
            Cafe.orders.put(name);
            while (Cafe.orders.contains(name)) {
                if (!Cafe.isOpenedCafe) break;
            }
            Cafe.maxOrders.release();
            if (Cafe.isOpenedCafe) {
                System.out.println(name + " has come to the coffee shop and made an order");
            }   
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


