package ecashie.model.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;

import ecashie.controller.exception.ResourceBundleException;
import ecashie.controller.exception.UnexpectedBehaviourException;
import ecashie.main.MainApp;

public class ResourceBundleString
{
	private String messageKey = "";

	public String getMessageKey()
	{
		return messageKey;
	}

	public void setMessageKey(String messageKey)
	{
		this.messageKey = messageKey;
	}

	private String messageHeader = "";

	public String getMessageHeader()
	{
		return messageHeader;
	}

	public void setMessageHeader(String messageHeader)
	{
		this.messageHeader = messageHeader;
	}

	private String messageContent = "";

	public String getMessageContent()
	{
		return messageContent;
	}

	public void setMessageContent(String messageContent)
	{
		this.messageContent = messageContent;
	}

	public ResourceBundleString(String messageKey, String messageHeader, String messageContent)
	{
		this.messageKey = messageKey;
		this.messageHeader = messageHeader;
		this.messageContent = messageContent;
	}

	public static String getLocaleString(String messageKey, String[] messageArgs)
	{
		String localeString = "";

		try
		{
			localeString = MainApp.ResourceBundle.getString(messageKey);

			if (messageArgs != null)
			{
				localeString = formatMessageString(localeString, messageArgs);
			}
		}
		catch (NullPointerException | MissingResourceException e)
		{
			new ResourceBundleException();
		}

		return localeString;
	}

	private static String formatMessageString(String localeString, String[] messageArgs)
	{
		try
		{
			MessageFormat messageFormat = new MessageFormat("");
			messageFormat.applyPattern(localeString);
			localeString = messageFormat.format(messageArgs);
		}
		catch (IllegalArgumentException e)
		{
			new UnexpectedBehaviourException();
		}

		return localeString;
	}

	public static ResourceBundleString getLocaleMessage(String messageKey, String[] messageArgs)
	{
		String messageHeader = ResourceBundleString.getLocaleString(messageKey + ".header", null);
		String messageContent = ResourceBundleString.getLocaleString(messageKey + ".content", null);

		ResourceBundleString resourceBundleString = new ResourceBundleString(messageKey, messageHeader, messageContent);

		return resourceBundleString;
	}
}
