package ecashie.view.start;

import java.io.File;
import java.util.Optional;

import ecashie.controller.database.DatabaseAccess;
import ecashie.controller.exception.DatabasePasswordInvalidException;
import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.gui.Navigation;
import ecashie.controller.settings.AppSettings;
import ecashie.controller.settings.UserData;
import ecashie.main.AppLoader;
import ecashie.view.inputfields.FileFolderPathFieldController;
import ecashie.view.inputfields.InputField;
import ecashie.view.inputfields.PasswordFieldController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
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

		initFolderPathChange();

		initPasswordChange();

		initRecentUsedDatabase();

		requestFocus();
	}

	private void initFolderPathChange()
	{
		InputField inputField = new InputField(filePathTextField, true, Optional.of(3), Optional.of(200),
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

		inputField.setIsFile(".ecdb");

		filePathFieldController = new FileFolderPathFieldController(inputField, filePathGridPane, filePathTextField,
				filePathStatusLabel);
	}

	private void initPasswordChange()
	{
		InputField inputField = new InputField(passwordPasswordField, false, Optional.of(8), Optional.of(200),
				Optional.of("PASSWORD"), Optional.empty(), Optional.empty(), Optional.empty());

		passwordFieldController = new PasswordFieldController(inputField, passwordGridPane, passwordPasswordField,
				passwordTextField, passwordVisibilityImageView, passwordStatusLabel);
	}

	private void initRecentUsedDatabase()
	{
		if (!AppSettings.RecentUsedDatabase.isEmpty())
		{
			filePathFieldController.getInputField().setText(AppSettings.RecentUsedDatabase);

			saveHistoryCheckBox.setSelected(true);
		}
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

		File file = fileChooser.showOpenDialog(GuiBuilder.PrimaryStage);

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
		GuiBuilder.PrimaryStage.hide();

		Task<Void> loginTask = new Task<Void>()
		{
			@Override
			protected Void call()
			{
				try
				{
					openDatabase();

					saveHistory(userDataFile);
					
					if (AppLoader.IsFailed)
					{
						this.cancel();
					}
					else
					{
						openMainScene();
					}
				}
				catch (DatabasePasswordInvalidException e)
				{
					passwordFieldController.getInputField().setError("invalidPassword");
					passwordFieldController.getInputField().visualizeStatus(passwordGridPane, passwordStatusLabel);

					passwordFieldController.getInputField().requestFocus();
				}

				return null;
			}
		};

		loginTask.setOnSucceeded(new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent t)
			{
				AppLoader.loaderStage.hide();
				GuiBuilder.PrimaryStage.show();
			}
		});

		loginTask.setOnCancelled(new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent arg0)
			{
				AppLoader.loaderStage.close();
				GuiBuilder.PrimaryStage.close();
			}
		});

		AppLoader.notifyPreloader(0, "Initialize Preloader");
		AppLoader.loaderStage.show();

		new Thread(loginTask).start();
	}

	private void openDatabase() throws DatabasePasswordInvalidException
	{
		UserData.setCashJournalFile(new File(filePathFieldController.getInputField().getText()));
		UserData.setPassword(passwordFieldController.getInputField().getText());

		DatabaseAccess.openDatabase(false);
	}

	private void saveHistory(File userDataFile)
	{
		StartSceneController.saveHistory(saveHistoryCheckBox, userDataFile);
	}

	private void openMainScene()
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				Navigation.Next = "MainScene";
				Navigation.goForward(true);
			}
		});
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
