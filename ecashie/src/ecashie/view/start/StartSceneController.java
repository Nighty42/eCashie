package ecashie.view.start;

import java.io.File;

import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.settings.AppSettings;
import ecashie.main.ExitApp;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class StartSceneController
{
	@FXML
	private GridPane gridPane;

	@FXML
	private AnchorPane openCashJournalScenePane;
	@FXML
	private AnchorPane createCashJournalScenePane;

	@FXML
	private ToggleButton openToggleButton;
	@FXML
	private ToggleButton createToggleButton;

	@FXML
	private void initialize()
	{
		GuiBuilder.embedPaneIntoScene(openCashJournalScenePane, "OpenCashJournalScene");
		GuiBuilder.embedPaneIntoScene(createCashJournalScenePane, "CreateCashJournalScene");

		openCashJournalScenePane.managedProperty().bind(openCashJournalScenePane.visibleProperty());
		createCashJournalScenePane.managedProperty().bind(createCashJournalScenePane.visibleProperty());

		openToggleButton.fire();
	}

	@FXML
	private void onActionOpenToggleButton()
	{
		OpenCashJournalSceneController.getInstance().requestFocus();

		openToggleButton.setSelected(true);
		createToggleButton.setSelected(false);

		openCashJournalScenePane.setVisible(true);
		createCashJournalScenePane.setVisible(false);

		GuiBuilder.primaryStage.sizeToScene();
	}

	@FXML
	private void onActionCreateToggleButton()
	{
		CreateCashJournalSceneController.getInstance().requestFocus();

		openToggleButton.setSelected(false);
		createToggleButton.setSelected(true);

		openCashJournalScenePane.setVisible(false);
		createCashJournalScenePane.setVisible(true);

		GuiBuilder.primaryStage.sizeToScene();
	}

	@FXML
	private void onActionExitButton()
	{
		ExitApp.exit();
	}

	@FXML
	private void onActionLoginButton()
	{
		if (openToggleButton.isSelected())
		{
			OpenCashJournalSceneController.getInstance().onActionLoginButton();
		}
		else
		{
			CreateCashJournalSceneController.getInstance().onActionLoginButton();
		}
	}

	public static void saveHistory(CheckBox saveHistoryCheckBox, File userDataFile)
	{
		if (saveHistoryCheckBox.isSelected())
		{
			AppSettings.RecentUsedDatabase = userDataFile.getAbsolutePath();
		}
		else
		{
			AppSettings.RecentUsedDatabase = "";
		}
	}
}
