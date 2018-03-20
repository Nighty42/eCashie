package ecashie.controller.internationalization;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import ecashie.model.settings.AppSettings;
import ecashie.view.choices.CurrencySymbolPositionChoices;

public class CurrencyUtils
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
		SupportedCurrency currency = AppSettings.baseCurrency;

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

		switch (AppSettings.baseCurrency.getCurrencySymbolPosition())
		{
			case CurrencySymbolPositionChoices.prefix:
				returnString = AppSettings.baseCurrency.getCurrencySymbol() + " " + valueAsString;
				break;
			case CurrencySymbolPositionChoices.suffix:
				returnString = valueAsString + " " + AppSettings.baseCurrency.getCurrencySymbol();
				break;
		}
		
		return returnString;
	}
}
