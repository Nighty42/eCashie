package ecashie.controller.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import ecashie.controller.utilities.FileOperations;

public class CryptoBytes
{
	/* Contains:
	 * IV, Salt, decrypted DEK, VK, CryptKey 
	 */
	public static byte[] subtract(byte[] bytesOriginal)
	{
		if (FileOperations.bytesAreValid(bytesOriginal))
		{
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytesOriginal);

			inputStream.read(CryptoEngine.IV, 0, CryptoEngine.IV.length);
			bytesOriginal = Arrays.copyOfRange(bytesOriginal, CryptoEngine.IV.length, bytesOriginal.length);

			inputStream.read(CryptoEngine.Salt, 0, CryptoEngine.Salt.length);
			bytesOriginal = Arrays.copyOfRange(bytesOriginal, CryptoEngine.Salt.length, bytesOriginal.length);

			inputStream.read(CryptoEngine.DEK_Enc, 0, CryptoEngine.DEK_Enc.length);
			bytesOriginal = Arrays.copyOfRange(bytesOriginal, CryptoEngine.DEK_Enc.length, bytesOriginal.length);

			inputStream.read(CryptoEngine.VK, 0, CryptoEngine.VK.length);
			bytesOriginal = Arrays.copyOfRange(bytesOriginal, CryptoEngine.VK.length, bytesOriginal.length);
		}
		
		return bytesOriginal;
	}

	public static byte[] append(byte[] bytesOriginal) throws Exception
	{
		ByteArrayOutputStream outputStream = null;

		outputStream = new ByteArrayOutputStream();

		// 16 Byte
		outputStream.write(CryptoEngine.IV);
		// 8 Byte
		outputStream.write(CryptoEngine.Salt);
		// 32 Byte
		outputStream.write(CryptoEngine.DEK_Enc);
		// 16 Byte
		outputStream.write(CryptoEngine.VK);

		outputStream.write(bytesOriginal);

		outputStream.close();

		return outputStream.toByteArray();
	}
}
