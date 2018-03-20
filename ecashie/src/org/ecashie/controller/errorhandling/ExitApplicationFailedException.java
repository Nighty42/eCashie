package org.ecashie.controller.errorhandling;

public class ExitApplicationFailedException extends UnexpectedBehaviourException
{
	private static final long serialVersionUID = -1280948717660482188L;

	public static boolean CloseDatabaseFailed = false;
	public static boolean PackEncryptAppendWriteFailed = false;
	public static boolean DeleteUserDataFolderFailed = false;
	public static boolean CloseLoggerFailed = false;
	public static boolean CloseServerSocketFailed = false;
	public static boolean WriteUserSettingsFailed = false;
	public static boolean WriteAppSettings = false;

	public ExitApplicationFailedException()
	{
		super();
	}
}
