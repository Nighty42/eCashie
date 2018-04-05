package ecashie.controller.database;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ecashie.controller.crypto.CryptoBytes;
import ecashie.controller.crypto.CryptoEngine;
import ecashie.controller.exception.DatabaseAccessException;
import ecashie.controller.exception.DatabasePasswordInvalidException;
import ecashie.controller.exception.UnexpectedBehaviourException;
import ecashie.controller.settings.UserData;
import ecashie.controller.settings.UserSettings;
import ecashie.controller.utilities.ZipOperations;
import ecashie.main.AppPreloader;

public class DatabaseAccess
{
	public static Connection Connection = null;
	public static Statement Statement = null;

	// ================================================================================
	// OPEN
	// ================================================================================

	public static void openDatabase(boolean newDatabase) throws DatabasePasswordInvalidException
	{
		try
		{
			loadJDBCDriver();
			
			if (newDatabase)
			{
				openNewDatabase();
			}
			else
			{
				openExistentDatabase();
			}
		}
		catch (ClassNotFoundException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | SQLException
				| IllegalArgumentException | ParserConfigurationException | SAXException | IOException e)
		{
			new UnexpectedBehaviourException();
		}
	}

	private static void openExistentDatabase()
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException, IOException, SQLException,
			DatabasePasswordInvalidException, IllegalArgumentException, ParserConfigurationException, SAXException
	{
		AppPreloader.notifyPreloader(10, "Read encrypted User Data");
		
		byte[] appendedEncryptedPackedBytes = UserData.readExistentUserData();

		AppPreloader.notifyPreloader(20, "Subtract compressed Bytes");

		byte[] encryptedPackedBytes = CryptoBytes.subtract(appendedEncryptedPackedBytes);

		AppPreloader.notifyPreloader(60, "Decrypt Data");

		byte[] packedBytes = CryptoEngine.decrypt(encryptedPackedBytes, UserData.getPassword());

		AppPreloader.notifyPreloader(70, "Decompress Bytes and read User Settings");
		
		ZipOperations.unpack(packedBytes);
		
		UserSettings.read();
		
		AppPreloader.notifyPreloader(90, "Establish Database Connection");
		
		DatabaseAccess.establishDatabaseConnection();		
	}

	private static void openNewDatabase() throws SQLException
	{
		CryptoEngine.generateDBCryptKey();

		DatabaseAccess.establishDatabaseConnection();

		DatabaseBuilder.createTables();

		DatabaseAccess.setPassword();
	}

	private static void loadJDBCDriver() throws ClassNotFoundException
	{
		Class.forName("org.hsqldb.jdbcDriver");
	}

	public static void establishDatabaseConnection()
	{
		// Define URL for DatabaseAccess-Connection
		// Note: For more information about the encryption HSQLDB uses visit
		// this link: https://bz.apache.org/ooo/show_bug.cgi?id=115454
		String url = "jdbc:hsqldb:File:" + UserData.getDatabaseFile() + ";shutdown=true;crypt_key="
				+ CryptoEngine.CryptKey + ";crypt_type=aes;crypt_lobs=true;reconfig_logging=false";

		try
		{
			Connection = DriverManager.getConnection(url, "SA", UserData.getPassword());
			Statement = Connection.createStatement();
		}
		catch (SQLException e)
		{
			new DatabaseAccessException(e);
		}
	}

	// ================================================================================
	// CLOSE
	// ================================================================================

	public static void closeDatabase(Connection connection, Statement statement) throws SQLException
	{
		try
		{
			closeDatabaseStatement(statement);
		}
		finally
		{
			closeDatabaseConnection(connection);
		}
	}

	private static void closeDatabaseStatement(Statement statement) throws SQLException
	{
		if (statement != null && !statement.isClosed())
		{
			statement.execute("SHUTDOWN");
		}
	}

	private static void closeDatabaseConnection(Connection connection) throws SQLException
	{
		if (connection != null && !connection.isClosed())
		{
			connection.close();
		}
	}

	public static void packEncryptAppendWriteDatabase()
			throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NullPointerException
	{
		if (DatabaseAccess.Connection != null)
		{
			byte[] decryptedBytes = ZipOperations.pack(UserData.getDatabaseFolder());

			byte[] encryptedBytes = CryptoEngine.encrypt(decryptedBytes, UserData.getPassword());

			byte[] encryptedAppendedBytes = CryptoBytes.append(encryptedBytes);

			UserData.writeBytesToUserFile(UserData.getCashJournalFile(), encryptedAppendedBytes);
		}
	}

	public static void setPassword()
	{
		// TODO: Possible SQL Injection
		String sql = "ALTER USER SA SET PASSWORD '" + UserData.getPassword() + "';";

		try
		{
			Statement.execute(sql);
		}
		catch (SQLException e)
		{
			new DatabaseAccessException(e);
		}
	}
}
