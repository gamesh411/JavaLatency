package com.gamesh411;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.gamesh411.TCP.LatencyClientTCP;
import com.gamesh411.TCP.LatencyServerTCP;
import com.gamesh411.UDP.LatencyClientUDP;
import com.gamesh411.UDP.LatencyServerUDP;

public class MainUI extends JFrame
{
	private static final long	serialVersionUID	= 1L;
	
	public static void main(final String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					final MainUI frame = new MainUI();
					frame.setVisible(true);
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	boolean				serverRunning	= false;
	boolean				clientRunning	= false;
	private Thread		currentServerThread;
	private Thread		currentClientThread;
	
	private JPanel		contentPane;
	private JTextField	txtLatency;
	private JTextField	txtTargetIp;
	private JTextField	txtTargetPort;
	private JTextField	txtServerPort;
	private JButton		btnServerStart;
	private JButton		btnClientStart;
	
	/**
	 * Create the frame.
	 */
	public MainUI()
	{
		buildGUI();
	}
	
	private void buildGUI()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 330, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtTargetIp = new JTextField();
		txtTargetIp.setHorizontalAlignment(SwingConstants.CENTER);
		txtTargetIp.setText("phynaster.no-ip.biz");
		txtTargetIp.setColumns(10);
		txtTargetIp.setBounds(83, 81, 114, 20);
		contentPane.add(txtTargetIp);
		
		final JLabel lblTargetIp = new JLabel("Target:");
		lblTargetIp.setToolTipText("IP address or hostname of the target you want to measure your latency to.");
		lblTargetIp.setBounds(10, 80, 45, 22);
		contentPane.add(lblTargetIp);
		
		txtTargetPort = new JTextField();
		txtTargetPort.setHorizontalAlignment(SwingConstants.CENTER);
		txtTargetPort.setText("4444");
		txtTargetPort.setColumns(10);
		txtTargetPort.setBounds(244, 81, 68, 20);
		contentPane.add(txtTargetPort);
		
		final JLabel lblTargetPort = new JLabel("Port");
		lblTargetPort.setBounds(207, 80, 33, 22);
		contentPane.add(lblTargetPort);
		
		final JLabel lblLatency = new JLabel("Latency");
		lblLatency.setBounds(10, 137, 45, 22);
		contentPane.add(lblLatency);
		
		txtLatency = new JTextField();
		txtLatency.setHorizontalAlignment(SwingConstants.CENTER);
		txtLatency.setEditable(false);
		txtLatency.setBounds(83, 139, 114, 20);
		contentPane.add(txtLatency);
		txtLatency.setColumns(10);
		
		txtServerPort = new JTextField();
		txtServerPort.setHorizontalAlignment(SwingConstants.CENTER);
		txtServerPort.setText("4444");
		txtServerPort.setColumns(10);
		txtServerPort.setBounds(83, 11, 114, 20);
		contentPane.add(txtServerPort);
		
		final JLabel lblServerPort = new JLabel("Server Port:");
		lblServerPort.setToolTipText("Specify the port on which the server listens for connections.");
		lblServerPort.setBounds(10, 10, 68, 22);
		contentPane.add(lblServerPort);
		
		final JRadioButton rdbtnServerTCP = new JRadioButton("TCP");
		rdbtnServerTCP.setSelected(true);
		rdbtnServerTCP.setBounds(87, 38, 53, 23);
		contentPane.add(rdbtnServerTCP);
		
		final JRadioButton rdbtnServerUDP = new JRadioButton("UDP");
		rdbtnServerUDP.setBounds(142, 38, 55, 23);
		contentPane.add(rdbtnServerUDP);
		
		final JLabel lblServerProtocol = new JLabel("Server Port:");
		lblServerProtocol.setToolTipText("Specify the port on which the server listens for connections.");
		lblServerProtocol.setBounds(10, 38, 68, 22);
		contentPane.add(lblServerProtocol);
		
		final JLabel lblClientProtocol = new JLabel("Server Port:");
		lblClientProtocol.setToolTipText("Specify the port on which the server listens for connections.");
		lblClientProtocol.setBounds(10, 108, 68, 22);
		contentPane.add(lblClientProtocol);
		
		final JRadioButton rdbtnClientTCP = new JRadioButton("TCP");
		rdbtnClientTCP.setSelected(true);
		rdbtnClientTCP.setBounds(88, 108, 52, 23);
		contentPane.add(rdbtnClientTCP);
		
		final JRadioButton rdbtnClientUDP = new JRadioButton("UDP");
		rdbtnClientUDP.setBounds(142, 108, 55, 23);
		contentPane.add(rdbtnClientUDP);
		
		final ButtonGroup serverProtocolButtonGroup = new ButtonGroup();
		serverProtocolButtonGroup.add(rdbtnServerTCP);
		serverProtocolButtonGroup.add(rdbtnServerUDP);
		
		final ButtonGroup clientProtocolButtonGroup = new ButtonGroup();
		clientProtocolButtonGroup.add(rdbtnClientTCP);
		clientProtocolButtonGroup.add(rdbtnClientUDP);
		
		btnServerStart = new JButton("Start Server");
		btnServerStart.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				if (!serverRunning)
				{
					final JTextField fieldToUpdate = txtLatency;
					final String port = txtServerPort.getText();
					if (rdbtnServerTCP.isSelected())
					{
						startServerTCP(fieldToUpdate, port);
					}
					else
					{
						startServerUDP(fieldToUpdate, port);
					}
				}
				else
				{
					stopServer();
				}
				
			}
		});
		btnServerStart.setBounds(207, 11, 105, 47);
		contentPane.add(btnServerStart);
		
		btnClientStart = new JButton("Test Latency");
		btnClientStart.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				if (!clientRunning)
				{
					final String targetAddress = txtTargetIp.getText();
					final String port = txtTargetPort.getText();
					if (rdbtnClientTCP.isSelected())
					{
						startClientTCP(targetAddress, port);
					}
					else
					{
						startClientUDP(targetAddress, port);
					}
					
				}
				else
				{
					stopClient();
					
				}
			}
		});
		btnClientStart.setBounds(207, 112, 105, 47);
		contentPane.add(btnClientStart);
		setResizable(false);
		
	}
	
	protected void startClientTCP(final String targetAddress, final String port)
	{
		InetAddress addr;
		int p;
		try
		{
			addr = InetAddress.getByName(targetAddress);
			p = Integer.parseInt(port);
			currentClientThread = new Thread(new LatencyClientTCP(addr, p));
			currentClientThread.start();
			clientRunning = true;
			btnClientStart.setText("Stop Client");
		}
		catch (final UnknownHostException e)
		{
			JOptionPane.showMessageDialog(null, "Couldnt find target, make sure that the hostname is valid, or enter its IP address!");
		}
		catch (final NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, "Port must be an integer!");
		}
		
	}
	
	protected void startClientUDP(final String targetAddress, final String port)
	{
		InetAddress addr;
		int p;
		try
		{
			addr = InetAddress.getByName(targetAddress);
			p = Integer.parseInt(port);
			currentClientThread = new Thread(new LatencyClientUDP(addr, p));
			currentClientThread.start();
			clientRunning = true;
			btnClientStart.setText("Stop Client");
		}
		catch (final UnknownHostException e)
		{
			JOptionPane.showMessageDialog(null, "Couldnt find target, make sure that the hostname is valid, or enter its IP address!");
		}
		catch (final NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, "Port must be an integer!");
		}
		
	}
	
	protected <T> void startServerTCP(final T uiElement, final String port)
	{
		int p;
		try
		{
			p = Integer.parseInt(port);
			currentServerThread = new LatencyServerTCP<>(uiElement, p);
			currentServerThread.start();
			serverRunning = true;
			btnServerStart.setText("Running...");
		}
		catch (final NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, "Port must be an integer!");
		}
	}
	
	protected <T> void startServerUDP(final T uiElement, final String port)
	{
		int p;
		try
		{
			p = Integer.parseInt(port);
			//Here we don't use a runnable, but a Thread for the UDP server, because i had to @Override interrupt()
			currentServerThread = new LatencyServerUDP<>(uiElement, p);
			currentServerThread.start();
			serverRunning = true;
			btnServerStart.setText("Running...");
		}
		catch (final NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, "Port must be an integer!");
		}
	}
	
	protected void stopClient()
	{
		btnClientStart.setEnabled(false);
		btnClientStart.setText("Stopping...");
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				currentClientThread.interrupt();
				try
				{
					currentClientThread.join();
				}
				catch (final InterruptedException e)
				{
					JOptionPane.showMessageDialog(null, "Couldn't stop Client Thread, shutting down...");
					System.exit(ERROR);
				}
				clientRunning = false;
				btnClientStart.setText("Test Latency");
				btnClientStart.setEnabled(true);
			}
		}).start();
	}
	
	protected void stopServer()
	{
		btnServerStart.setEnabled(false);
		btnServerStart.setText("Stopping...");
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				currentServerThread.interrupt();
				try
				{
					currentServerThread.join();
				}
				catch (final InterruptedException e)
				{
					JOptionPane.showMessageDialog(null, "Couldn't stop Server Thread, shutting down...");
					System.exit(ERROR);
				}
				serverRunning = false;
				btnServerStart.setText("Start Server");
				btnServerStart.setEnabled(true);
			}
		}).start();
		
	}
}
