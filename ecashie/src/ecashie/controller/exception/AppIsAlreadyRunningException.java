package ecashie.controller.exception;

public class AppIsAlreadyRunningException extends UnexpectedBehaviourException
{
	private static final long serialVersionUID = -4114165736195360458L;

	public AppIsAlreadyRunningException(Exception e)
	{
		super(e);
	}
}
