package org.ecashie.controller.errorhandling;

public class LoggingNotAvailableException extends Exception
{
	private static final long serialVersionUID = -3974060638161385435L;

	public LoggingNotAvailableException()
	{
		String messageKey = "exception.loggingNotAvailable";

		GeneralExceptionHandler.logException(this, messageKey);
	}
}
