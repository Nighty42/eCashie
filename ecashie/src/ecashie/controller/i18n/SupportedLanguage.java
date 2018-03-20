package ecashie.controller.i18n;

import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SupportedLanguage
{
	public static final SupportedLanguage en_GB = new SupportedLanguage(new Locale("en", "GB"));
	public static final SupportedLanguage de_DE = new SupportedLanguage(new Locale("de", "DE"));

	public static ObservableList<SupportedLanguage> getList()
	{
		return FXCollections.observableArrayList(de_DE, en_GB);
	}

	private Locale locale = null;

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public SupportedLanguage(Locale locale)
	{
		this.locale = locale;
	}

	@Override
	public String toString()
	{
		return locale.getDisplayLanguage();
	}
}
