package ecashie.controller.utilities;

import java.io.File;

import org.apache.commons.io.FileUtils;

import ecashie.controller.exception.UnexpectedBehaviourException;

public class FileOperations
{
	public static boolean fileIsFile(File file)
	{
		if (file.isFile())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean fileExists(File file)
	{
		if (file != null && file.exists())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean fileIsValid(File file)
	{
		if (file.isFile() && fileExists(file))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean fileIsNotEmpty(File file) throws Exception
	{
		byte[] fileBytes = FileUtils.readFileToByteArray(file);

		if (fileBytes.length > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean folderIsValid(File folder)
	{
		if (folder.isDirectory() && fileExists(folder))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean bytesAreValid(byte[] bytes)
	{
		if (bytes != null && bytes.length > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean stringIsValid(String prefix)
	{
		if (prefix != null && !prefix.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static byte[] readFileToByteArray(File file) throws Exception
	{
		byte[] fileOutput = null;

		if (fileIsFile(file) && fileExists(file) && fileIsNotEmpty(file))
		{
			fileOutput = FileUtils.readFileToByteArray(file);
		}

		return fileOutput;
	}

	public static void writeByteArrayToFile(File file, byte[] bytes) throws Exception
	{
		if (fileIsFile(file) && fileExists(file) && bytesAreValid(bytes))
		{
			FileUtils.writeByteArrayToFile(file, bytes);
		}
	}

	public static void forceDeleteFolderOnExit(File folder)
	{
		if (folderIsValid(folder))
		{
			try
			{
				FileUtils.forceDeleteOnExit(folder);
			}
			catch (Exception e)
			{
				new UnexpectedBehaviourException(e);
			}
		}
	}

	public static void forceDeleteFolder(File folder) throws Exception
	{
		if (folderIsValid(folder))
		{
			FileUtils.forceDelete(folder);
		}
	}

	public static void forceDeleteFile(File file) throws Exception
	{
		if (fileIsValid(file))
		{
			FileUtils.forceDelete(file);
		}
	}

	public static void createFile(File file) throws Exception
	{
		if (!file.exists())
		{
			file.createNewFile();
		}
	}

	public static void createFolder(File folder) throws Exception
	{
		if (!folder.exists())
		{
			folder.mkdir();
		}
	}

	public static void moveFile(File oldFile, File newFile) throws Exception
	{
		forceDeleteFile(newFile);
		FileUtils.moveFile(oldFile, newFile);
	}
}
