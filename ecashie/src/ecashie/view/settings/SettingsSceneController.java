package ecashie.view.settings;

import ecashie.controller.gui.GuiBuilder;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class SettingsSceneController
{	
	@FXML
	private AnchorPane generalSettingsScenePane;
	@FXML
	private AnchorPane bankaccountsSettingsScenePane;
	@FXML
	private AnchorPane categoriesSettingsScenePane;
	
	@FXML
	private void initialize()
	{
		GuiBuilder.embedPaneIntoScene(generalSettingsScenePane, "GeneralSettingsScene");
		GuiBuilder.embedPaneIntoScene(bankaccountsSettingsScenePane, "BankaccountsSettingsScene");
		GuiBuilder.embedPaneIntoScene(categoriesSettingsScenePane, "CategoriesSettingsScene");
	}
}
