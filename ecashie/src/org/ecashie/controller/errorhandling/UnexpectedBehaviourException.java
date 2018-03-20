package org.ecashie.controller.errorhandling;

import org.ecashie.MainAppController;

public class UnexpectedBehaviourException extends Exception
{
	private static final long serialVersionUID = 1519934566969918754L;

	public UnexpectedBehaviourException()
	{
		String messageKey = "exception.foundBug";
		
		GeneralExceptionHandler.logException(this, messageKey);
		
		MainAppController.exitApplication();
	}
}
