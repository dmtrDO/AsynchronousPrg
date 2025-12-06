
// Створіть два асинхронних завдання. Перше завдання отримує даін з БД,
// а друге завдання залежить від результату першого і обробляє отримані дані

import java.sql.*;
import java.util.concurrent.*;

public class AsyncDB {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/udp_db", 
            "root",
            "strongpassword123");

            CompletableFuture<ResultSet> fetchedDataFromBd = fetchDataFromDB(connection);
            fetchedDataFromBd.thenCompose(results -> processFetchedData(results));

            while (true) {
                System.out.println("I'm working");
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static CompletableFuture<ResultSet> fetchDataFromDB(Connection connection) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery("SELECT id, name, data FROM files");
                return results;   
            } catch (Exception e) {
                System.out.println(e);
                throw new CompletionException(e);
            }
        });
    }

    public static CompletableFuture<Void> processFetchedData(ResultSet results) {
        return CompletableFuture.runAsync(() -> {
            try {
                while (results.next()) {
                    int id = results.getInt("id");
                    String name = results.getString("name");
                    System.out.println(id + "\t" + name);
                }
                Statement statement = results.getStatement();
                statement.getConnection().close();
                results.close();   
            } catch (SQLException e) {
                System.out.println(e);
            }
        });
    }
}


