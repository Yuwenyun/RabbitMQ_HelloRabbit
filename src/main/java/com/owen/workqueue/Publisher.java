package com.owen.workqueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher
{
	private final static String QUEUE_NAME = "WORK_DISTRIBUTION_QUEUE";
	
	public static void main(String[] args)
	{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		
		try(Connection connection = factory.newConnection())
		{
			Channel channel = connection.createChannel();
			String message = getMessage(args);
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(String.format("Msg sent : %s", message));
			
			channel.close();
		}
		catch (IOException | TimeoutException e)
		{
			e.printStackTrace();
		}
	}
	
	private static String getMessage(String[] args)
	{
		if(args.length < 1)
			return "Hello Owen";
		
		StringBuilder builder = new StringBuilder(args[0]);
		for(int i = 0; i < args.length; i++)
			builder.append(" ").append(args[i]);
		return builder.toString();
	}
}
