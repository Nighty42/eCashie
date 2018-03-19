package ecashie.view.inputfields.general;

import ecashie.controller.validation.Validation;
import ecashie.view.inputfields.InputField;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class TextFieldController
{
	private TextField textField;

	public TextField getTextField()
	{
		return textField;
	}

	public void setTextField(TextField textField)
	{
		this.textField = textField;
	}

	private GridPane gridPane;

	public GridPane getGridPane()
	{
		return gridPane;
	}

	public void setGridPane(GridPane gridPane)
	{
		this.gridPane = gridPane;
	}

	private Label statusLabel;

	public Label getStatusLabel()
	{
		return statusLabel;
	}

	public void setStatusLabel(Label statusLabel)
	{
		this.statusLabel = statusLabel;
	}

	private InputField inputField;

	public InputField getInputField()
	{
		return inputField;
	}

	public void setInputField(InputField inputField)
	{
		this.inputField = inputField;
	}

	private boolean enableLiveValidation = false;

	public boolean isEnableLiveValidation()
	{
		return enableLiveValidation;
	}

	public void setEnableLiveValidation(boolean enableLiveValidation)
	{
		this.enableLiveValidation = enableLiveValidation;
	}

	public TextFieldController(InputField inputField, GridPane gridPane, TextField textField, Label statusLabel)
	{
		this.inputField = inputField;
		this.gridPane = gridPane;
		this.textField = textField;
		this.statusLabel = statusLabel;

		initializeControlEvents();
	}

	private void initializeControlEvents()
	{
		textField.textProperty().addListener((observable, oldValue, newValue) ->
		{
			if (Validation.fireOnTextChangeInputField)
			{
				onTextChangeTextField(observable, oldValue, newValue);
			}
		});
	}

	private void onTextChangeTextField(ObservableValue<? extends String> observable, String oldValue, String newValue)
	{
		validate();
	}

	public void validate()
	{
		inputField.validateWithVisualization(enableLiveValidation, gridPane, statusLabel);
	}
}
