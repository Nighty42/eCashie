package ecashie;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.xml.sax.SAXException;

import ecashie.controller.database.DatabaseAccess;
import ecashie.controller.errorhandling.ExitApplicationFailedException;
import ecashie.controller.errorhandling.GeneralExceptionHandler;
import ecashie.controller.errorhandling.LoggingNotAvailableException;
import ecashie.controller.errorhandling.ResourceBundleException;
import ecashie.controller.errorhandling.UnexpectedBehaviourException;
import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.internationalization.LanguageUtils;
import ecashie.controller.internationalization.ResourceBundleString;
import ecashie.controller.logging.ApplicationLogger;
import ecashie.controller.utilities.FileOperations;
import ecashie.model.appdetails.AppDetails;
import ecashie.model.settings.AppSettings;
import ecashie.model.settings.UserData;
import ecashie.model.settings.UserSettings;
import javafx.stage.Stage;

public class MainAppController
{
	public static ResourceBundle ResourceBundle;

	public static void initialize(Stage primaryStage)
	{
		initializeLogging();

		try
		{
			readAppDetails();

			readAppSettings();

			initializeSecurityProvider();

			initializeApplicationLanguage();

			initializePrimaryStage(primaryStage);
		}
		catch (Exception e)
		{
			new UnexpectedBehaviourException();
		}
	}

	private static void initializePrimaryStage(Stage primaryStage)
	{
		GuiBuilder.primaryStage = primaryStage;
		GuiBuilder.initializePrimaryStage();
	}

	private static void initializeLogging()
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

	private static void initializeSecurityProvider() throws NullPointerException, SecurityException
	{
		Security.addProvider(new BouncyCastleProvider());
	}

	private static void initializeApplicationLanguage() throws IOException
	{				
		LanguageUtils.changeLanguage(AppSettings.language);
	}

	private static void readAppDetails()
			throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException
	{
		AppDetails.read();
	}

	private static void readAppSettings()
			throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException
	{
		AppSettings.read();
	}

	public static void exitApplication()
	{
		GuiBuilder.primaryStage.close();
		
		closeDatabase();

		writeUserSettings();

		packEncryptAppendWriteDatabase();

		deleteUserDataFolder();

		writeAppSettings();

		closeServerSocket();

		showAlertMessage();

		closeLogger();

		System.exit(0);
	}

	private static void closeDatabase()
	{
		if (!ExitApplicationFailedException.CloseDatabaseFailed)
		{
			try
			{
				DatabaseAccess.closeDatabase(DatabaseAccess.Connection, DatabaseAccess.Statement);
			}
			catch (SQLException e)
			{
				ExitApplicationFailedException.CloseDatabaseFailed = true;

				new ExitApplicationFailedException();
			}
		}
	}

	private static void packEncryptAppendWriteDatabase()
	{
		if (!ExitApplicationFailedException.PackEncryptAppendWriteFailed)
		{
			try
			{
				DatabaseAccess.packEncryptAppendWriteDatabase();
			}
			catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
					| BadPaddingException | InvalidAlgorithmParameterException | IOException e)
			{
				ExitApplicationFailedException.PackEncryptAppendWriteFailed = true;

				new ExitApplicationFailedException();
			}
		}
	}
	
	private static void writeUserSettings()
	{
		if (!ExitApplicationFailedException.WriteUserSettingsFailed)
		{
			try
			{
				UserSettings.write();
			}
			catch (ParserConfigurationException | TransformerException | IOException e)
			{
				ExitApplicationFailedException.WriteUserSettingsFailed = true;

				new ExitApplicationFailedException();
			}
		}
	}

	public static void deleteUserDataFolder()
	{
		if (!ExitApplicationFailedException.DeleteUserDataFolderFailed)
		{
			try
			{
				FileOperations.forceDeleteFolder(UserData.getDatabaseFolder());
			}
			catch (IOException e)
			{
				ExitApplicationFailedException.DeleteUserDataFolderFailed = true;

				new ExitApplicationFailedException();
			}
		}
	}

	private static void writeAppSettings()
	{
		if (!ExitApplicationFailedException.WriteAppSettings)
		{
			try
			{
				AppSettings.write();
			}
			catch (IOException | ParserConfigurationException | TransformerException e)
			{
				ExitApplicationFailedException.WriteAppSettings = true;

				new ExitApplicationFailedException();
			}
		}
	}

	private static void closeServerSocket()
	{
		if (!ExitApplicationFailedException.CloseServerSocketFailed)
		{
			try
			{
				MainApp.closeServerSocket();
			}
			catch (IOException e)
			{
				ExitApplicationFailedException.CloseServerSocketFailed = true;

				new ExitApplicationFailedException();
			}
		}
	}

	private static void closeLogger()
	{
		if (!ExitApplicationFailedException.CloseLoggerFailed)
		{
			try
			{
				ApplicationLogger.closeLogger();
			}
			catch (SecurityException e)
			{
				ExitApplicationFailedException.CloseLoggerFailed = true;

				new ExitApplicationFailedException();
			}
		}
	}

	private static void showAlertMessage()
	{
		if (!ApplicationLogger.getLogAsString().isEmpty())
		{
			ResourceBundleString resourceBundleString = null;

			if (ApplicationLogger.getLogAsString().contains("ResourceBundleException"))
			{
				resourceBundleString = new ResourceBundleString("---", ResourceBundleException.messageHeader,
						ResourceBundleException.messageContent);
			}
			else
			{
				resourceBundleString = ResourceBundleString.getLocaleMessage("exception.foundBug", null);
			}

			GeneralExceptionHandler.generateAlert(resourceBundleString.getMessageHeader(),
					resourceBundleString.getMessageContent(), "https://github.com/mariusraht1/eCashie/issues/new");
		}
	}
}
