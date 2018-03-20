package ecashie.controller.exception;

import ecashie.ExitApp;

public class ResourceBundleException extends Exception
{
	private static final long serialVersionUID = -5654953668816500336L;
	
	public static final String messageHeader = "Error with resource bundle!";
	public static final String messageContent = "It wasn't possible to use a specific resource bundle! Would you like to report this bug now?";

	public ResourceBundleException()
	{
		String messageKey = "---";

		GeneralExceptionHandler.logException(this, messageKey);

		ExitApp.exit();
	}
}
