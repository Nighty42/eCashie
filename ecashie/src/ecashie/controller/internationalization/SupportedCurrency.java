package ecashie.controller.internationalization;

import java.util.Currency;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import ecashie.view.choices.CurrencySymbolPositionChoices;
import ecashie.view.choices.DecimalMarkChoices;
import ecashie.view.choices.ThousandsSeparatorChoices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SupportedCurrency
{
	private Currency currency = null;

	public Currency getCurrency()
	{
		return currency;
	}

	public void setCurrency(Currency currency)
	{
		this.currency = currency;
	}

	private String currencySymbol;

	public String getCurrencySymbol()
	{
		return currencySymbol;
	}

	public void setCurrenySymbol(String currenySymbol)
	{
		this.currencySymbol = currenySymbol;
	}

	private int currencySymbolPosition = CurrencySymbolPositionChoices.suffix;

	public int getCurrencySymbolPosition()
	{
		return currencySymbolPosition;
	}

	public String getCurrencySymbolPositionAsString()
	{
		return String.valueOf(currencySymbolPosition);
	}
	
	public void setCurrencySymbolPosition(int currencySymbolPosition)
	{
		this.currencySymbolPosition = currencySymbolPosition;
	}
	
	private int thousandsSeparator = ThousandsSeparatorChoices.dot;

	public int getThousandsSeparator()
	{
		return thousandsSeparator;
	}

	public String getThousandsSeparatorAsString()
	{
		return String.valueOf(thousandsSeparator);
	}
	
	public char getThousandsSeparatorAsSymbol()
	{
		return ThousandsSeparatorChoices.getSymbolById(thousandsSeparator);
	}
	
	public void setThousandsSeparator(int thousandsSeparator)
	{
		this.thousandsSeparator = thousandsSeparator;
	}
	
	private int decimalMark = DecimalMarkChoices.comma;

	public int getDecimalMark()
	{
		return decimalMark;
	}

	public String getDecimalMarkAsString()
	{
		return String.valueOf(decimalMark);
	}
	
	public char getDecimalMarkAsSymbol()
	{
		return DecimalMarkChoices.getSymbolById(decimalMark);
	}
	
	public void setDecimalMark(int decimalMark)
	{
		this.decimalMark = decimalMark;
	}

	private int numberOfDecimalPlaces = 2;

	public int getNumberOfDecimalPlaces()
	{
		return numberOfDecimalPlaces;
	}

	public String getNumberOfDecimalPlacesAsString()
	{
		return String.valueOf(numberOfDecimalPlaces);
	}

	public void setNumberOfDecimalPlaces(int numberOfDecimalPlaces)
	{
		this.numberOfDecimalPlaces = numberOfDecimalPlaces;
	}

	public SupportedCurrency(Currency currency)
	{
		this.currency = currency;
		this.currencySymbol = currency.getSymbol();
		this.currencySymbolPosition = CurrencySymbolPositionChoices.suffix;
		this.thousandsSeparator = ThousandsSeparatorChoices.dot;
		this.decimalMark = DecimalMarkChoices.comma;
		this.numberOfDecimalPlaces = 2;
	}

	public SupportedCurrency(Currency currency, String currencySymbol, int currencySymbolPosition,
			int thousandsSeparator, int decimalMark, int numberOfDecimalPlaces)
	{
		this.currency = currency;
		this.currencySymbol = currencySymbol;
		this.currencySymbolPosition = currencySymbolPosition;
		this.thousandsSeparator = thousandsSeparator;
		this.decimalMark = decimalMark;
		this.numberOfDecimalPlaces = numberOfDecimalPlaces;
	}

	public static ObservableList<SupportedCurrency> getList()
	{
		Set<Currency> currencyList = new HashSet<Currency>();
		ObservableList<SupportedCurrency> supportedCurrencyList = FXCollections.observableArrayList();

		for (Locale locale : Locale.getAvailableLocales())
		{
			try
			{
				currencyList.add(Currency.getInstance(locale));
			}
			catch (Exception e)
			{
				// when the locale is not supported
			}
		}

		Iterator<Currency> iterator = currencyList.iterator();

		while (iterator.hasNext())
		{
			Currency currency = iterator.next();

			supportedCurrencyList.add(new SupportedCurrency(currency));
		}

		return supportedCurrencyList.sorted();
	}

	@Override
	public String toString()
	{
		return currency.getDisplayName();
	}
}
