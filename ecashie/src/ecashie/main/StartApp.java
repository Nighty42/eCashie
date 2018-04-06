package ecashie.main;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import ecashie.controller.appdetails.AppDetails;
import ecashie.controller.exception.LoggingNotAvailableException;
import ecashie.controller.i18n.LanguageController;
import ecashie.controller.logging.ApplicationLogger;
import ecashie.controller.settings.AppSettings;

public class StartApp
{
	public static void start() throws Exception
	{
		AppLoader.notifyPreloader(25, "Initialize Logging and Settings");

		initAppSettings();
		
		initLogging();

		AppLoader.notifyPreloader(65, "Read Application Settings");
		
		readAppDetails();

		readAppSettings();

		AppLoader.notifyPreloader(90, "Initialize Security Provider and Language");

		initSecurityProvider();

		initLanguage();
	}

	private static void initAppSettings()
	{
		try
		{
			AppSettings.init();
		}
		catch (Exception e)
		{
			new LoggingNotAvailableException(e);
		}
	}

	private static void initLogging()
	{
		try
		{
			ApplicationLogger.setup();
		}
		catch (Exception e)
		{
			new LoggingNotAvailableException(e);
		}
	}

	private static void readAppDetails() throws Exception
	{
		AppDetails.read();
	}

	private static void readAppSettings() throws Exception
	{
		AppSettings.read();
	}

	private static void initSecurityProvider() throws Exception
	{
		Security.addProvider(new BouncyCastleProvider());
	}

	private static void initLanguage() throws Exception
	{
		LanguageController.changeLanguage(AppSettings.Language);
	}
}
