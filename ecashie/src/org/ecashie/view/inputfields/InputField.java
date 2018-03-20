package org.ecashie.view.inputfields;

import java.util.Optional;

import org.ecashie.controller.gui.GuiBuilder;
import org.ecashie.controller.internationalization.ResourceBundleString;
import org.ecashie.controller.validation.ComparationObject;
import org.ecashie.controller.validation.Solutions;
import org.ecashie.controller.validation.Validation;
import org.ecashie.view.root.RootLayout;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class InputField
{
	private static final int isNeitherFileNorFolder = 0;
	private static final int isFile = 1;
	private static final int isFolder = 2;

	private ObjectProperty<Control> control = new SimpleObjectProperty<>();

	public ObjectProperty<Control> controlProperty()
	{
		return control;
	}

	public Control getControl()
	{
		return control.getValue();
	}

	public void setControl(Control control)
	{
		this.control.setValue(control);
	}

	private Optional<Integer> minLength;

	public Optional<Integer> getMinLength()
	{
		return minLength;
	}

	public void setMinLength(int minLength)
	{
		this.minLength = Optional.of(minLength);
	}

	private Optional<Integer> maxLength;

	public Optional<Integer> getMaxLength()
	{
		return maxLength;
	}

	public void setMaxLength(int maxLength)
	{
		this.maxLength = Optional.of(maxLength);
	}

	private boolean whitespacesAllowed;

	public boolean getWhitespacesAllowed()
	{
		return whitespacesAllowed;
	}

	public void setWhitespacesAllowed(boolean whitespacesAllowed)
	{
		this.whitespacesAllowed = whitespacesAllowed;
	}

	private Optional<String> regexSequenceID = Optional.empty();

	public String getRegexSequenceID()
	{
		if (regexSequenceID.isPresent())
		{
			return regexSequenceID.get();
		}
		else
		{
			return "";
		}
	}

	public void setRegexSequenceID(String regexSequenceID)
	{
		this.regexSequenceID = Optional.of(regexSequenceID);
	}

	private Optional<Integer> minValue = Optional.empty();

	public Optional<Integer> getMinValue()
	{
		return minValue;
	}

	public void setMinValue(int minValue)
	{
		this.minValue = Optional.of(minValue);
	}

	private Optional<Integer> maxValue = Optional.empty();

	public Optional<Integer> getMaxValue()
	{
		return maxValue;
	}

	public void setMaxValue(int maxValue)
	{
		this.maxValue = Optional.of(maxValue);
	}

	private Optional<ComparationObject> comparationObject = Optional.empty();

	public ComparationObject getComparationObject()
	{
		if (comparationObject != null && comparationObject.isPresent())
		{
			return comparationObject.get();
		}
		else
		{
			return null;
		}
	}

	public void setCompareObject(ComparationObject comparationObject)
	{
		this.comparationObject = Optional.of(comparationObject);
	}

	private Optional<String> fileExtension = Optional.empty();

	public String getFileExtension()
	{
		if (fileExtension.isPresent())
		{
			return fileExtension.get();
		}
		else
		{
			return "";
		}
	}

	public void setFileExtension(Optional<String> fileExtension)
	{
		this.fileExtension = fileExtension;
		this.isPath = isFile;
	}

	private int isPath = isNeitherFileNorFolder;

	public boolean isNeitherFileNorFolder()
	{
		if (isPath == isNeitherFileNorFolder)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void setIsNeitherFileNorFolder()
	{
		this.isPath = isNeitherFileNorFolder;
	}

	public boolean isFile()
	{
		if (isPath == isFile)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void setIsFile(String fileExtension)
	{
		this.isPath = isFile;
		this.fileExtension = Optional.of(fileExtension);
	}

	public boolean isFolder()
	{
		if (isPath == isFolder)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void setIsFolder()
	{
		this.isPath = isFolder;
	}

	private String error = "";

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}

	public InputField(Control control, boolean whitespacesAllowed, Optional<Integer> minLength,
			Optional<Integer> maxLength, Optional<String> regexSequenceID, Optional<Integer> minValue,
			Optional<Integer> maxValue, Optional<ComparationObject> comparationObject)
	{
		this.control.setValue(control);
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.whitespacesAllowed = whitespacesAllowed;
		this.regexSequenceID = regexSequenceID;

		this.minValue = minValue;
		this.maxValue = maxValue;
		this.comparationObject = comparationObject;
	}

	public void validateWithVisualization(boolean enableLiveValidation, GridPane gridPane, Label statusLabel)
	{
		if (enableLiveValidation)
		{
			Validation.validateInputField(this);

			Solutions.runPossibleSolutions(this);

			visualizeStatus(gridPane, statusLabel);
		}
	}

	public void validateWithoutVisualization()
	{
		Validation.validateInputField(this);

		Solutions.runPossibleSolutions(this);
	}

	private TextField getControlAsTextField()
	{
		return (TextField) control.getValue();
	}

	private PasswordField getControlAsPasswordField()
	{
		return (PasswordField) control.getValue();
	}

	private TextField getControlAsTextFieldOfSpinner()
	{
		Spinner<?> spinner = (Spinner<?>) control.getValue();
		return spinner.getEditor();
	}

	private String getSimpleNameControlClass()
	{
		return control.getValue().getClass().getSimpleName();
	}

	public String getText()
	{
		String text = "";

		switch (getSimpleNameControlClass())
		{
		case "TextField":
			TextField textField = getControlAsTextField();
			text = textField.getText();
			break;
		case "PasswordField":
			PasswordField passwordField = getControlAsPasswordField();
			text = passwordField.getText();
			break;
		case "Spinner":
			TextField textFieldOfSpinner = getControlAsTextFieldOfSpinner();
			text = textFieldOfSpinner.getText();
			break;
		}

		return text;
	}

	public void setText(String text)
	{
		switch (getSimpleNameControlClass())
		{
		case "TextField":
			TextField textField = getControlAsTextField();
			textField.setText(text);
			break;
		case "PasswordField":
			PasswordField passwordField = getControlAsPasswordField();
			passwordField.setText(text);
			break;
		case "Spinner":
			TextField textFieldOfSpinner = getControlAsTextFieldOfSpinner();
			textFieldOfSpinner.setText(text);
			break;
		}
	}

	public void requestFocus()
	{
		Platform.runLater(() -> {
			switch (getSimpleNameControlClass())
			{
			case "TextField":
				TextField textField = getControlAsTextField();
				textField.requestFocus();
				break;
			case "PasswordField":
				PasswordField passwordField = getControlAsPasswordField();
				passwordField.requestFocus();
				break;
			case "Spinner":
				TextField textFieldOfSpinner = getControlAsTextFieldOfSpinner();
				textFieldOfSpinner.requestFocus();
				break;
			}
		});
	}

	public void end()
	{
		Platform.runLater(() -> {
			switch (getSimpleNameControlClass())
			{
			case "TextField":
				TextField textField = getControlAsTextField();
				textField.requestFocus();
				textField.end();
				break;
			case "PasswordField":
				PasswordField passwordField = getControlAsPasswordField();
				passwordField.requestFocus();
				passwordField.end();
				break;
			case "Spinner":
				TextField textFieldOfSpinner = getControlAsTextFieldOfSpinner();
				textFieldOfSpinner.requestFocus();
				textFieldOfSpinner.end();
				break;
			}
		});
	}

	public void selectAll()
	{
		Platform.runLater(() -> {
			switch (getSimpleNameControlClass())
			{
			case "TextField":
				TextField textField = getControlAsTextField();
				textField.requestFocus();
				textField.selectAll();
				break;
			case "PasswordField":
				PasswordField passwordField = getControlAsPasswordField();
				passwordField.requestFocus();
				passwordField.selectAll();
				break;
			case "Spinner":
				TextField textFieldOfSpinner = getControlAsTextFieldOfSpinner();
				textFieldOfSpinner.requestFocus();
				textFieldOfSpinner.selectAll();
				break;
			}
		});
	}

	public String[] getPossibleMessageArguments()
	{
		String[] possibleArguments = new String[] { Integer.toString(getMinLength().get()),
				Integer.toString(getMaxLength().get()) };

		return possibleArguments;
	}

	public boolean isValid()
	{
		if (error.isEmpty() && !getText().isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void visualizeStatus(GridPane gridPane, Label statusLabel)
	{
		if (isValid())
		{
			gridPane.setId("successGridPane");

			statusLabel.setText("");
		}
		else
		{
			gridPane.setId("failureGridPane");

			String[] arguments = getPossibleMessageArguments();
			String messageKey = "validation." + getError();

			String errorText = ResourceBundleString.getLocaleString(messageKey, arguments);

			statusLabel.setText(errorText);
		}

		if (GuiBuilder.currentRootScene == RootLayout.nonFullScreen)
		{
			GuiBuilder.primaryStage.sizeToScene();
		}
	}
}