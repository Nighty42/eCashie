package org.ecashie;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.IllegalBlockingModeException;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.ecashie.controller.errorhandling.AppIsAlreadyRunningException;
import org.ecashie.controller.gui.GuiBuilder;
import org.ecashie.controller.gui.Navigation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class MainApp extends Application
{
	private static int port = 9999;
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws UnknownHostException, IOException
	{
		// DEBUG: Create/Open Database
		// MainAppController.initialize(primaryStage);
		//
		// UserDataController.CashJournalFile = new File(System.getProperty("user.dir")
		// + "\\MyCashJournal.ecdb");
		// UserDataController.Password = "1234";
		//
		// DatabaseAccess.openDatabase();
		//
		// TestDatabaseInterface.doTests();
		//
		// MainAppController.exitApplication();

		// Map<Currency, Locale> map = getCurrencyLocaleMap();
		// String countryCode = "AL";
		//
		// Locale locale = new Locale("EN", countryCode);
		// Currency currency = Currency.getInstance(locale);
		// String symbol = currency.getSymbol(map.get(currency));
		// System.out.println("For country " + countryCode + ", currency symbol is " +
		// symbol);

		if (catchAppIsAlreadyRunning())
		{
			System.exit(0);
		}
		else
		{
			MainAppController.initialize(primaryStage);

			// DEBUG: MainScene
			// Navigation.addBefore("StartScene");
			//
			// Navigation.Next = "MainScene";
			// Navigation.goForward();

			// DEBUG: StartScene
			Navigation.Next = "StartScene";
			Navigation.goForward();
		}
	}

	public static Map<Currency, Locale> getCurrencyLocaleMap()
	{
		Map<Currency, Locale> map = new HashMap<Currency, Locale>();

		for (Locale locale : Locale.getAvailableLocales())
		{
			try
			{
				Currency currency = Currency.getInstance(locale);
				map.put(currency, locale);
			}
			catch (Exception e)
			{
				// skip strange locale
				continue;
			}
		}

		return map;
	}

	private static boolean catchAppIsAlreadyRunning()
	{
		try
		{
			serverSocket = new ServerSocket(port, 0, InetAddress.getLocalHost());

			Thread threadConnectionListener = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					serverSocketListener();
				}
			});

			threadConnectionListener.start();
		}
		catch (BindException e)
		{
			clientSocketListener();

			return true;
		}
		catch (IOException e)
		{
			return true;
		}

		return false;
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
		serverSocket.close();
	}
}
