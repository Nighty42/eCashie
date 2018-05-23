package ecashie.controller.i18n;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.StringTokenizer;

import ecashie.controller.exception.ResourceBundleException;
import ecashie.controller.exception.UnexpectedBehaviourException;
import ecashie.controller.settings.AppSettings;
import ecashie.main.MainApp;
import ecashie.model.i18n.ResourceBundleString;
import ecashie.model.i18n.SupportedLanguage;

public class LanguageController
{
	public static Locale stringToLocale(String localeID)
	{
		StringTokenizer stringTokenizer = new StringTokenizer(localeID, "_");
		String language = "en";
		String country = "GB";

		if (stringTokenizer.hasMoreTokens())
		{
			language = (String) stringTokenizer.nextElement();
		}

		if (stringTokenizer.hasMoreTokens())
		{
			country = (String) stringTokenizer.nextElement();
		}

		return new Locale(language, country);
	}

	public static void changeLanguage(SupportedLanguage newLanguage) throws Exception
	{
		AppSettings.Language = newLanguage;

		Locale.setDefault(AppSettings.Language.getLocale());

		InputStream inputStream = MainApp.class.getResourceAsStream(
				"/ecashie/resources/i18n/" + AppSettings.Language.getLocale().toString() + ".properties");

		MainApp.ResourceBundle = new PropertyResourceBundle(inputStream);
	}

	public static boolean validateLanguage(Locale locale_A)
	{
		boolean valid = false;

		if (locale_A != null)
		{
			for (SupportedLanguage supportedLanguage : SupportedLanguage.getList())
			{
				if (locale_A.toString().equals(supportedLanguage.getLocale().toString()))
				{
					valid = true;
					break;
				}
			}
		}

		return valid;
	}

	private static String formatMessageString(String localeString, String[] messageArgs)
	{
		try
		{
			MessageFormat messageFormat = new MessageFormat("");
			messageFormat.applyPattern(localeString);
			localeString = messageFormat.format(messageArgs);
		}
		catch (Exception e)
		{
			new UnexpectedBehaviourException(e);
		}

		return localeString;
	}

	public static ResourceBundleString getLocaleMessage(String messageKey)
	{
		String messageHeader = getLocaleString(messageKey + ".header", null);
		String messageContent = getLocaleString(messageKey + ".content", null);

		ResourceBundleString resourceBundleString = new ResourceBundleString(messageKey, messageHeader, messageContent);

		return resourceBundleString;
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
}
