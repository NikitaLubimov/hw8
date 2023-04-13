package org.example.hw8;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class Producer {
    private static String QUEUE_NAME;
    private final static String EXCHANGER_NAME = "exchanger";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Введите тему топика и текст сообщения:");
                String message = scanner.nextLine();
                if (message.equals("exit")) {
                    break;
                }
                int i = message.indexOf(" ");
                String nameTopic = message.substring(0, i);
                String msg = message.substring(i+1);
                QUEUE_NAME = nameTopic;

                channel.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.DIRECT);
                channel.queueDeclare(nameTopic, false, false, false, null);
                channel.queueBind(QUEUE_NAME, EXCHANGER_NAME, nameTopic);

                channel.basicPublish(EXCHANGER_NAME, nameTopic, null, msg.getBytes());
                System.out.println(" [x] Send '" + msg + "'");
            }

            scanner.close();
        }
    }
}