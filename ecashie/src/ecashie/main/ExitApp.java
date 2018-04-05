package ecashie.main;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import ecashie.controller.database.DatabaseAccess;
import ecashie.controller.exception.ExitApplicationFailedException;
import ecashie.controller.exception.GeneralExceptionHandler;
import ecashie.controller.exception.ResourceBundleException;
import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.i18n.LanguageController;
import ecashie.controller.logging.ApplicationLogger;
import ecashie.controller.settings.AppSettings;
import ecashie.controller.settings.UserData;
import ecashie.controller.settings.UserSettings;
import ecashie.controller.utilities.FileOperations;
import ecashie.controller.utilities.SocketListener;
import ecashie.model.i18n.ResourceBundleString;
import javafx.application.Platform;

public class ExitApp
{
	public static void exit()
	{
		closePrimaryStage();

		closeDatabase();

		writeUserSettings();

		packEncryptAppendWriteDatabase();

		deleteUserDataFolder();

		writeAppSettings();

		closeServerSocket();

		showAlertMessage();

		closeLogger();

		Platform.exit();
		System.exit(0);
	}

	private static void closePrimaryStage()
	{
		if (GuiBuilder.PrimaryStage != null)
		{
			GuiBuilder.PrimaryStage.close();
		}
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
					| BadPaddingException | InvalidAlgorithmParameterException | IOException | NullPointerException e)
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
				SocketListener.closeServerSocket();
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
				resourceBundleString = LanguageController.getLocaleMessage("exception.foundBug", null);
			}

			GeneralExceptionHandler.generateAlert(resourceBundleString.getMessageHeader(),
					resourceBundleString.getMessageContent(), "https://sourceforge.net/p/ecashie/tickets/new/");
		}
	}
}
