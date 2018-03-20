package ecashie.view.start;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import ecashie.MainAppController;
import ecashie.controller.errorhandling.UnexpectedBehaviourException;
import ecashie.controller.gui.GuiBuilder;
import ecashie.model.settings.AppSettings;
import javafx.fxml.FXML;
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
		MainAppController.exitApplication();
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

	public static void saveHistory(File userDataFile)
	{
		try
		{
			AppSettings.recentUsedDatabase = userDataFile.getAbsolutePath();
			
			AppSettings.write();
		}
		catch (ParserConfigurationException | TransformerException | IOException e)
		{
			new UnexpectedBehaviourException();
		}
	}
}
