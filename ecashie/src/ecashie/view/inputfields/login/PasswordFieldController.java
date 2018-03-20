package ecashie.view.inputfields.login;

import ecashie.controller.validation.Validation;
import ecashie.view.inputfields.InputField;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class PasswordFieldController
{
	private GridPane gridPane;

	public GridPane getGridPane()
	{
		return gridPane;
	}

	public void setGridPane(GridPane gridPane)
	{
		this.gridPane = gridPane;
	}

	private PasswordField passwordField;

	public PasswordField getPasswordField()
	{
		return passwordField;
	}

	public void setPasswordField(PasswordField passwordField)
	{
		this.passwordField = passwordField;
	}

	private TextField textField;

	public TextField getTextField()
	{
		return textField;
	}

	public void setTextField(TextField textField)
	{
		this.textField = textField;
	}

	private ImageView visibilityImageView;

	public ImageView getVisibilityImageView()
	{
		return visibilityImageView;
	}

	public void setVisibilityImageView(ImageView visibilityImageView)
	{
		this.visibilityImageView = visibilityImageView;
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

	public PasswordFieldController(InputField inputField, GridPane gridPane, PasswordField passwordField,
			TextField textField, ImageView visibilityImageView, Label statusLabel)
	{
		this.inputField = inputField;
		this.gridPane = gridPane;
		this.passwordField = passwordField;
		this.textField = textField;
		this.visibilityImageView = visibilityImageView;
		this.statusLabel = statusLabel;

		initializeBinding();

		initializeControlEvents();
	}

	public void initializeBinding()
	{
		bindPasswordFieldsVisibility();

		bindChangePasswordVisibilityButtonStyleId();

		bindPasswordInputFieldControlProperty();
	}

	public void bindPasswordFieldsVisibility()
	{
		textField.visibleProperty().bind(passwordField.visibleProperty().not());
	}

	public void bindChangePasswordVisibilityButtonStyleId()
	{
		ObservableStringValue styleIdProperty = Bindings.when(passwordField.visibleProperty()).then("passwordIsHidden")
				.otherwise("passwordIsVisible");

		visibilityImageView.idProperty().bind(styleIdProperty);
	}

	public void bindPasswordInputFieldControlProperty()
	{
		ObservableObjectValue<Control> controlProperty = Bindings.when(passwordField.visibleProperty())
				.then((Control) passwordField).otherwise(textField);

		inputField.controlProperty().bind(controlProperty);
	}

	public void initializeControlEvents()
	{
		passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (Validation.fireOnTextChangeInputField)
			{
				onTextChangePasswordField(observable, oldValue, newValue);
			}
		});

		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (Validation.fireOnTextChangeInputField)
			{
				onTextChangePasswordField(observable, oldValue, newValue);
			}
		});
	}

	public void onTextChangePasswordField(ObservableValue<? extends String> observable, String oldValue,
			String newValue)
	{
		validate();
	}

	public void onActionChangePasswordVisibility()
	{
		if (passwordField.isVisible())
		{
			passwordField.setVisible(false);
			textField.setText(passwordField.getText());

			inputField.end();
		}
		else
		{
			passwordField.setVisible(true);
			passwordField.setText(textField.getText());

			inputField.end();
		}
	}

	public void validate()
	{
		inputField.validateWithVisualization(enableLiveValidation, gridPane, statusLabel);
	}
}
