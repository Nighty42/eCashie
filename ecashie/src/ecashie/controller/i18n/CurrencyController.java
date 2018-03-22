package ecashie.controller.i18n;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import ecashie.controller.settings.AppSettings;
import ecashie.model.i18n.SupportedCurrency;
import ecashie.view.inputfields.CurrencySymbolPosition;

public class CurrencyController
{
	public static Currency stringToCurrency(String currencyID)
	{
		return Currency.getInstance(currencyID);
	}

	public static Locale getLocaleByCurrencyCode(String currencyCode_A)
	{
		for (Locale locale : NumberFormat.getAvailableLocales())
		{
			String currencyCode_B = NumberFormat.getCurrencyInstance(locale).getCurrency().getCurrencyCode();

			if (currencyCode_A.equals(currencyCode_B))
			{
				return locale;
			}
		}

		return null;
	}

	public static String format(double valueAsDouble)
	{
		SupportedCurrency currency = AppSettings.BaseCurrency;

		String decimalFormatString = defineDecimalFormatString(currency);

		DecimalFormat decimalFormatter = new DecimalFormat(decimalFormatString);
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		
		decimalFormatSymbols.setDecimalSeparator(currency.getDecimalMarkAsSymbol());
		decimalFormatSymbols.setGroupingSeparator(currency.getThousandsSeparatorAsSymbol());
		
		decimalFormatter.setDecimalFormatSymbols(decimalFormatSymbols);
		decimalFormatter.setGroupingSize(3);
		decimalFormatter.setGroupingUsed(true);

		return defineCurrencySymbolPosition(decimalFormatter, valueAsDouble);
	}

	private static String defineDecimalFormatString(SupportedCurrency currency)
	{
		String decimalFormatString = "0";
		String numberOfDecimalPlaces = "";

		if (currency.getNumberOfDecimalPlaces() > 0)
		{
			decimalFormatString += ".";

			for (int i = 0; i < currency.getNumberOfDecimalPlaces(); ++i)
			{
				numberOfDecimalPlaces += "0";
			}

			decimalFormatString += numberOfDecimalPlaces;
		}

		return decimalFormatString;
	}
	
	private static String defineCurrencySymbolPosition(DecimalFormat decimalFormatter, double valueAsDouble)
	{
		String returnString = "";
		String valueAsString = decimalFormatter.format(valueAsDouble);

		switch (AppSettings.BaseCurrency.getCurrencySymbolPosition())
		{
			case CurrencySymbolPosition.prefix:
				returnString = AppSettings.BaseCurrency.getCurrencySymbol() + " " + valueAsString;
				break;
			case CurrencySymbolPosition.suffix:
				returnString = valueAsString + " " + AppSettings.BaseCurrency.getCurrencySymbol();
				break;
		}
		
		return returnString;
	}
}
