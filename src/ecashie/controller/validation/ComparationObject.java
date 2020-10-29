package ecashie.controller.validation;

import ecashie.view.inputfields.InputField;

public class ComparationObject
{
	private InputField inputField;

	public InputField getInputField()
	{
		return inputField;
	}

	public void setInputField(InputField inputField)
	{
		this.inputField = inputField;
	}

	private boolean shouldBeEquals;

	public boolean getShouldBeEquals()
	{
		return shouldBeEquals;
	}

	public void setShouldBeEquals(boolean shouldBeEquals)
	{
		this.shouldBeEquals = shouldBeEquals;
	}

	public ComparationObject(InputField inputField, boolean shouldBeEquals)
	{
		this.inputField = inputField;
		this.shouldBeEquals = shouldBeEquals;
	}
}
