package com.zk.zk_online.Utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.security.PrivateKey;


/**
 * Created by ZYB on 2017/11/6 0006.
 */

public class RabbitMQUtil {

    private static String IP = "58.62.236.54";
    private static int PORT = 5672;
    private static String Username = "yy";
    private static String password = "hello";
    private static String queuename = "zsMonitor";

    private static ConnectionFactory factory;

    static {
        factory = new ConnectionFactory();
        factory.setHost(IP);// MQ的IP
        factory.setPort(PORT);// MQ端口
        factory.setUsername(Username);// MQ用户名
        factory.setPassword(password);// MQ密码
    }

    public static void sendMessage(final String code) {


        try {


            //开启子线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Connection connection = null;
                        connection = factory.newConnection();
                        Channel channel = connection.createChannel();

                        /* 创建消息队列，并且发送消息 */
                        channel.queueDeclare(queuename, true, false, false, null);
                        String message = "ECD|02A2|" + code;
                        //发送消息
                        channel.basicPublish("", queuename, null, message.getBytes());
                        System.out.println("生产了个'" + message + "'");

                        /* 关闭连接 */
                        channel.close();
                        connection.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //从消息队列中读取消息
    public static void  receMessage()
    {
    }


}
