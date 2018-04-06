package ecashie.main;

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
		closeDatabase();

		writeUserSettings();

		packEncryptAppendWriteDatabase();

		deleteUserDataFolder();

		writeAppSettings();

		closeServerSocket();

		Platform.runLater(new Runnable()
		{

			@Override
			public void run()
			{
				closeLoadingStage();

				closePrimaryStage();

				showAlertMessage();

				closeLogger();

				Platform.exit();
				System.exit(0);
			}

		});
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
			catch (Exception e)
			{
				ExitApplicationFailedException.CloseDatabaseFailed = true;

				new ExitApplicationFailedException(e);
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
			catch (Exception e)
			{
				ExitApplicationFailedException.PackEncryptAppendWriteFailed = true;

				new ExitApplicationFailedException(e);
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
			catch (Exception e)
			{
				ExitApplicationFailedException.WriteUserSettingsFailed = true;

				new ExitApplicationFailedException(e);
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
			catch (Exception e)
			{
				ExitApplicationFailedException.DeleteUserDataFolderFailed = true;

				new ExitApplicationFailedException(e);
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
			catch (Exception e)
			{
				ExitApplicationFailedException.WriteAppSettings = true;

				new ExitApplicationFailedException(e);
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
			catch (Exception e)
			{
				ExitApplicationFailedException.CloseServerSocketFailed = true;

				new Exception();
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
			catch (Exception e)
			{
				ExitApplicationFailedException.CloseLoggerFailed = true;

				new ExitApplicationFailedException(e);
			}
		}
	}

	private static void closeLoadingStage()
	{
		if (AppLoader.loaderStage != null)
		{
			AppLoader.loaderStage.close();
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
