package com.gamesh411.UDP;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.JOptionPane;

public class LatencyServerUDP<T> extends Thread
{
	public class ServerThread extends Thread
	{
		private final DatagramPacket	packet;
		private Long					latencyToClientLong;
		
		public ServerThread(final DatagramPacket packet)
		{
			this.packet = packet;
			
		}
		
		@Override
		public void run()
		{
			setName("ProcessingThread");
			final String message = new String(packet.getData()).trim();
			latencyToClientLong = (System.currentTimeMillis() - Long.parseLong(message));
			System.out.println("Latency is: " + latencyToClientLong);
			try
			{
				Method showTXT;
				showTXT = updateType.getMethod("setText", String.class);
				showTXT.invoke(updateVar, latencyToClientLong.toString() + " ms");
			}
			catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				JOptionPane.showMessageDialog(null, e.toString());
			}
			
		}
	}
	
	public int				port;
	
	protected Class<?>		updateType;
	protected T				updateVar;
	
	private DatagramSocket	udpServerSocket;
	
	public LatencyServerUDP(final T thingToUpdate, final int port)
	{
		updateVar = thingToUpdate;
		updateType = thingToUpdate.getClass();
		updateType.cast(this.updateVar);
		this.port = port;
		
		try
		{
			udpServerSocket = new DatagramSocket(4444);
		}
		catch (final SocketException e)
		{
			JOptionPane.showMessageDialog(null, e.toString());
		}
	}
	
	public LatencyServerUDP(final T thingToUpdate, final String port) throws NumberFormatException
	{
		updateVar = thingToUpdate;
		updateType = thingToUpdate.getClass();
		updateType.cast(this.updateVar);
		this.port = Integer.parseInt(port);
		
		try
		{
			udpServerSocket = new DatagramSocket(4444);
		}
		catch (final SocketException e)
		{
			JOptionPane.showMessageDialog(null, e.toString());
		}
	}
	
	@Override
	public void interrupt()
	{
		System.out.println("Interrupt received");
		super.interrupt();
		//		JOptionPane.showMessageDialog(null, "Server Socket Closing");
		udpServerSocket.close();
	}
	
	@Override
	public void run()
	{
		setName("Main Server Thread");
		System.out.println("Server up & ready for connections...");
		while (!udpServerSocket.isClosed())
		{
			try
			{
				final byte[] data = new byte[1024];
				final DatagramPacket packet = new DatagramPacket(data, data.length);
				udpServerSocket.receive(packet);
				//				System.out.println("Starting packet processing thread");
				new ServerThread(packet).start();
				//				System.out.println("After inner main server loop");
			}
			catch (final SocketException e)
			{
				//				JOptionPane.showMessageDialog(null, e.toString());
			}
			//			System.out.println("After main server loop");
			catch (final IOException e)
			{
				JOptionPane.showMessageDialog(null, e.toString());
			}
			
		}
	}
}
