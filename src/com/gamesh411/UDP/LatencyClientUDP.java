package com.gamesh411.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class LatencyClientUDP implements Runnable
{
	private final InetAddress	targetAddress;
	private final int			targetPort;
	
	private DatagramSocket		udpSocket;
	
	public LatencyClientUDP(final InetAddress targetIp, final int targetPort)
	{
		targetAddress = targetIp;
		this.targetPort = targetPort;
	}
	
	public LatencyClientUDP(final String targetAddress, final String targetPort) throws UnknownHostException, NumberFormatException
	{
		this.targetAddress = InetAddress.getByName(targetAddress);
		this.targetPort = Integer.parseInt(targetPort);
	}
	
	@Override
	public void run()
	{
		try
		{
			Thread.currentThread().setName("UDPTimeSenderThread");
			udpSocket = new DatagramSocket();
			DatagramPacket packet;
			byte[] data = new byte[1024];
			while (!Thread.currentThread().isInterrupted())
			{
				data = new Long(System.currentTimeMillis()).toString().getBytes();
				packet = new DatagramPacket(data, data.length, targetAddress, targetPort);
				udpSocket.send(packet);
				Thread.sleep(500);
			}
			
		}
		catch (final IOException e)
		{
			JOptionPane.showMessageDialog(null, e.toString());
		}
		catch (final InterruptedException e)
		{
		}
		finally
		{
			//JOptionPane.showMessageDialog(null, "Client Socket Closing");
			udpSocket.close();
		}
		
	}
}
