package ecashie.view.start;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import ecashie.controller.database.DatabaseAccess;
import ecashie.controller.errorhandling.DatabasePasswordInvalidException;
import ecashie.controller.errorhandling.UnexpectedBehaviourException;
import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.gui.Navigation;
import ecashie.model.settings.UserData;
import ecashie.view.inputfields.InputField;
import ecashie.view.inputfields.general.TextFieldController;
import ecashie.view.inputfields.login.FileFolderPathFieldController;
import ecashie.view.inputfields.login.PasswordFieldController;
import ecashie.view.inputfields.login.PasswordLengthSpinnerController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;

public class CreateCashJournalSceneController
{
	@FXML
	private TextField fileNameTextField;
	@FXML
	private GridPane fileNameGridPane;
	@FXML
	private Label fileNameStatusLabel;

	@FXML
	private TextField folderPathTextField;
	@FXML
	private GridPane folderPathGridPane;
	@FXML
	private Label folderPathStatusLabel;

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
	@FXML
	private Spinner<Integer> passwordLengthSpinner;

	private TextFieldController fileNameFieldController;
	private FileFolderPathFieldController folderPathFieldController;
	private PasswordFieldController passwordFieldController;
	private PasswordLengthSpinnerController passwordLengthFieldController;

	private static CreateCashJournalSceneController instance = null;

	public static CreateCashJournalSceneController getInstance()
	{
		return instance;
	}

	@FXML
	private void initialize()
	{
		instance = this;

		initializeFileNameChange();

		initializeFolderPathChange();

		initializePasswordField();

		initializePasswordLengthField();

		requestFocus();
	}

	private void initializeFileNameChange()
	{
		InputField inputField = new InputField(fileNameTextField, true, Optional.of(1), Optional.of(50),
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

		fileNameFieldController = new TextFieldController(inputField, fileNameGridPane, fileNameTextField,
				fileNameStatusLabel);
	}

	private void initializeFolderPathChange()
	{
		InputField inputField = new InputField(folderPathTextField, true, Optional.of(3), Optional.of(200),
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

		folderPathFieldController = new FileFolderPathFieldController(inputField, folderPathGridPane,
				folderPathTextField, folderPathStatusLabel);
	}

	private void initializePasswordField()
	{
		InputField inputField = new InputField(passwordPasswordField, false, Optional.of(8), Optional.of(200),
				Optional.of("PASSWORD"), Optional.empty(), Optional.empty(), Optional.empty());

		passwordFieldController = new PasswordFieldController(inputField, passwordGridPane, passwordPasswordField,
				passwordTextField, passwordVisibilityImageView, passwordStatusLabel);
	}

	private void initializePasswordLengthField()
	{
		InputField inputField = new InputField(passwordLengthSpinner, false, Optional.empty(), Optional.empty(),
				Optional.of("ONLY_NUMBERS"), Optional.of(8), Optional.of(200), Optional.empty());

		passwordLengthFieldController = new PasswordLengthSpinnerController(inputField, passwordLengthSpinner);
	}

	// ================================================================================
	// Drag and Drop-Events
	// ================================================================================

	@FXML
	private void onDragExitedFolderPathTextField(DragEvent dragEvent)
	{
		folderPathFieldController.onDragExitedTextField(dragEvent);
	}

	@FXML
	private void onDragEnteredFolderPathTextField(DragEvent dragEvent)
	{
		folderPathFieldController.onDragEnteredTextField(dragEvent);
	}

	@FXML
	private void onDragOverFolderPathTextField(DragEvent dragEvent)
	{
		folderPathFieldController.onDragOverTextField(dragEvent);
	}

	@FXML
	private void onDragDroppedFolderPathTextField(DragEvent dragEvent)
	{
		folderPathFieldController.onDragDroppedTextField(dragEvent);
	}

	// ================================================================================
	// onAction-Events
	// ================================================================================

	@FXML
	private void onActionOpenDatabaseButton()
	{
		String pathInitialDirectory = System.getProperty("user.home");

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File(pathInitialDirectory));

		File directory = directoryChooser.showDialog(GuiBuilder.primaryStage);

		if (directory != null)
		{
			folderPathTextField.setText(directory.getAbsolutePath());
		}
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

	public void onActionLoginButton()
	{
		enableLiveValidation(true);

		validateFileName();
		validateFolderPath();
		validatePassword();

		if (fileNameFieldController.getInputField().isValid() && folderPathFieldController.getInputField().isValid()
				&& passwordFieldController.getInputField().isValid())
		{
			doLogin();
		}
		else
		{
			requestFocus();
		}
	}

	private void enableLiveValidation(boolean enable)
	{
		fileNameFieldController.setEnableLiveValidation(enable);
		folderPathFieldController.setEnableLiveValidation(enable);
		passwordFieldController.setEnableLiveValidation(enable);
	}

	private void doLogin()
	{
		File userDataFile = new File(folderPathFieldController.getInputField().getText() + "\\"
				+ fileNameFieldController.getInputField().getText() + ".ecdb");

		boolean fileExistsAndWasReplaced = UserData.handleUserDataFileExists(fileNameFieldController, userDataFile);

		if (fileExistsAndWasReplaced)
		{
			createDatabase(userDataFile);

			saveHistory(userDataFile);

			openMainScene();
		}
		else
		{
			requestFocus();
		}
	}

	private void createDatabase(File userDataFile)
	{
		UserData.setCashJournalFile(userDataFile);
		UserData.setPassword(passwordFieldController.getInputField().getText());

		try
		{
			DatabaseAccess.openDatabase(true);
		}
		catch (DatabasePasswordInvalidException e)
		{
			new UnexpectedBehaviourException();
		}
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

	private void validateFileName()
	{
		fileNameFieldController.validate();
	}

	private void validateFolderPath()
	{
		folderPathFieldController.validate();
	}

	private void validatePassword()
	{
		passwordFieldController.validate();
	}

	public void requestFocus()
	{
		if (!fileNameFieldController.getInputField().isValid())
		{
			fileNameFieldController.getInputField().requestFocus();
		}
		else if (!folderPathFieldController.getInputField().isValid())
		{
			folderPathFieldController.getInputField().requestFocus();
		}
		else if (!passwordFieldController.getInputField().isValid())
		{
			passwordFieldController.getInputField().requestFocus();
		}
		else
		{
			fileNameFieldController.getInputField().requestFocus();
		}
	}
}
