package ecashie.view.start;

import java.io.File;
import java.util.Optional;

import ecashie.controller.database.DatabaseAccess;
import ecashie.controller.errorhandling.DatabasePasswordInvalidException;
import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.gui.Navigation;
import ecashie.model.settings.AppSettings;
import ecashie.model.settings.UserData;
import ecashie.view.inputfields.InputField;
import ecashie.view.inputfields.login.FileFolderPathFieldController;
import ecashie.view.inputfields.login.PasswordFieldController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

public class OpenCashJournalSceneController
{
	@FXML
	private TextField filePathTextField;
	@FXML
	private GridPane filePathGridPane;
	@FXML
	private Label filePathStatusLabel;

	@FXML
	private CheckBox saveHistoryCheckBox;

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

	private FileFolderPathFieldController filePathFieldController;
	private PasswordFieldController passwordFieldController;

	private static OpenCashJournalSceneController instance = null;

	public static OpenCashJournalSceneController getInstance()
	{
		return instance;
	}

	@FXML
	private void initialize()
	{
		instance = this;

		initializeFolderPathChange();

		initializePasswordChange();

		initializeRecentUsedDatabase();

		requestFocus();
	}

	private void initializeFolderPathChange()
	{
		InputField inputField = new InputField(filePathTextField, true, Optional.of(3), Optional.of(200),
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

		inputField.setIsFile(".ecdb");

		filePathFieldController = new FileFolderPathFieldController(inputField, filePathGridPane, filePathTextField,
				filePathStatusLabel);
	}

	private void initializePasswordChange()
	{
		InputField inputField = new InputField(passwordPasswordField, false, Optional.of(8), Optional.of(200),
				Optional.of("PASSWORD"), Optional.empty(), Optional.empty(), Optional.empty());

		passwordFieldController = new PasswordFieldController(inputField, passwordGridPane, passwordPasswordField,
				passwordTextField, passwordVisibilityImageView, passwordStatusLabel);
	}

	private void initializeRecentUsedDatabase()
	{
		filePathFieldController.getInputField().setText(AppSettings.recentUsedDatabase);
	}

	// ================================================================================
	// onDrag-Events
	// ================================================================================

	@FXML
	private void onDragExitedFilePathTextField(DragEvent dragEvent)
	{
		filePathFieldController.onDragExitedTextField(dragEvent);
	}

	@FXML
	private void onDragEnteredFilePathTextField(DragEvent dragEvent)
	{
		filePathFieldController.onDragEnteredTextField(dragEvent);
	}

	@FXML
	private void onDragOverFilePathTextField(DragEvent dragEvent)
	{
		filePathFieldController.onDragOverTextField(dragEvent);
	}

	@FXML
	private void onDragDroppedFilePathTextField(DragEvent dragEvent)
	{
		filePathFieldController.onDragDroppedTextField(dragEvent);
	}

	// ================================================================================
	// onAction-Events
	// ================================================================================

	@FXML
	private void onActionExistentDatabaseButton()
	{
		validateFilePath();

		openFileChooser();
	}

	private void openFileChooser()
	{
		String pathInitialDirectory = System.getProperty("user.home");

		if (filePathFieldController.getInputField().isValid())
		{
			String filePath = filePathTextField.getText();
			int lastIndexOfSlash = filePath.lastIndexOf("\\");

			pathInitialDirectory = filePath.substring(0, lastIndexOfSlash);
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(pathInitialDirectory));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ECDB Files (*.ecdb)", "*.ecdb");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showOpenDialog(GuiBuilder.primaryStage);

		if (file != null)
		{
			filePathFieldController.getInputField().setText(file.getAbsolutePath());
		}
		else
		{
			filePathFieldController.getInputField().requestFocus();
		}
	}

	@FXML
	private void onActionChangePasswordVisibility()
	{
		passwordFieldController.onActionChangePasswordVisibility();
	}

	public void onActionLoginButton()
	{
		enableLiveValidation(true);

		validateFilePath();
		validatePassword();

		if (filePathFieldController.getInputField().isValid() && passwordFieldController.getInputField().isValid())
		{
			File userDataFile = new File(filePathFieldController.getInputField().getText());

			doLogin(userDataFile);
		}
		else
		{
			requestFocus();
		}
	}

	private void enableLiveValidation(boolean enable)
	{
		filePathFieldController.setEnableLiveValidation(enable);
		passwordFieldController.setEnableLiveValidation(enable);
	}

	private void doLogin(File userDataFile)
	{
		try
		{
			openDatabase();

			saveHistory(userDataFile);

			openMainScene();
		}
		catch (DatabasePasswordInvalidException e)
		{
			passwordFieldController.getInputField().setError("invalidPassword");
			passwordFieldController.getInputField().visualizeStatus(passwordGridPane, passwordStatusLabel);

			passwordFieldController.getInputField().requestFocus();
		}
	}

	private void openDatabase() throws DatabasePasswordInvalidException
	{
		UserData.setCashJournalFile(new File(filePathFieldController.getInputField().getText()));
		UserData.setPassword(passwordFieldController.getInputField().getText());

		DatabaseAccess.openDatabase(false);
	}

	private void saveHistory(File userDataFile)
	{
		if (saveHistoryCheckBox.isSelected())
		{
			StartSceneController.saveHistory(userDataFile);
		}
	}

	private void openMainScene()
	{
		Navigation.Next = "MainScene";
		Navigation.goForward();
	}

	// ================================================================================
	// Validation
	// ================================================================================

	private void validateFilePath()
	{
		filePathFieldController.validate();
	}

	private void validatePassword()
	{
		passwordFieldController.validate();
	}

	public void requestFocus()
	{
		if (!filePathFieldController.getInputField().isValid())
		{
			filePathFieldController.getInputField().requestFocus();
		}
		else if (!passwordFieldController.getInputField().isValid())
		{
			passwordFieldController.getInputField().requestFocus();
		}
		else
		{
			filePathFieldController.getInputField().requestFocus();
		}
	}
}
