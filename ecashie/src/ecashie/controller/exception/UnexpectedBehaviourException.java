package ecashie.controller.exception;

import ecashie.main.AppLoader;
import ecashie.main.ExitApp;

public class UnexpectedBehaviourException extends Exception
{
	private static final long serialVersionUID = 1519934566969918754L;

	public UnexpectedBehaviourException(Exception e)
	{
		AppLoader.IsFailed = true;
		
		if (e != null)
		{
			GeneralExceptionHandler.logException(e);
		}

		String messageKey = "exception.foundBug";

		GeneralExceptionHandler.logException(this, messageKey);

		ExitApp.exit();
	}
}
