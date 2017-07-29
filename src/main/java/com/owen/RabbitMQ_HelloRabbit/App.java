package com.owen.RabbitMQ_HelloRabbit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Key classes and interfaces:
 *   Channel: Protocol operations
 *   Connection: open channels, register connection life cycle event handlers, close connections
 *   ConnectionFactory: instantiate Connection instance
 *   Consumer: 
 *     
 * Basic concepts:
 *   Producer: user application that sends messages
 *   Queue: buffer that stores messages
 *   Consumer: user application receives messages
 * 
 * @author yuwenyun
 */
public class App 
{
	private final static String QUEUE_NAME = "Owen";
	
    public static void main( String[] args )
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        
        try(Connection connection = factory.newConnection())
		{
        	// the provided Channel didn't implement Closable thus it
        	// needs the close explicitly
    		Channel channel = connection.createChannel();
    		
			// declare a queue with channel, rabbitmq only creates the queue
			// if it doesn't exist
    		// queueDeclare(QUEUE_NAME, durable, 
			//channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			// publish message, message format is byte array, so we can
			// encode whatever we like here
			String msg = "Hello Owen";
			// basicPublish(Exchange_Name, QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, content)
			// note, above MessageProperties.PERSISTENT_TEXT_PLAIN is used when the queue is durable
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			System.out.println(String.format("%s is sent!", msg));
			
			// close the channel
			channel.close();
		}
		catch (IOException | TimeoutException e)
		{
			e.printStackTrace();
		}
    }
}
