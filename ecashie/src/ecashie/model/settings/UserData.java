package ecashie.model.settings;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import ecashie.controller.errorhandling.UnexpectedBehaviourException;
import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.utilities.AlertMessage;
import ecashie.controller.utilities.FileOperations;
import ecashie.view.inputfields.general.TextFieldController;
import javafx.scene.control.ButtonType;

public class UserData
{
	// File which holds all the encrypted data for one user-specific cash journal
	private static File CashJournalFile;
	private static File CashJournalFileTemp;

	public static File getCashJournalFile()
	{
		return CashJournalFile;
	}

	public static void setCashJournalFile(File cashJournalFile)
	{
		// Save old file path in case of renaming
		CashJournalFileTemp = CashJournalFile;

		if (CashJournalFile == null || !CashJournalFile.equals(cashJournalFile))
		{
			CashJournalFile = cashJournalFile;

			try
			{
				createUserDataFolder();
				createCashJournalFile();

				// Update window title to new file name
				GuiBuilder.updateWindowTitle();

				// Rename file if neccessary
				if (CashJournalFileTemp != null && !CashJournalFileTemp.equals(CashJournalFile))
				{
					FileOperations.moveFile(CashJournalFileTemp, CashJournalFile);
					CashJournalFileTemp = null;
				}
			}
			catch (IOException e)
			{
				new UnexpectedBehaviourException();
			}
		}
	}

	// Folder which holds the files that must be encrypted at application exit
	private static File DatabaseFolder = new File(".database");

	public static File getDatabaseFolder()
	{
		return DatabaseFolder;
	}

	// File for database operations
	private static File DatabaseFile = new File(DatabaseFolder + "\\database.ecdb");

	public static File getDatabaseFile()
	{
		return DatabaseFile;
	}

	public static void setDatabaseFile(File databaseFile)
	{
		if (!DatabaseFile.equals(databaseFile))
		{
			DatabaseFile = databaseFile;
		}
	}

	// User-specific password for generation of Key Encryption Key (KEK) and
	// Verification Key (VK)
	private static String Password = "";

	public static String getPassword()
	{
		return Password;
	}

	public static void setPassword(String password)
	{
		Password = password;
	}

	public static byte[] readExistentUserData()
	{
		byte[] bytesUserDataFile = null;

		try
		{
			bytesUserDataFile = FileOperations.readFileToByteArray(CashJournalFile);
		}
		catch (IOException e)
		{
			new UnexpectedBehaviourException();
		}

		return bytesUserDataFile;
	}

	private static void createUserDataFolder() throws SecurityException
	{
		FileOperations.createFolder(DatabaseFolder);

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				FileOperations.forceDeleteFolderOnExit(DatabaseFolder);
			}
		});
	}

	private static void createCashJournalFile() throws IOException
	{
		FileOperations.createFile(CashJournalFile);
	}

	public static void writeBytesToUserFile(File userDataFile2, byte[] encryptedAppendedBytes) throws IOException
	{
		FileOperations.writeByteArrayToFile(CashJournalFile, encryptedAppendedBytes);
	}

	public static String getCashJournalName()
	{
		String cashJournalName = "";

		if (CashJournalFile != null)
		{
			int startIndex = CashJournalFile.getAbsolutePath().lastIndexOf("\\") + 1;
			int endIndex = CashJournalFile.getAbsolutePath().lastIndexOf(".");

			cashJournalName = CashJournalFile.getAbsolutePath().substring(startIndex, endIndex);
		}

		return cashJournalName;
	}

	public static void destroyPassword()
	{
		Password = null;
	}

	public static boolean handleUserDataFileExists(TextFieldController fileNameFieldController, File userDataFile)
	{
		if (userDataFile.exists())
		{
			Optional<ButtonType> result = showAlertReplaceExistentUserDataFile(fileNameFieldController);

			if (result.get() == ButtonType.YES)
			{
				replaceUserDataFile(userDataFile);
			}
			else
			{
				return false;
			}
		}

		return true;
	}

	private static Optional<ButtonType> showAlertReplaceExistentUserDataFile(
			TextFieldController fileNameFieldController)
	{
		return AlertMessage.yesNo("overwriteFile", new String[] { fileNameFieldController.getInputField().getText() });
	}

	private static void replaceUserDataFile(File userDataFile)
	{
		try
		{
			FileOperations.forceDeleteFile(userDataFile);
		}
		catch (IOException e)
		{
			new UnexpectedBehaviourException();
		}
	}
}
