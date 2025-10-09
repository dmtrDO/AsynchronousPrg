public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Стартуємо баристу
        Thread barista = new Thread(new Barista("Barista", Cafe.maxOrders));
        barista.start();

        Cafe.isOpenedCafe = true; // відкриваємо кав’ярню

        // Генеруємо клієнтів
        Thread cafe = new Thread(new Cafe());
        cafe.start();

        // Робочий день
        Thread.sleep(6000);
        Cafe.isOpenedCafe = false;
        System.out.println("The shop is closed, clients can no longer enter the cafe !");
    }
}
