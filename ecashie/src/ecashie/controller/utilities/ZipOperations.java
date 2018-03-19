package ecashie.controller.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import ecashie.controller.errorhandling.UnexpectedBehaviourException;
import ecashie.model.settings.UserData;

public class ZipOperations
{
	// ================================================================================
	// Pack Bytes to ZIP-CashJournalFile
	// ================================================================================

	public static byte[] pack(File inputFile) throws IOException
	{
		byte[] packedBytes = new byte[1];

		if (FileOperations.folderIsValid(inputFile))
		{
			packedBytes = packZipFile(inputFile.toPath());
		}

		return packedBytes;
	}

	private static byte[] packZipFile(Path inputFile) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(baos);

		Files.walkFileTree(inputFile, new SimpleFileVisitor<Path>()
		{
			@Override
			public FileVisitResult visitFile(Path filePath, BasicFileAttributes basicFileAttributes) throws IOException
			{
				zipOutputStream.putNextEntry(new ZipEntry(inputFile.relativize(filePath).toString()));
				Files.copy(filePath, zipOutputStream);
				zipOutputStream.closeEntry();
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path directoryPath, BasicFileAttributes basicFileAttributes)
					throws IOException
			{
				zipOutputStream.putNextEntry(new ZipEntry(inputFile.relativize(directoryPath).toString() + "/"));
				zipOutputStream.closeEntry();
				return FileVisitResult.CONTINUE;
			}
		});

		return baos.toByteArray();
	}

	// ================================================================================
	// Unpack ZIP-CashJournalFile to Bytes
	// ================================================================================

	public static void unpack(byte[] inputBytes) throws IOException
	{
		if (FileOperations.bytesAreValid(inputBytes))
		{
			unpackByteArray(inputBytes);
		}
	}

	private static void unpackByteArray(byte[] inputBytes) throws IOException
	{
		ZipInputStream zipInputStream = null;

		try
		{
			zipInputStream = new ZipInputStream(new ByteArrayInputStream(inputBytes));
			ZipEntry zipEntry = getNextEntry(zipInputStream);

			Path rootDirectory = null;

			while (zipEntry != null)
			{
				String fileName = zipEntry.getName();

				if (fileName.equals("/"))
				{
					rootDirectory = UserData.getDatabaseFolder().toPath();
				}
				else if (!zipEntry.isDirectory())
				{

					zipEntryIsFile(zipInputStream, rootDirectory, fileName);
				}

				zipEntry = getNextEntry(zipInputStream);
			}
		}
		finally
		{
			closeZipInputStream(zipInputStream);
		}
	}

	private static void closeZipInputStream(ZipInputStream zipInputStream) throws IOException
	{
		if (zipInputStream != null)
		{
			zipInputStream.closeEntry();
			zipInputStream.close();
		}
	}

	private static ZipEntry getNextEntry(ZipInputStream zipInputStream)
	{
		ZipEntry zipEntry = null;

		try
		{
			zipEntry = zipInputStream.getNextEntry();
		}
		catch (IOException e)
		{
			new UnexpectedBehaviourException();
		}

		return zipEntry;
	}

	private static void zipEntryIsFile(ZipInputStream zipInputStream, Path subDirectory, String fileName)
	{
		File newFileFromZipEntry = new File(subDirectory + "/" + fileName);
		createNewFileFromZipEntry(newFileFromZipEntry);

		FileOutputStream fileOutputStream = createFileOutputStream(newFileFromZipEntry);

		writeByteArrayToZipFile(zipInputStream, fileOutputStream);

		closeZipOutputStream(fileOutputStream);
	}

	private static void createNewFileFromZipEntry(File newFileFromZipEntry)
	{
		try
		{
			newFileFromZipEntry.createNewFile();
		}
		catch (IOException e)
		{
			new UnexpectedBehaviourException();
		}
	}

	private static void writeByteArrayToZipFile(ZipInputStream zipInputStream, FileOutputStream fileOutputStream)
	{
		try
		{
			byte[] buffer = new byte[1024];
			int bytesLength = 0;

			while ((bytesLength = zipInputStream.read(buffer)) != -1)
			{
				fileOutputStream.write(buffer, 0, bytesLength);
			}
		}
		catch (IOException e)
		{
			new UnexpectedBehaviourException();
		}
	}

	private static FileOutputStream createFileOutputStream(File newFileFromZipEntry)
	{
		FileOutputStream fileOutputStream = null;

		try
		{
			fileOutputStream = new FileOutputStream(newFileFromZipEntry);
		}
		catch (FileNotFoundException e)
		{
			new UnexpectedBehaviourException();
		}

		return fileOutputStream;
	}

	private static void closeZipOutputStream(FileOutputStream fileOutputStream)
	{
		try
		{
			fileOutputStream.close();
		}
		catch (IOException e)
		{
			new UnexpectedBehaviourException();
		}
	}
}
