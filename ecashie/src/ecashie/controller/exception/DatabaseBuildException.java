package ecashie.controller.exception;

import ecashie.controller.settings.UserData;
import ecashie.controller.utilities.FileOperations;
import ecashie.main.ExitApp;

public class DatabaseBuildException extends Exception
{
	private static final long serialVersionUID = -3946494694938955621L;

	public DatabaseBuildException(Exception exception)
	{
		GeneralExceptionHandler.logException(exception);

		try
		{
			FileOperations.forceDeleteFile(UserData.getCashJournalFile());
			FileOperations.forceDeleteFolder(UserData.getDatabaseFolder());
		}
		catch (Exception e)
		{
			new UnexpectedBehaviourException(e);
		}
		
		ExitApp.exit();
	}
}
