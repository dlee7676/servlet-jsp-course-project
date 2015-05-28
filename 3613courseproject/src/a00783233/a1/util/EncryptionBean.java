/* David Lee, A00783233 */

package a00783233.a1.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class EncryptionBean {
	public static void encryptFile(String inputFile, String outputFile, String password, byte[] salt, int iterationCount) {
		Vector<Byte> fileBytes = new Vector<Byte>();
		Cipher cipher = null;
		try {
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterationCount);
			
			cipher = Cipher.getInstance("PBEWithMD5AndDES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		try {
			FileInputStream fis = new FileInputStream(new File(inputFile));
			CipherInputStream in = new CipherInputStream(fis, cipher);
			byte contents = (byte)in.read();
			int count = 0;
			while (contents != -1) {
				count++;
				fileBytes.add(new Byte(contents));
				contents = (byte)in.read();
			}
			in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// create array of bytes
		byte[] outputArray = null;
		String result = null;
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(inputFile));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        result = sb.toString();
	        br.close();
	    } 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		try {
			outputArray = result.getBytes("ISO-8859-1");
		}

		// handle UnsupportedEncodingException
		catch (UnsupportedEncodingException exception) {
			exception.printStackTrace();
		}

		// create FileOutputStream
		File file = new File(outputFile);
		FileOutputStream fileOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream(file, false);
		}

		// handle IOException
		catch (IOException e) {
			e.printStackTrace();
		}

		// create CipherOutputStream
		CipherOutputStream out = new CipherOutputStream(fileOutputStream,
				cipher);

		// write contents to file and close
		try {
			out.write(outputArray);
			out.flush();
			out.close();
		}

		// handle IOException
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static InputStream decryptFile(String inputFile, String password, byte[] salt, int iterationCount) {
		Cipher cipher = null;
		try {
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterationCount);
			
			cipher = Cipher.getInstance("PBEWithMD5AndDES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		ByteArrayOutputStream buffer = null; 
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(inputFile));
			buffer = new ByteArrayOutputStream();
			int ch;
			CipherInputStream in = new CipherInputStream(fis, cipher);
			int i = 0;
			while ((ch = in.read()) != -1) {
				byte b = (byte)(ch);
				buffer.write(b);
				i++;
			}
			in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] decryptedText = buffer.toByteArray();
		InputStream result = new ByteArrayInputStream(decryptedText);
		return result;
	}
}
