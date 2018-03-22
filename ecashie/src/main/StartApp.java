package main;

import java.io.IOException;
import java.net.BindException;
import java.security.Security;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.xml.sax.SAXException;

import ecashie.controller.appdetails.AppDetails;
import ecashie.controller.exception.LoggingNotAvailableException;
import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.i18n.LanguageController;
import ecashie.controller.logging.ApplicationLogger;
import ecashie.controller.settings.AppSettings;
import ecashie.controller.utilities.SocketListener;
import javafx.stage.Stage;

public class StartApp
{
	public static void start(Stage primaryStage)
			throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException, XMLStreamException
	{
		if (appIsAlreadyRunning())
		{
			System.exit(0);
		}
		else
		{
			initAppSettings();

			initLogging();

			readAppDetails();

			readAppSettings();

			initSecurityProvider();

			initLanguage();

			initPrimaryStage(primaryStage);
		}
	}

	private static boolean appIsAlreadyRunning()
	{
		try
		{
			new SocketListener();

			Thread threadConnectionListener = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					SocketListener.serverSocketListener();
				}
			});

			threadConnectionListener.start();
		}
		catch (BindException e)
		{
			SocketListener.clientSocketListener();

			return true;
		}
		catch (IOException e)
		{
			return true;
		}

		return false;
	}

	private static void initAppSettings()
	{
		AppSettings.init();
	}

	private static void initLogging()
	{
		try
		{
			ApplicationLogger.setup();
		}
		catch (IOException e)
		{
			new LoggingNotAvailableException();
		}
	}

	private static void readAppDetails()
			throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException, XMLStreamException
	{
		AppDetails.read();
	}

	private static void readAppSettings()
			throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException
	{
		AppSettings.read();
	}

	private static void initPrimaryStage(Stage primaryStage)
	{
		GuiBuilder.primaryStage = primaryStage;

		GuiBuilder.initializePrimaryStage();
	}

	private static void initSecurityProvider() throws NullPointerException, SecurityException
	{
		Security.addProvider(new BouncyCastleProvider());
	}

	private static void initLanguage() throws IOException
	{
		LanguageController.changeLanguage(AppSettings.Language);
	}
}
