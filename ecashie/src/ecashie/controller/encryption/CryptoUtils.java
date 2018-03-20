package ecashie.controller.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

/*
 * Implementation of the crytographic features Supported encryption specifications: - AES - Threefish - Serpent
 */

public class CryptoUtils
{
	public static List<byte[]> generateSecureRandom(int length)
	{
		byte[] secureBytes = new byte[length];

		SecureRandom r = new SecureRandom();
		r.setSeed(r.generateSeed(length * 8));

		r.nextBytes(secureBytes);

		return Arrays.asList(secureBytes);
	}

	public static List<byte[]> generateAESKey(int length) throws NoSuchAlgorithmException
	{
		byte[] secretKeyBytes = new byte[length];

		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(length * 8);
		SecretKey secretKey = keyGen.generateKey();

		secretKeyBytes = secretKey.getEncoded();

		return Arrays.asList(secretKeyBytes);
	}

	public static List<byte[]> generatePBKDF2Key(byte[] password, byte[] salt, int length)
	{
		byte[] a_b = new byte[length];

		PBEParametersGenerator pbkdf = new PKCS5S2ParametersGenerator(new SHA256Digest());
		pbkdf.init(password, salt, 4096);

		KeyParameter key = (KeyParameter) pbkdf.generateDerivedParameters(length * 8);

		a_b = key.getKey();

		byte[] a = Arrays.copyOfRange(a_b, 0, 16);
		byte[] b = Arrays.copyOfRange(a_b, 16, 32);

		return Arrays.asList(a, b);
	}

	public static List<byte[]> AESCrypto(int encryptMode, byte[] key, byte[] iv, byte[] inputBytes)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException
	{
		SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "AES");
		Cipher cipher = Cipher.getInstance("AES");

		if (iv != null)
		{
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(encryptMode, secretKey, ivSpec);
		}
		else
		{
			cipher.init(encryptMode, secretKey);
		}

		byte[] outputBytes = cipher.doFinal(inputBytes);

		return Arrays.asList(outputBytes);
	}

	public static String generateUUID()
	{
		return UUID.randomUUID().toString();
	}
}
