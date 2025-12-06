
// Користувач хоче порівняти погоду в трьох різних містах.
// Потрібно отримати температуру, вологість та швидкість вітру для кожного міста
// паралельно, а потім порівняти ці дані. Дати висновки про те, де зараз
// можна сходити на пляж, а де варто вдягнутись тепліше

import java.util.concurrent.*;
import java.util.*;

public class AsyncWeather {
    public static class WeatherData {
        String city;
        int temperature;
        int humidity;
        int windSpeed;

        public WeatherData(String city, int temperature, int humidity, int windSpeed) {
            this.city = city;
            this.temperature = temperature;
            this.humidity = humidity;
            this.windSpeed = windSpeed;
        }

        @Override
        public String toString() {
            return String.format("[%s]: T: %d°C, H: %d%%, Wind: %d km/h", city, temperature, humidity, windSpeed);
        }
    }
   
    public static void main(String[] args) {
        CompletableFuture<WeatherData> city1 = getWeather("city1");
        CompletableFuture<WeatherData> city2 = getWeather("city2");
        CompletableFuture<WeatherData> city3 = getWeather("city3");

        CompletableFuture<List<WeatherData>> combinedCity12 = city1.thenCombine(city2, (result1, result2) -> {
            List<WeatherData> list = new ArrayList<>();
            list.add(result1);
            list.add(result2);
            return list;
        });

        CompletableFuture<List<WeatherData>> combinedCity123 = combinedCity12.thenCombine(city3, (currentList, result3) -> {
            currentList.add(result3);
            return currentList;
        });

        combinedCity123.thenAccept(weatherList -> {
            System.out.println("Received data: ");
            for (WeatherData weather : weatherList) {
                analyzeWeather(weather);
            }
        });

        while (true) {
            System.out.println("I'm working...");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static CompletableFuture<WeatherData> getWeather(String cityName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep((int)(Math.random() * 5000));   
            } catch (Exception e) {
                System.out.println(e);
            }
            int temp = (int)(Math.random() * 40 - 10);
            int humidity = (int)(Math.random() * 100);
            int wind = (int)(Math.random() * 30);
            System.out.println("Weather data for " + cityName + " has just been received");
            return new WeatherData(cityName, temp, humidity, wind);
        });
    }

    public static void analyzeWeather(WeatherData weather) {
        System.out.println("----------------------------------------");
        System.out.println(weather);
        if (weather.temperature > 15) { 
            System.out.println("You can go to the beach");
        } else {
            System.out.println("Dress warmer before going outside");
        }
        System.out.println("----------------------------------------");
    }

}


