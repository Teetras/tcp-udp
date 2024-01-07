package org.example.tcp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Соединение установлено.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nВыберите действие:");
                System.out.println("1. Получить расписание");
                System.out.println("2. Редактировать расписание");
                System.out.println("3. Удалить запись из расписания");
                System.out.println("0. Выход");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Очистка буфера после считывания числа

                switch (choice) {
                    case 1:
                        System.out.println("Введите номер записи:");
                        String getKey = scanner.nextLine();
                        String getRequest = "get:" + getKey;
                        writer.println(getRequest);

                        String getResponse = reader.readLine();
                        System.out.println("Результат: " + getResponse);
                        break;
                    case 2:
                        System.out.println("Введите номер записи:");
                        String editKey = scanner.nextLine();
                        System.out.println("Введите новое значение:");
                        String editValue = scanner.nextLine();
                        String editRequest = "edit:" + editKey + ":" + editValue;
                        writer.println(editRequest);

                        String editResponse = reader.readLine();
                        System.out.println("Результат: " + editResponse);
                        break;
                    case 3:
                        System.out.println("Введите номер записи:");
                        String deleteKey = scanner.nextLine();
                        String deleteRequest = "delete:" + deleteKey;
                        writer.println(deleteRequest);

                        String deleteResponse = reader.readLine();
                        System.out.println("Результат: " + deleteResponse);
                        break;
                    case 0:
                        writer.println("exit"); // Отправка сигнала о завершении соединения серверу
                        System.out.println("Соединение закрыто.");
                        return;
                    default:
                        System.out.println("Неверный выбор.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
