package org.example.upd;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class ClientUpd {
    public static void main(String[] args) {
        final String serverHost = "localhost";
        final int serverPort = 9876;

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(serverHost);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Ввод значения для x: ");
            double x = scanner.nextDouble();
            System.out.print("Ввод значения для y: ");
            double y = scanner.nextDouble();

            // Отправляем значения x и y на сервер
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeDouble(x);
            objectOutputStream.writeDouble(y);
            objectOutputStream.flush();

            byte[] sendData = byteArrayOutputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            clientSocket.send(sendPacket);

            // Получаем ответ от сервера
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receiveData);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            double result = objectInputStream.readDouble();
            System.out.println("Ответ сервера: Результат = " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}