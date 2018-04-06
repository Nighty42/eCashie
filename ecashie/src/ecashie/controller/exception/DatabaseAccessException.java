package ecashie.controller.exception;

public class DatabaseAccessException extends UnexpectedBehaviourException
{
	private static final long serialVersionUID = 667743859174770270L;

	public DatabaseAccessException(Exception e)
	{
		super(e);
	}
}
