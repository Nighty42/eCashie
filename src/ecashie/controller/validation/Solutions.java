package ecashie.controller.validation;

import ecashie.view.inputfields.InputField;
import javafx.application.Platform;

public class Solutions
{
	public static void runPossibleSolutions(InputField inputField)
	{
		switch (inputField.getError())
		{
			case "isOverMaximumLength":
				Solutions.handleIsOverMaximumLength(inputField);
				break;
			case "containsWhiteSpaces":
				Solutions.handleContainsWhiteSpaces(inputField);
				break;
			case "isOverMaximumValue":
				Solutions.handleIsOverMaxValue(inputField);
				break;
			case "invalidSequence.ONLY_NUMBERS":
				Solutions.handleInvalidSequence(inputField);
				break;
		}
	}
	
	public static void handleIsOverMaximumLength(InputField inputField)
	{
		String newValue = inputField.getText().substring(0, inputField.getMaxLength().get());
		
		Validation.fireOnTextChangeInputField = false;

		Platform.runLater(() ->
		{
			inputField.setText(newValue);
			inputField.end();
			Validation.fireOnTextChangeInputField = true;
		});
	}

	public static void handleContainsWhiteSpaces(InputField inputField)
	{
		Validation.fireOnTextChangeInputField = false;

		Platform.runLater(() ->
		{
			inputField.setText(inputField.getText().replaceAll("\\s+", ""));
			inputField.end();
			Validation.fireOnTextChangeInputField = true;
		});
	}

	public static void handleIsOverMaxValue(InputField inputField)
	{
		Validation.fireOnTextChangeInputField = false;

		Platform.runLater(() ->
		{
			inputField.setText(inputField.getMaxValue().get().toString());
			inputField.end();
			Validation.fireOnTextChangeInputField = true;
		});
	}

	public static void handleInvalidSequence(InputField inputField)
	{
		if (!inputField.getText().matches(RegExSequences.getByID("ONLY_NUMBERS")) && inputField.getMinValue().isPresent())
		{
			Validation.fireOnTextChangeInputField = false;

			Platform.runLater(() ->
			{
				inputField.setText(inputField.getMinValue().get().toString());
				inputField.end();
				Validation.fireOnTextChangeInputField = true;
			});
		}
	}
}
