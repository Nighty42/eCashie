package ecashie.controller.utilities;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.IllegalBlockingModeException;

import ecashie.controller.exception.AppIsAlreadyRunningException;
import ecashie.controller.gui.GuiBuilder;
import javafx.application.Platform;

public class SocketListener
{
	private static int port = 9999;
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;

	public SocketListener() throws UnknownHostException, IOException
	{
		serverSocket = new ServerSocket(port, 0, InetAddress.getLocalHost());
	}

	public static void serverSocketListener()
	{
		while (true)
		{
			try
			{
				clientSocket = serverSocket.accept();

				if (clientSocket.isConnected())
				{
					clientSocket.close();

					Platform.runLater(() -> {
						// Windows: ToFront() just let the icon in the taskbar blink
						GuiBuilder.primaryStage.setAlwaysOnTop(true);
						GuiBuilder.primaryStage.requestFocus();
						GuiBuilder.primaryStage.setAlwaysOnTop(false);
					});
				}
			}
			catch (IllegalBlockingModeException | SecurityException e)
			{
				new AppIsAlreadyRunningException();
			}
			catch (IOException e)
			{
				// Just ignore this due to no attempt from other applications to connect with
				// the server socket.
			}
		}
	}

	public static void clientSocketListener()
	{
		try
		{
			clientSocket = new Socket(InetAddress.getLocalHost(), port);
		}
		catch (SecurityException | NullPointerException | IllegalArgumentException | IOException e)
		{
			new AppIsAlreadyRunningException();
		}
	}

	public static void closeServerSocket() throws IOException
	{
		if (serverSocket != null)
		{
			serverSocket.close();
		}
	}
}
