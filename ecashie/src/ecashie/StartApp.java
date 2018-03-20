package ecashie;

import java.io.IOException;
import java.net.BindException;
import java.security.Security;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.xml.sax.SAXException;

import ecashie.controller.exception.LoggingNotAvailableException;
import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.i18n.LanguageUtils;
import ecashie.controller.logging.ApplicationLogger;
import ecashie.controller.utilities.SocketListener;
import ecashie.model.appdetails.AppDetails;
import ecashie.model.settings.AppSettings;
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
			initLogging();

			initAppDetails();

			initAppSettings();

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

	private static void initAppDetails()
			throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException, XMLStreamException
	{
		AppDetails.read();
	}

	private static void initAppSettings()
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
		LanguageUtils.changeLanguage(AppSettings.language);
	}
}
