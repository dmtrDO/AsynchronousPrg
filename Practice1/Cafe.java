/*
  
Очікуваний результат: Код має бути написаний коректно (синтаксис, ООП, обов’язкові
конструкції), відповідати вимогам завдання та успішно виконувати задачі.
Вимоги до кожного варіанту:
 Створення потоків і використання Thread states, використання інтерфейсу Runnable.
 Обробити помилки, що можуть виникати під час виконання.
 Код повинен бути структурованим і коментованим.
 Результат виконання повинен виводитись у вигляді зрозумілому не програмісту. 

Кав'ярня
Створіть програму, де кілька клієнтів (потоки) приходять до кав'ярні та роблять
замовлення на каву. Один потік (бариста) відповідає за приготування кави.
Використовуйте семафори, щоб обмежити кількість замовлень, що одночасно готуються
(наприклад, 2 замовлення одночасно). Враховуйте робочі години:
коли кав’ярня зачинена, ніхто не може увійти, лише працівник піти додому.

*/

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

public class Cafe implements Runnable{
    // черга замовлень
    public static final ArrayBlockingQueue<String> orders = new ArrayBlockingQueue<>(50);

    // максимум одночасних замовлень
    public static final Semaphore maxOrders = new Semaphore(2); 

    // змінна для моніторингу стану кафе
    public static volatile boolean isOpenedCafe = true;

    @Override
    public void run() {
        // Генеруємо клієнтів
        int i = 0;
        while (Cafe.isOpenedCafe) {
            i++;
            Thread client = new Thread(new Client("Client " + i));
            client.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}


