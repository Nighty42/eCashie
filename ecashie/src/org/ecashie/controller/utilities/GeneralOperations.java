package org.ecashie.controller.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class GeneralOperations
{
	public static String currentYear()
	{
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	}

	public static byte[] toBytes(String text)
	{
		return text.getBytes(StandardCharsets.UTF_8);
	}

	public static String getStackTraceAsString(Throwable throwable)
	{
		StringWriter stringWriter = new StringWriter();

		if (throwable != null)
		{
			throwable.printStackTrace(new PrintWriter(stringWriter));
		}

		return stringWriter.toString();
	}
	
	public static void copyToClipboard(String content)
	{
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent clipboardContent = new ClipboardContent();
		
		clipboardContent.putString(content);
		clipboard.setContent(clipboardContent);
	}
}
