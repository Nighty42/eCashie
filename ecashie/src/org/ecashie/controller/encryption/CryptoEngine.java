package org.ecashie.controller.encryption;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.util.Arrays;
import org.ecashie.controller.errorhandling.DatabasePasswordInvalidException;
import org.ecashie.controller.utilities.FileOperations;
import org.ecashie.model.settings.UserData;

public class CryptoEngine
{
	public static byte[] DecryptedBytes = null;
	public static byte[] EncryptedBytes = null;

	// CRYPT_KEY for Database-internal en-/decryption
	public static String CryptKey = "";

	// Initialization Vector: 128 Bit
	public static byte[] IV = new byte[16];

	// Salt: 64 Bit
	public static byte[] Salt = new byte[8];

	// Data Encryption Key: 128 Bit
	public static byte[] DEK = new byte[16];

	// Encrypted Data Encryption Key: 256 Bit
	public static byte[] DEK_Enc = new byte[32];

	// Key Encryption Key: 128 Bit
	public static byte[] KEK = new byte[16];

	// Verification Key: 128 Bit
	public static byte[] VK = new byte[16];

	// ================================================================================
	// ENCRYPTION
	// ================================================================================

	public static byte[] encrypt(byte[] decryptedBytes, String password)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException
	{
		DecryptedBytes = decryptedBytes;

		// Generate IV randomly
		generateIV();

		// Generate Salt randomly
		generateSalt();

		// Generate DEK randomly
		generateDEK();

		// Generate KEK and VK with Password and Salt
		generateKEK_VK();

		// Encrypt data with DEK
		encryptData();

		// Encrypt DEK with KEK and IV
		encryptDEK();

		// Release Password for Garbage Collector
		UserData.destroyPassword();
		
		return EncryptedBytes;
	}

	private static void generateIV()
	{
		List<byte[]> result = CryptoUtils.generateSecureRandom(16);

		IV = result.get(0);
	}

	private static void generateSalt()
	{
		List<byte[]> result = CryptoUtils.generateSecureRandom(8);

		Salt = result.get(0);
	}

	private static void generateDEK() throws NoSuchAlgorithmException
	{
		List<byte[]> result = CryptoUtils.generateAESKey(16);

		DEK = result.get(0);
	}

	private static void generateKEK_VK()
	{
		List<byte[]> result = CryptoUtils
				.generatePBKDF2Key(UserData.getPassword().getBytes(StandardCharsets.UTF_8), Salt, 32);

		KEK = result.get(0);
		VK = result.get(1);
	}

	// Encrypt data with DEK
	private static void encryptData() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
	{
		List<byte[]> result = CryptoUtils.AESCrypto(Cipher.ENCRYPT_MODE, DEK, null, DecryptedBytes);

		EncryptedBytes = result.get(0);
	}

	// Encrypt DEK with KEK and IV
	private static void encryptDEK() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
	{
		List<byte[]> result = CryptoUtils.AESCrypto(Cipher.ENCRYPT_MODE, KEK, IV, DEK);
		DEK_Enc = result.get(0);
	}

	// ================================================================================
	// DECRYPTION
	// ================================================================================

	public static byte[] decrypt(byte[] encryptedBytes, String password)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException, DatabasePasswordInvalidException
	{
		if (FileOperations.bytesAreValid(encryptedBytes))
		{
			EncryptedBytes = encryptedBytes;
			
			if (validPwd())
			{
				decryptDEK();

				decryptData();
			}
			else
			{
				throw new DatabasePasswordInvalidException();
			}
		}

		return DecryptedBytes;
	}

	private static boolean validPwd()
	{
		List<byte[]> result = CryptoUtils
				.generatePBKDF2Key(UserData.getPassword().getBytes(StandardCharsets.UTF_8), Salt, 32);

		byte[] vk_pwd = result.get(1);

		if (Arrays.areEqual(VK, vk_pwd))
		{
			KEK = result.get(0);
			return true;
		}
		else
		{
			UserData.setPassword(null);
			return false;
		}
	}

	// Decrypt DEK with KEK and IV
	private static void decryptDEK() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
	{
		List<byte[]> result = CryptoUtils.AESCrypto(Cipher.DECRYPT_MODE, KEK, IV, DEK_Enc);

		DEK = result.get(0);
	}

	// Decrypt data with DEK
	private static void decryptData() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
	{
		List<byte[]> result = CryptoUtils.AESCrypto(Cipher.DECRYPT_MODE, DEK, null, EncryptedBytes);

		DecryptedBytes = result.get(0);
	}

	// ================================================================================
	// Database-related En-/Decryption
	// ================================================================================

	public static void generateDBCryptKey() throws SQLException
	{
		Connection connection = null;
		Statement statement = null;

		try
		{
			String UUID = CryptoUtils.generateUUID();

			String url = "jdbc:hsqldb:mem:" + UUID + "_tmp";
			connection = DriverManager.getConnection(url);

			statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("CALL CRYPT_KEY('AES', null);");
			rs.next();
			CryptoEngine.CryptKey = rs.getString(1);
		}
		finally
		{
			try
			{
				statement.execute("SHUTDOWN");
			}
			finally
			{
				connection.close();
			}
		}
	}

	// ================================================================================
	// UTILITIES
	// ================================================================================

	public static String getAsString(byte[] input) throws UnsupportedEncodingException
	{
		return new String(input, "UTF-8");
	}
}
