package ecashie.controller.exception;

import ecashie.main.AppLoader;

public class LoggingNotAvailableException extends Exception
{
	private static final long serialVersionUID = -3974060638161385435L;

	public LoggingNotAvailableException(Exception e)
	{
		AppLoader.IsFailed = true;

		if (e != null)
		{
			GeneralExceptionHandler.logException(e);
		}

		GeneralExceptionHandler.logException(this);
	}
}
