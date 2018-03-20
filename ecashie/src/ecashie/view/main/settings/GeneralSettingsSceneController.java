package ecashie.view.main.settings;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

import ecashie.controller.database.DatabaseAccess;
import ecashie.controller.errorhandling.UnexpectedBehaviourException;
import ecashie.controller.gui.Navigation;
import ecashie.controller.internationalization.CurrencyUtils;
import ecashie.controller.internationalization.LanguageUtils;
import ecashie.controller.internationalization.SupportedCurrency;
import ecashie.controller.internationalization.SupportedLanguage;
import ecashie.controller.validation.Validation;
import ecashie.model.settings.AppSettings;
import ecashie.model.settings.UserData;
import ecashie.view.choices.CurrencySymbolPositionChoices;
import ecashie.view.choices.DecimalMarkChoices;
import ecashie.view.choices.ThousandsSeparatorChoices;
import ecashie.view.inputfields.InputField;
import ecashie.view.inputfields.general.SpinnerController;
import ecashie.view.inputfields.general.TextFieldController;
import ecashie.view.inputfields.login.PasswordFieldController;
import ecashie.view.inputfields.login.PasswordLengthSpinnerController;
import ecashie.view.menu.MenuSceneController;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class GeneralSettingsSceneController
{
	@FXML
	private GridPane fileNameGridPane;
	@FXML
	private TextField fileNameTextField;
	@FXML
	private Label fileNameStatusLabel;

	@FXML
	private GridPane passwordGridPane;
	@FXML
	private PasswordField passwordPasswordField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private ImageView passwordVisibilityImageView;
	@FXML
	private Label passwordStatusLabel;
	@FXML
	private Spinner<Integer> passwordLengthSpinner;

	@FXML
	private Button saveButton;
	@FXML
	private Button resetButton;

	@FXML
	private ComboBox<SupportedLanguage> languageComboBox;
	@FXML
	private ComboBox<SupportedCurrency> currencyComboBox;

	@FXML
	private GridPane currencySymbolGridPane;
	@FXML
	private TextField currencySymbolTextField;
	@FXML
	private Label currencySymbolStatusLabel;

	@FXML
	private ToggleGroup currencySymbolPositionChoices;
	@FXML
	private RadioButton currencySymbolAsPrefixRadioButton;
	@FXML
	private RadioButton currencySymbolAsSuffixRadioButton;

	@FXML
	private ToggleGroup thousandsSeparatorChoices;
	@FXML
	private RadioButton thousandsSeparatorAsCommaRadioButton;
	@FXML
	private RadioButton thousandsSeparatorAsSpaceRadioButton;
	@FXML
	private RadioButton thousandsSeparatorAsDotRadioButton;

	@FXML
	private ToggleGroup decimalMarkChoices;
	@FXML
	private RadioButton decimalMarkAsCommaRadioButton;
	@FXML
	private RadioButton decimalMarkAsDotRadioButton;

	@FXML
	private Spinner<Integer> numberOfDecimalPlacesSpinner;

	@FXML
	private Label previewCurrencySettingsLabel;

	private TextFieldController fileNameFieldController;
	private PasswordFieldController passwordFieldController;
	private PasswordLengthSpinnerController passwordLengthFieldController;

	private TextFieldController currencySymbolFieldController;
	private SpinnerController numberOfDecimalPlacesFieldController;

	@FXML
	private void initialize()
	{
		initializeFileNameField();

		initializePasswordField();

		initializePasswordLengthField();

		initializeLanguageComboBox();

		initializeCurrencyComboBox();

		initializeCurrencySymbolField();

		initializeNumberOfDecimalPlacesField();

		initializeFieldValues();

		updatePreview();

		fileNameFieldController.getInputField().requestFocus();
	}

	private void initializeFileNameField()
	{
		InputField inputField = new InputField(fileNameTextField, true, Optional.of(1), Optional.of(50),
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

		fileNameFieldController = new TextFieldController(inputField, fileNameGridPane, fileNameTextField,
				fileNameStatusLabel);

		fileNameFieldController.getInputField().setText(UserData.getCashJournalName());

		fileNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (Validation.fireOnTextChangeInputField)
			{
				setButtonStatusLoginData();
			}
		});
	}

	private void initializePasswordField()
	{
		InputField inputField = new InputField(passwordPasswordField, false, Optional.of(8), Optional.of(200),
				Optional.of("PASSWORD"), Optional.empty(), Optional.empty(), Optional.empty());

		passwordFieldController = new PasswordFieldController(inputField, passwordGridPane, passwordPasswordField,
				passwordTextField, passwordVisibilityImageView, passwordStatusLabel);

		passwordFieldController.getInputField().setText(UserData.getPassword());

		passwordPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (Validation.fireOnTextChangeInputField)
			{
				setButtonStatusLoginData();
			}
		});

		passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (Validation.fireOnTextChangeInputField)
			{
				setButtonStatusLoginData();
			}
		});
		
		enableLiveValidation(true);
	}

	private void initializePasswordLengthField()
	{
		InputField inputField = new InputField(passwordLengthSpinner, false, Optional.empty(), Optional.empty(),
				Optional.of("ONLY_NUMBERS"), Optional.of(8), Optional.of(200), Optional.empty());

		passwordLengthFieldController = new PasswordLengthSpinnerController(inputField, passwordLengthSpinner);
	}

	private void setButtonStatusLoginData()
	{
		if (!fileNameFieldController.getInputField().getText().equals(UserData.getCashJournalName())
				|| !passwordFieldController.getInputField().getText().equals(UserData.getPassword()))
		{
			resetButton.setDisable(false);

			if (fileNameFieldController.getInputField().isValid() && passwordFieldController.getInputField().isValid())
			{
				saveButton.setDisable(false);
			}
			else
			{
				saveButton.setDisable(true);
			}
		}
		else
		{
			resetButton.setDisable(true);
			saveButton.setDisable(true);
		}
	}

	@FXML
	private void onActionResetButton()
	{
		fileNameFieldController.getInputField().setText(UserData.getCashJournalName());
		passwordFieldController.getInputField().setText(UserData.getPassword());

		fileNameFieldController.getInputField().end();
	}

	@FXML
	private void onActionSaveButton()
	{
		try
		{
			saveChanges();

			resetButton.setDisable(true);
			saveButton.setDisable(true);
			fileNameFieldController.getInputField().end();
		}
		catch (SQLException e)
		{
			new UnexpectedBehaviourException();
		}
	}

	private void enableLiveValidation(boolean enable)
	{
		fileNameFieldController.setEnableLiveValidation(enable);
		passwordFieldController.setEnableLiveValidation(enable);
	}

	private void saveChanges() throws SQLException
	{
		saveFileName();

		savePassword();
	}

	private void saveFileName()
	{
		String userDataFolder = UserData.getCashJournalFile().getAbsolutePath().substring(0,
				UserData.getCashJournalFile().getAbsolutePath().lastIndexOf("\\"));

		File userDataFile = new File(
				userDataFolder + "\\" + fileNameFieldController.getInputField().getText() + ".ecdb");

		UserData.setCashJournalFile(userDataFile);
	}

	private void savePassword() throws SQLException
	{
		UserData.setPassword(passwordFieldController.getInputField().getText());
		DatabaseAccess.setPassword();
	}

	@FXML
	private void onActionChangePasswordVisibility()
	{
		passwordFieldController.onActionChangePasswordVisibility();
	}

	@FXML
	private void onActionMinimumPasswordLength()
	{
		passwordLengthFieldController.onActionMinimumPasswordLength();
	}

	@FXML
	private void onActionMaximumPasswordLength()
	{
		passwordLengthFieldController.onActionMaximumPasswordLength();
	}

	@FXML
	private void onActionGeneratePassword()
	{
		passwordFieldController.getInputField().setText(passwordLengthFieldController.onActionGeneratePassword());
	}

	@FXML
	private void onActionCopyPassword() throws IOException
	{
		passwordLengthFieldController.onActionCopyPassword(passwordFieldController.getInputField().getText());
	}

	private void initializeLanguageComboBox()
	{
		languageComboBox.setItems(SupportedLanguage.getList());

		languageComboBox.getSelectionModel().select(SupportedLanguage.getList()
				.filtered(x -> x.getLocale().toString().equals(AppSettings.language.getLocale().toString())).get(0));

		languageComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onSelectionChangedLanguageComboBox(observable, oldValue, newValue);
		});
	}

	private void initializeCurrencyComboBox()
	{
		currencyComboBox.setItems(SupportedCurrency.getList());

		currencyComboBox.getSelectionModel().select(SupportedCurrency.getList().filtered(
				x -> x.getCurrency().getCurrencyCode().equals(AppSettings.baseCurrency.getCurrency().getCurrencyCode()))
				.get(0));

		currencyComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onSelectionChangedCurrencyComboBox(observable, oldValue, newValue);
		});
	}

	private void initializeCurrencySymbolField()
	{
		InputField inputField = new InputField(currencySymbolTextField, true, Optional.of(1), Optional.of(5),
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

		currencySymbolFieldController = new TextFieldController(inputField, currencySymbolGridPane,
				currencySymbolTextField, currencySymbolStatusLabel);

		currencySymbolFieldController.setEnableLiveValidation(true);

		currencySymbolTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (Validation.fireOnTextChangeInputField)
			{
				onTextChangeCurrencySymbolTextField(observable, oldValue, newValue);
			}
		});
	}

	private void initializeNumberOfDecimalPlacesField()
	{
		InputField inputField = new InputField(numberOfDecimalPlacesSpinner, false, Optional.empty(), Optional.empty(),
				Optional.of("ONLY_NUMBERS"), Optional.of(0), Optional.of(10), Optional.empty());

		numberOfDecimalPlacesFieldController = new SpinnerController(inputField, numberOfDecimalPlacesSpinner);

		numberOfDecimalPlacesSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			onValueChangedNumberOfDecimalPlacesSpinnerTextField();
		});

		numberOfDecimalPlacesSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
			onValueChangedNumberOfDecimalPlacesSpinnerTextField();
		});
	}

	private void initializeFieldValues()
	{
		currencySymbolFieldController.getInputField().setText(AppSettings.baseCurrency.getCurrencySymbol());

		switch (AppSettings.baseCurrency.getCurrencySymbolPosition())
		{
		case CurrencySymbolPositionChoices.prefix:
			currencySymbolPositionChoices.selectToggle(currencySymbolAsPrefixRadioButton);
			break;
		case CurrencySymbolPositionChoices.suffix:
			currencySymbolPositionChoices.selectToggle(currencySymbolAsSuffixRadioButton);
			break;
		}

		switch (AppSettings.baseCurrency.getThousandsSeparator())
		{
		case ThousandsSeparatorChoices.comma:
			thousandsSeparatorChoices.selectToggle(thousandsSeparatorAsCommaRadioButton);
			break;
		case ThousandsSeparatorChoices.space:
			thousandsSeparatorChoices.selectToggle(thousandsSeparatorAsSpaceRadioButton);
			break;
		case ThousandsSeparatorChoices.dot:
			thousandsSeparatorChoices.selectToggle(thousandsSeparatorAsDotRadioButton);
			break;
		}

		switch (AppSettings.baseCurrency.getDecimalMark())
		{
		case DecimalMarkChoices.comma:
			decimalMarkChoices.selectToggle(decimalMarkAsCommaRadioButton);
			break;
		case DecimalMarkChoices.dot:
			decimalMarkChoices.selectToggle(decimalMarkAsDotRadioButton);
			break;
		}

		numberOfDecimalPlacesFieldController.getInputField()
				.setText(AppSettings.baseCurrency.getNumberOfDecimalPlacesAsString());
	}

	private void onTextChangeCurrencySymbolTextField(ObservableValue<? extends String> observable, String oldValue,
			String newValue)
	{
		AppSettings.baseCurrency.setCurrenySymbol(newValue);

		updatePreview();
	}

	private void onSelectionChangedLanguageComboBox(ObservableValue<? extends SupportedLanguage> observable,
			SupportedLanguage oldValue, SupportedLanguage newValue)
	{
		LanguageUtils.changeLanguage(newValue);

		updateMainScene();
	}

	private void onSelectionChangedCurrencyComboBox(ObservableValue<? extends SupportedCurrency> observable,
			SupportedCurrency oldValue, SupportedCurrency newValue)
	{
		AppSettings.baseCurrency = newValue;

		Locale locale = CurrencyUtils.getLocaleByCurrencyCode(newValue.getCurrency().getCurrencyCode());
		Currency currency = Currency.getInstance(locale);
		String symbol = currency.getSymbol(locale);

		currencySymbolFieldController.getInputField().setText(symbol);
	}

	@FXML
	private void onActionCurrencySymbolAsPrefixRadioButton()
	{
		AppSettings.baseCurrency.setCurrencySymbolPosition(CurrencySymbolPositionChoices.prefix);

		updatePreview();
	}

	@FXML
	private void onActionCurrencySymbolAsSuffixRadioButton()
	{
		AppSettings.baseCurrency.setCurrencySymbolPosition(CurrencySymbolPositionChoices.suffix);

		updatePreview();
	}

	@FXML
	private void onActionThousandsSeparatorAsCommaRadioButton()
	{
		AppSettings.baseCurrency.setThousandsSeparator(ThousandsSeparatorChoices.comma);

		updatePreview();
	}

	@FXML
	private void onActionThousandsSeparatorAsSpaceRadioButton()
	{
		AppSettings.baseCurrency.setThousandsSeparator(ThousandsSeparatorChoices.space);

		updatePreview();
	}

	@FXML
	private void onActionThousandsSeparatorAsDotRadioButton()
	{
		AppSettings.baseCurrency.setThousandsSeparator(ThousandsSeparatorChoices.dot);

		updatePreview();
	}

	@FXML
	private void onActionDecimalMarkAsCommaRadioButton()
	{
		AppSettings.baseCurrency.setDecimalMark(DecimalMarkChoices.comma);

		updatePreview();
	}

	@FXML
	private void onActionDecimalMarkAsDotRadioButton()
	{
		AppSettings.baseCurrency.setDecimalMark(DecimalMarkChoices.dot);

		updatePreview();
	}

	@FXML
	private void onActionMinimumNumberOfDecimalPlaces()
	{
		numberOfDecimalPlacesFieldController.onActionMinimum();

		updatePreview();
	}

	@FXML
	private void onActionMaximumNumberOfDecimalPlaces()
	{
		numberOfDecimalPlacesFieldController.onActionMaximum();

		updatePreview();
	}

	private void onValueChangedNumberOfDecimalPlacesSpinnerTextField()
	{
		if (numberOfDecimalPlacesFieldController.getInputField().isValid())
		{
			AppSettings.baseCurrency
					.setNumberOfDecimalPlaces(Integer.parseInt(numberOfDecimalPlacesSpinner.getEditor().getText()));

			updatePreview();
		}
	}

	private void updatePreview()
	{
		double previewAsDouble = 123456.7890123456;
		String previewAsString = CurrencyUtils.format(previewAsDouble);

		previewCurrencySettingsLabel.setText(previewAsString);
	}

	private void updateMainScene()
	{
		MenuSceneController.showSettingsScene = true;

		Navigation.goForward();
	}
}
