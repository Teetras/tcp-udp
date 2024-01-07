package org.example.tcp;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int PORT = 12345;
    private Map<String, String> schedule;

    public Server() {
        schedule = new HashMap<>();
        initializeSchedule();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен. Ожидание подключений...");

            while (true) {
                // Ожидание подключения клиента
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try {
            System.out.println("Клиент подключился: " + socket.getInetAddress());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String request;
            while ((request = reader.readLine()) != null) {
                String response = processRequest(request);
                writer.println(response);
                if (request.trim().equalsIgnoreCase("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Клиент отключился.");
        }
    }

    private String processRequest(String request) {
        String[] parts = request.split(":");
        String command = parts[0].trim().toLowerCase();
        String response = "";

        switch (command) {
            case "get":
                String key = parts[1].trim();
                if (schedule.containsKey(key)) {
                    response = schedule.get(key);
                } else {
                    response = "Запись не найдена.";
                }
                break;
            case "edit":
                key = parts[1].trim();
                String value = parts[2].trim();
                schedule.put(key, value);
                response = "Запись успешно отредактирована.";
                break;
            case "delete":
                key = parts[1].trim();
                if (schedule.containsKey(key)) {
                    schedule.remove(key);
                    response = "Запись успешно удалена.";
                } else {
                    response = "Запись не найдена.";
                }
                break;
            default:
                response = "Неверная команда.";
                break;
        }

        return response;
    }

    private void initializeSchedule() {
        schedule.put("1", "Математика");
        schedule.put("2", "Физика");
        schedule.put("3", "История");
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
