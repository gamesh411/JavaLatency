package com.gamesh411.TCP;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class LatencyClientTCP implements Runnable
{
	private final InetAddress	targetAddress;
	private final int			targetPort;
	private PrintWriter			printWriter;
	private Socket				socket;
	
	public LatencyClientTCP(final InetAddress targetAddress, final int targetPort)
	{
		this.targetAddress = targetAddress;
		this.targetPort = targetPort;
		socket = new Socket();
	}
	
	public LatencyClientTCP(final String targetAddress, final String targetPort) throws UnknownHostException, NumberFormatException
	{
		this.targetAddress = InetAddress.getByName(targetAddress);
		this.targetPort = Integer.parseInt(targetPort);
	}
	
	@Override
	public void run()
	{
		Thread.currentThread().setName("Main Client Thread TCP");
		while (true)
		{
			try
			{
				if (!socket.isConnected())
				{
					socket = new Socket(targetAddress, targetPort);
					printWriter = new PrintWriter(socket.getOutputStream(), true);
				}
			}
			catch (final IOException e)
			{
				System.out.println("couldnt connect or its not connected anymore");
				return;
			}
			
			printWriter.println(System.nanoTime());
			
			try
			{
				Thread.sleep(1000);
			}
			catch (final InterruptedException e)
			{
				try
				{
					System.out.println(Thread.currentThread().getName() + " is exiting gracefully...");
					printWriter.close();
					socket.close();
				}
				catch (final IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
				break;
			}
			
		}
		
	}
}
