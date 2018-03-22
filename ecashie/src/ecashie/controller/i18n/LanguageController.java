package ecashie.controller.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.StringTokenizer;

import ecashie.MainApp;
import ecashie.controller.exception.UnexpectedBehaviourException;
import ecashie.model.settings.AppSettings;

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

	public static void changeLanguage(SupportedLanguage newLanguage)
	{
		try
		{
			AppSettings.Language = newLanguage;

			Locale.setDefault(AppSettings.Language.getLocale());

			InputStream inputStream = MainApp.class.getResourceAsStream(
					"/ecashie/resources/i18n/" + AppSettings.Language.getLocale().toString() + ".properties");

			MainApp.ResourceBundle = new PropertyResourceBundle(inputStream);
		}
		catch (IOException e)
		{
			new UnexpectedBehaviourException();
		}
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
}
