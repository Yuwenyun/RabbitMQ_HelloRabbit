package com.owen.RabbitMQ_HelloRabbit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Receiver
{
	private final static String QUEUE_NAME = "Hello";
	
	public static void main(String[] args)
	{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try(Connection connection = factory.newConnection())
		{
			Channel channel = connection.createChannel();
			// declare the queue to make sure it exists, won't create it if it exists.
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			System.out.println(String.format("Listening to [%s]", QUEUE_NAME));
			
			Consumer consumer = new DefaultConsumer(channel)
			{
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, 
						AMQP.BasicProperties properties, byte[] body)throws IOException
				{
					String message = new String(body, "UTF-8");
					System.out.println(String.format("Received : %s", message));
				}
			};
			channel.basicConsume(QUEUE_NAME, true, consumer);
			
			// consumer dies when channel, connection is closed or TCP connection is lost.
			channel.close();
		}
		catch (IOException | TimeoutException e)
		{
			e.printStackTrace();
		}
	}
}
