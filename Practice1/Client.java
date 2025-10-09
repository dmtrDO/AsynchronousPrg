
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
            // зменшує кількість дозволів у семафора на 1
            Cafe.maxOrders.acquire();
            // додаємо клієнта у чергу замовлень
            Cafe.orders.put(name);
            // чекаємо на приготування
            while (Cafe.orders.contains(name)) {
                if (!Cafe.isOpenedCafe) break;
            }
            // після приготування звільняємо дозвіл у семафора (+1)
            Cafe.maxOrders.release();
            if (Cafe.isOpenedCafe) {
                System.out.println(name + " has come to the coffee shop and made an order");
            }   
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


