package org.ecashie.view.choices;

public class ThousandsSeparatorChoices
{
	public static final int comma = 0;
	public static final int space = 1;
	public static final int dot = 2;

	public static char getComma()
	{
		return ',';
	}

	public static char getSpace()
	{
		return ' ';
	}

	public static char getDot()
	{
		return '.';
	}

	public static char getSymbolById(int thousandsSeparatorId)
	{
		switch (thousandsSeparatorId)
		{
			case comma:
				return getComma();
			case space:
				return getSpace();
			case dot:
				return getDot();
		}

		return ' ';
	}
}
