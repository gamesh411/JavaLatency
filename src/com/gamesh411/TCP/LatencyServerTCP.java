package com.gamesh411.TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class LatencyServerTCP<T> extends Thread
{
	public class ServerThread extends Thread
	{
		private final Socket	socket;
		private BufferedReader	bufferedReader;
		private Long			latencyToClientLong;
		
		public ServerThread(final Socket socket)
		{
			this.socket = socket;
			try
			{
				this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			}
			catch (final IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void interrupt()
		{
			System.out.println("Interrupt received");
			super.interrupt();
			//		JOptionPane.showMessageDialog(null, "Server Socket Closing");
			
			try
			{
				socket.close();
			}
			catch (final IOException e)
			{
				System.out.println("Cant Close Serversocket");
			}
		}
		
		@Override
		public void run()
		{
			Thread.currentThread().setName("Processing Server Thread TCP");
			try
			{
				String message;
				while ((message = bufferedReader.readLine()) != null)
				{
					Logger.getAnonymousLogger().info("fdsfd");
					this.latencyToClientLong = (System.nanoTime() - Long.parseLong(message));
					System.out.println("Latency is: " + this.latencyToClientLong);
					final Method showTXT = LatencyServerTCP.this.updateType.getMethod("setText", String.class);
					showTXT.invoke(LatencyServerTCP.this.updateVar, this.latencyToClientLong.toString() + " ms");
				}
			}
			catch (final NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e)
			{
				try
				{
					bufferedReader.close();
					socket.close();
				}
				catch (final IOException e1)
				{
					JOptionPane.showMessageDialog(null, e1.toString());
				}
			}
		}
		
	}
	
	protected ServerSocket	tcpServerSocket;
	public int				port;
	ArrayList<ServerThread>	connections;
	
	protected Class<?>		updateType;
	protected T				updateVar;
	
	public LatencyServerTCP(final T thingToUpdate, final int port)
	{
		updateVar = thingToUpdate;
		updateType = thingToUpdate.getClass();
		updateType.cast(updateVar);
		this.port = port;
		connections = new ArrayList<>();
	}
	
	public LatencyServerTCP(final T thingToUpdate, final String port) throws NumberFormatException
	{
		updateVar = thingToUpdate;
		updateType = thingToUpdate.getClass();
		updateType.cast(updateVar);
		this.port = Integer.parseInt(port);
	}
	
	@Override
	public void interrupt()
	{
		System.out.println("Interrupt received");
		super.interrupt();
		//		JOptionPane.showMessageDialog(null, "Server Socket Closing");
		
		try
		{
			tcpServerSocket.close();
		}
		catch (final IOException e)
		{
			System.out.println("Cant Close Serversocket");
		}
	}
	
	@Override
	public void run()
	{
		Thread.currentThread().setName("Main Server Thread TCP");
		try
		{
			tcpServerSocket = new ServerSocket(port);
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Server up & ready for connections...");
		
		while (!Thread.currentThread().isInterrupted())
		{
			Socket socket;
			try
			{
				socket = tcpServerSocket.accept();
				connections.add(new ServerThread(socket));
				connections.get(connections.size() - 1).start();
			}
			catch (final IOException e)
			{
				System.out.println("Closing Server Connection");
				for (final ServerThread thread : connections)
				{
					thread.interrupt();
				}
				interrupt();
			}
		}
	}
}
