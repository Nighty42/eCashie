package ecashie.view.choices;

public class DecimalMarkChoices
{
	public static final int none = -1;
	public static final int comma = 0;
	public static final int dot = 1;

	public static char getNone()
	{
		return Character.MIN_VALUE;
	}

	public static char getComma()
	{
		return ',';
	}

	public static char getDot()
	{
		return '.';
	}

	public static char getSymbolById(int decimalMarkId)
	{
		switch (decimalMarkId)
		{
			case comma:
				return getComma();
			case dot:
				return getDot();
		}

		return ' ';
	}
}
