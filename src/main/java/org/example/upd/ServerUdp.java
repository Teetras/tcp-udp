package org.example.upd;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerUdp {
    public static void main(String[] args) {
        final int serverPort = 9876;

        try (DatagramSocket serverSocket = new DatagramSocket(serverPort)) {
            System.out.println("Server is running...");

            while (true) {
                 byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                serverSocket.receive(receivePacket);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receiveData);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                // Принимаем значения x и y от клиента
                double x = objectInputStream.readDouble();
                double y = objectInputStream.readDouble();

                double result = calculateFunction(x, y);

                // Отправляем результат обратно клиенту
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeDouble(result);
                objectOutputStream.flush();

                byte[] sendData = byteArrayOutputStream.toByteArray();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());

                serverSocket.send(sendPacket);

                System.out.println("Received from " + receivePacket.getAddress() + ":" + receivePacket.getPort() + ": x=" + x + ", y=" + y + ", Result: " + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double calculateFunction(double x, double y) {
        return 5 * Math.atan(x) - (1.0 / 4) * Math.cos((x + 3 * Math.abs(x - y) + Math.pow(x, 2)) / (Math.abs(x + Math.pow(y, 2)) + Math.pow(x, 3)));
    }
}