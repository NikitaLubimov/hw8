package org.example.hw8;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static String massage = "";
    private static String QUEUE_NAME;

    public static void main(String[] args) throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        while (true){
            System.out.println("Введите set_topic и тему для подписки. Exit для выхода. Re для смены подписки");
            massage = scanner.nextLine();

            if (massage.equals("Exit")){
                break;
            }

            String[] split = massage.split(" ");
            QUEUE_NAME = split[1];

            try {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                };

                channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String action = scanner.nextLine();
            if (action.equals("Exit")){
                break;
            }
            if (action.equals("Re")){
                continue;
            }
        }
        scanner.close();
    }
}
