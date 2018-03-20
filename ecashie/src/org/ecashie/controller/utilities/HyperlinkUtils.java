package org.ecashie.controller.utilities;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.ecashie.controller.errorhandling.UnexpectedBehaviourException;

public class HyperlinkUtils
{
	public static void openHyperlink(String hyperlinkText)
	{
		try
		{
			URI uriHyperlinkText = generateUriFromString(hyperlinkText);

			if (Desktop.isDesktopSupported())
			{
				desktopIsSupported(uriHyperlinkText);
			}
			else
			{
				desktopIsNotSupported(hyperlinkText);
			}
		}
		catch (URISyntaxException e)
		{
			new UnexpectedBehaviourException();
		}
		catch (IOException e)
		{
			desktopIsNotSupported(hyperlinkText);
		}
	}

	private static void desktopIsSupported(URI uriLastReleaseHyperlinkText) throws IOException
	{
		// For some problems with class "Desktop" see this thread:
		// http://stackoverflow.com/questions/5226212/how-to-open-the-default-webbrowser-using-java
		Desktop.getDesktop().browse(uriLastReleaseHyperlinkText);
	}

	private static void desktopIsNotSupported(String lastReleaseHyperlinkText)
	{
		GeneralOperations.copyToClipboard(lastReleaseHyperlinkText);

		String messageKey = "copyToClipboard.url";

		Notification.info(messageKey);
	}

	public static URI generateUriFromString(String uriString) throws URISyntaxException
	{
		return new URI(uriString);
	}

	public static URI generateUriFromUrl(URL resource) throws URISyntaxException
	{
		return resource.toURI();
	}
}
