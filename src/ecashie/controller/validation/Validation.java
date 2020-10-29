package ecashie.controller.validation;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ecashie.controller.errorhandling.UnexpectedBehaviourException;
import ecashie.view.inputfields.InputField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Validation

{
	public static boolean fireOnTextChangeInputField = true;

	private static InputField inputField = null;

	public static void validateInputField(InputField _inputField)
	{
		inputField = _inputField;

		inputField.setError("");

		ObservableList<String> validationMethods = FXCollections.observableArrayList("validateWhiteSpaces",
				"validateMinLength", "validateMaxLength", "validateRegexSequence", "validateMinValue",
				"validateMaxValue", "validateComparationObject", "validateFileFolderPath", "validateFileExtension");

		for (String validationMethod : validationMethods)
		{
			invokeValidationMethod(validationMethod);

			if (!inputField.getError().isEmpty())
			{
				break;
			}
		}
	}

	private static void invokeValidationMethod(String validationMethod)
	{
		try
		{
			Method method = Validation.class.getDeclaredMethod(validationMethod);
			method.setAccessible(true);
			method.invoke(null);
		}
		catch (NoSuchMethodException | IllegalArgumentException | SecurityException | IllegalAccessException
				| InvocationTargetException e)
		{
			new UnexpectedBehaviourException();
		}
	}

	@SuppressWarnings("unused")
	private static void validateWhiteSpaces()
	{
		if (!inputField.getWhitespacesAllowed() && hasWhiteSpaces())
		{
			inputField.setError("containsWhiteSpaces");
		}
	}

	@SuppressWarnings("unused")
	private static void validateMinLength()
	{
		if (inputField.getMinLength().isPresent() && hasTextLengthUnderMinimumLength())
		{
			inputField.setError("isUnderMinimumLength");
		}
	}

	@SuppressWarnings("unused")
	private static void validateMaxLength()
	{
		if (inputField.getMaxLength().isPresent() && hasTextLengthOverMaximumLength())
		{
			inputField.setError("isOverMaximumLength");
		}
	}

	@SuppressWarnings("unused")
	private static void validateRegexSequence()
	{
		if (hasInvalidValueSequence())
		{
			inputField.setError("invalidSequence." + inputField.getRegexSequenceID());
		}
	}

	@SuppressWarnings("unused")
	private static void validateMinValue()
	{
		if (inputField.getMinValue().isPresent() && hasTextValueUnderMinimumValue())
		{
			inputField.setError("isUnderMinimumValue");
		}
	}

	@SuppressWarnings("unused")
	private static void validateMaxValue()
	{
		if (inputField.getMaxValue().isPresent() && hasTextValueOverMaximumValue())
		{
			inputField.setError("isOverMaximumValue");
		}
	}

	@SuppressWarnings("unused")
	private static void validateComparationObject()
	{
		ComparationObject comparationObject = inputField.getComparationObject();

		if (comparationObject != null)
		{
			InputField inputFieldComparationObject = comparationObject.getInputField();

			boolean areEquals = hasIdenticalComparationObject(inputFieldComparationObject);

			if (areEquals && !comparationObject.getShouldBeEquals())
			{
				inputField.setError("isEqualsThoughShouldNotBeEquals");
			}
			else if (!areEquals && comparationObject.getShouldBeEquals())
			{
				inputField.setError("isNotEqualsThoughShouldBeEquals");
			}
		}
	}

	@SuppressWarnings("unused")
	private static void validateFileFolderPath()
	{
		if (!inputField.isNeitherFileNorFolder() && hasFileOrFolderIsNonExistent())
		{
			if (inputField.isFile())
			{
				inputField.setError("isNoFile");
			}
			else
			{
				inputField.setError("isNoFolder");
			}
		}
	}

	@SuppressWarnings("unused")
	private static void validateFileExtension()
	{
		if (inputField.isFile() && hasInvalidFileExtension())
		{
			inputField.setError("isInvalidFileExtension");
		}
	}

	private static boolean hasInvalidValueSequence()
	{
		if (inputField.getRegexSequenceID().isEmpty())
		{
			return false;
		}
		else if (inputField.getText().matches(RegExSequences.getByID(inputField.getRegexSequenceID())))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private static boolean hasTextLengthUnderMinimumLength()
	{
		if (inputField.getText().length() < inputField.getMinLength().get())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean hasTextLengthOverMaximumLength()
	{
		if (inputField.getText().length() > inputField.getMaxLength().get())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean hasWhiteSpaces()
	{
		Pattern pattern = Pattern.compile("\\s");
		Matcher matcher = pattern.matcher(inputField.getText());

		if (matcher.find())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean hasTextValueUnderMinimumValue()
	{
		int value = Integer.parseInt(inputField.getText());

		if (value < inputField.getMinValue().get())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean hasTextValueOverMaximumValue()
	{
		int value = Integer.parseInt(inputField.getText());

		if (value > inputField.getMaxValue().get())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean hasIdenticalComparationObject(InputField inputFieldComparationObject)
	{
		if (inputField.equals(inputFieldComparationObject))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean hasFileOrFolderIsNonExistent()
	{
		File file = new File(inputField.getText());

		if (!file.exists())
		{
			return true;
		}
		else if (inputField.isFile() && !file.isFile())
		{
			return true;
		}
		else if (fileShouldBeFileButIsNot(file))
		{
			return true;
		}
		else if (fileShouldBeFolderButIsNot(file))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean fileShouldBeFileButIsNot(File file)
	{
		if (inputField.isFile() && !file.isFile())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean fileShouldBeFolderButIsNot(File folder)
	{
		if (!inputField.isFile() && !folder.isDirectory())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean hasInvalidFileExtension()
	{
		if (!inputField.getText().isEmpty())
		{
			File file = new File(inputField.getText());

			int filePathLength = file.getAbsolutePath().length();
			int startIndex = file.getAbsolutePath().lastIndexOf(".");
			String fileExtension = file.getAbsolutePath().substring(startIndex, filePathLength);

			if (!fileExtension.equals(inputField.getFileExtension()))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
}
