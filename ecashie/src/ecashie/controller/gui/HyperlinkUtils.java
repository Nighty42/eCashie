package ecashie.controller.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import ecashie.controller.exception.UnexpectedBehaviourException;
import ecashie.controller.utilities.GeneralOperations;

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
		catch (IOException e)
		{
			desktopIsNotSupported(hyperlinkText);
		}
		catch (Exception e)
		{
			new UnexpectedBehaviourException(e);
		}

	}

	private static void desktopIsSupported(URI uriLastReleaseHyperlinkText) throws Exception
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

	public static URI generateUriFromString(String uriString) throws Exception
	{
		return new URI(uriString);
	}

	public static URI generateUriFromUrl(URL resource) throws Exception
	{
		return resource.toURI();
	}
}
