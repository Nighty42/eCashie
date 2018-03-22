package ecashie.controller.exception;

import java.io.IOException;

import ecashie.controller.settings.UserData;
import ecashie.controller.utilities.FileOperations;
import ecashie.main.ExitApp;

public class DatabaseBuildException extends Exception
{
	private static final long serialVersionUID = -3946494694938955621L;

	public DatabaseBuildException(Exception exception)
	{
		String messageKey = "exception.foundBug";

		GeneralExceptionHandler.logException(exception, messageKey);

		try
		{
			FileOperations.forceDeleteFile(UserData.getCashJournalFile());
			FileOperations.forceDeleteFolder(UserData.getDatabaseFolder());
		}
		catch (IOException e)
		{
			new UnexpectedBehaviourException();
		}
		
		ExitApp.exit();
	}
}
