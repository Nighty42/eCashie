package ecashie.view.main;

import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.gui.HyperlinkUtils;
import ecashie.controller.i18n.LanguageController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class MainSceneController
{
	@FXML
	private ToggleButton overviewToggleButton;
	@FXML
	private ToggleButton transactionsToggleButton;
	@FXML
	private ToggleButton statisticsToggleButton;
	@FXML
	private ToggleButton budgetplanToggleButton;

	@FXML
	private ToggleButton settingsToggleButton;

	@FXML
	private Label titleLabel;

	@FXML
	private AnchorPane menuScenePane;
	@FXML
	private AnchorPane overviewScenePane;
	@FXML
	private AnchorPane transactionsScenePane;
	@FXML
	private AnchorPane statisticsScenePane;
	@FXML
	private AnchorPane budgetplanScenePane;
	@FXML
	private AnchorPane manualScenePane;
	@FXML
	private AnchorPane aboutScenePane;
	@FXML
	private AnchorPane settingsScenePane;

	public static boolean showSettingsScene = false;

	private static MainSceneController instance;

	static
	{
		instance = new MainSceneController();
	}

	public static MainSceneController getInstance()
	{
		return instance;
	}

	@FXML
	private ToggleButton menuToggleButton;

	@FXML
	private GridPane sceneGridPane;

	@FXML
	private void initialize()
	{
		menuScenePane.setVisible(true);
		GuiBuilder.embedPaneIntoScene(overviewScenePane, "OverviewScene");
		GuiBuilder.embedPaneIntoScene(transactionsScenePane, "TransactionsScene");
		GuiBuilder.embedPaneIntoScene(statisticsScenePane, "StatisticsScene");
		GuiBuilder.embedPaneIntoScene(budgetplanScenePane, "BudgetplanScene");
		GuiBuilder.embedPaneIntoScene(manualScenePane, "ManualScene");
		GuiBuilder.embedPaneIntoScene(aboutScenePane, "AboutScene");
		GuiBuilder.embedPaneIntoScene(settingsScenePane, "SettingsScene");

		if (showSettingsScene)
		{
			settingsToggleButton.fire();
		}
		else
		{
			overviewToggleButton.fire();
		}

		sceneGridPane.getColumnConstraints().get(0).setMinWidth(Region.USE_COMPUTED_SIZE);
		sceneGridPane.getColumnConstraints().get(0).setPrefWidth(Region.USE_COMPUTED_SIZE);
		sceneGridPane.getColumnConstraints().get(0).setMaxWidth(Region.USE_COMPUTED_SIZE);
	}

	// ================================================================================
	// OnMouseClicked-Events
	// ================================================================================

	@FXML
	private void onActionMenuToggleButton()
	{
		if (menuToggleButton.isSelected())
		{
			sceneGridPane.getColumnConstraints().get(0).setMinWidth(Region.USE_COMPUTED_SIZE);
			sceneGridPane.getColumnConstraints().get(0).setPrefWidth(Region.USE_COMPUTED_SIZE);
			sceneGridPane.getColumnConstraints().get(0).setMaxWidth(Region.USE_COMPUTED_SIZE);

			menuScenePane.setVisible(true);
		}
		else
		{
			sceneGridPane.getColumnConstraints().get(0).setMinWidth(0);
			sceneGridPane.getColumnConstraints().get(0).setPrefWidth(0);
			sceneGridPane.getColumnConstraints().get(0).setMaxWidth(0);

			menuScenePane.setVisible(false);
		}
	}

	@FXML
	private void onActionBackButton()
	{

	}

	@FXML
	private void onActionForwardButton()
	{

	}

	@FXML
	private void onActionOverviewToggleButton()
	{
		selectionChangedScene("overview");
	}

	private void selectionChangedScene(String sceneName)
	{
		clearToggleButtonSelection();
		clearContentPaneVisibility();

		titleLabel.setText(LanguageController.getLocaleString(sceneName, null));

		switch (sceneName)
		{
			case "overview":
				overviewToggleButton.setSelected(true);
				overviewScenePane.setVisible(true);
				break;
			case "transactions":
				transactionsToggleButton.setSelected(true);
				transactionsScenePane.setVisible(true);
				break;
			case "statistics":
				statisticsToggleButton.setSelected(true);
				statisticsScenePane.setVisible(true);
				break;
			case "budgetplan":
				budgetplanToggleButton.setSelected(true);
				budgetplanScenePane.setVisible(true);
				break;
			case "settings":
				settingsToggleButton.setSelected(true);
				settingsScenePane.setVisible(true);
				break;
			case "manual":
				manualScenePane.setVisible(true);
				break;
			case "about":
				aboutScenePane.setVisible(true);
				break;
		}
	}

	@FXML
	private void onActionTransactionsToggleButton()
	{
		selectionChangedScene("transactions");
	}

	@FXML
	private void onActionStatisticsToggleButton()
	{
		selectionChangedScene("statistics");
	}

	@FXML
	private void onActionBudgetplanToggleButton()
	{
		selectionChangedScene("budgetplan");
	}

	@FXML
	private void onActionSettingsToggleButton()
	{
		selectionChangedScene("settings");
	}

	@FXML
	private void onActionManualMenuItem()
	{
		selectionChangedScene("manual");
	}

	@FXML
	private void onActionReportBugMenuItem()
	{
		HyperlinkUtils.openHyperlink("https://sourceforge.net/p/ecashie/tickets/new/");
	}

	@FXML
	private void onActionSuggestFeatureMenuItem()
	{
		HyperlinkUtils.openHyperlink("https://sourceforge.net/p/ecashie/feature-requests/new/");
	}

	@FXML
	private void onActionParticipateMenuItem()
	{
		HyperlinkUtils.openHyperlink("https://github.com/Nighty42/eCashie_java");
	}

	@FXML
	private void onActionAboutMenuItem()
	{
		selectionChangedScene("about");
	}

	private void clearToggleButtonSelection()
	{
		overviewToggleButton.setSelected(false);
		transactionsToggleButton.setSelected(false);
		statisticsToggleButton.setSelected(false);
		budgetplanToggleButton.setSelected(false);

		settingsToggleButton.setSelected(false);
	}

	private void clearContentPaneVisibility()
	{
		overviewScenePane.setVisible(false);
		transactionsScenePane.setVisible(false);
		statisticsScenePane.setVisible(false);
		budgetplanScenePane.setVisible(false);

		manualScenePane.setVisible(false);
		aboutScenePane.setVisible(false);

		settingsScenePane.setVisible(false);
	}
}
