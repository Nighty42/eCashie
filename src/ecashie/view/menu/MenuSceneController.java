package ecashie.view.menu;

import ecashie.controller.utilities.HyperlinkUtils;
import ecashie.view.main.MainSceneController;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;

public class MenuSceneController
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
	private TitledPane helpTitledPane;
	@FXML
	private ToggleButton manualToggleButton;
	@FXML
	private ToggleButton reportBugToggleButton;
	@FXML
	private ToggleButton suggestFeatureToggleButton;
	@FXML
	private ToggleButton participateToggleButton;
	@FXML
	private ToggleButton aboutToggleButton;

	@FXML
	private TitledPane settingsTitledPane;
	@FXML
	private ToggleButton generalSettingsToggleButton;
	@FXML
	private ToggleButton bankaccountsSettingsToggleButton;
	@FXML
	private ToggleButton categoriesSettingsToggleButton;

	public static boolean showSettingsScene = false;

	@FXML
	private void initialize()
	{
		if (showSettingsScene)
		{
			onActionGeneralSettingsToggleButton();

			settingsTitledPane.setExpanded(true);
		}
		else
		{
			onActionOverviewToggleButton();
		}
	}

	@FXML
	private void onActionOverviewToggleButton()
	{
		clearTitledPaneSelection();
		clearToggleButtonSelection();
		clearContentPaneVisibility();

		overviewToggleButton.setSelected(true);
		MainSceneController.getInstance().getOverviewScenePane().setVisible(true);
	}

	@FXML
	private void onActionTransactionsToggleButton()
	{
		clearTitledPaneSelection();
		clearToggleButtonSelection();
		clearContentPaneVisibility();

		transactionsToggleButton.setSelected(true);
		MainSceneController.getInstance().getTransactionsScenePane().setVisible(true);
	}

	@FXML
	private void onActionStatisticsToggleButton()
	{
		clearTitledPaneSelection();
		clearToggleButtonSelection();
		clearContentPaneVisibility();

		statisticsToggleButton.setSelected(true);
		MainSceneController.getInstance().getStatisticsScenePane().setVisible(true);
	}

	@FXML
	private void onActionBudgetplanToggleButton()
	{
		clearTitledPaneSelection();
		clearToggleButtonSelection();
		clearContentPaneVisibility();

		budgetplanToggleButton.setSelected(true);
		MainSceneController.getInstance().getBudgetplanScenePane().setVisible(true);
	}

	@FXML
	private void onActionManualButton()
	{
		HyperlinkUtils.openHyperlink("https://github.com/mariusraht1/eCashie/wiki");
		manualToggleButton.setSelected(false);
	}

	@FXML
	private void onActionReportBugButton()
	{
		HyperlinkUtils.openHyperlink("https://github.com/mariusraht1/eCashie/issues/new");
		reportBugToggleButton.setSelected(false);
	}

	@FXML
	private void onActionSuggestFeatureButton()
	{
		HyperlinkUtils.openHyperlink("https://github.com/mariusraht1/eCashie/issues/new");
		suggestFeatureToggleButton.setSelected(false);
	}

	@FXML
	private void onActionParticipateButton()
	{
		HyperlinkUtils.openHyperlink("https://github.com/Nighty42/eCashie");
		participateToggleButton.setSelected(false);
	}

	@FXML
	private void onActionAboutToggleButton()
	{
		clearToggleButtonSelection();
		clearContentPaneVisibility();

		aboutToggleButton.setSelected(true);
		MainSceneController.getInstance().getAboutScenePane().setVisible(true);
	}

	@FXML
	private void onActionGeneralSettingsToggleButton()
	{
		clearToggleButtonSelection();
		clearContentPaneVisibility();

		generalSettingsToggleButton.setSelected(true);
		MainSceneController.getInstance().getGeneralSettingsScenePane().setVisible(true);
	}

	@FXML
	private void onActionBankaccountsSettingsToggleButton()
	{
		clearToggleButtonSelection();
		clearContentPaneVisibility();

		bankaccountsSettingsToggleButton.setSelected(true);
		MainSceneController.getInstance().getBankaccountsSettingsScenePane().setVisible(true);
	}

	@FXML
	private void onActionCategoriesSettingsToggleButton()
	{
		clearToggleButtonSelection();
		clearContentPaneVisibility();

		categoriesSettingsToggleButton.setSelected(true);
		MainSceneController.getInstance().getCategoriesSettingsScenePane().setVisible(true);
	}

	@FXML
	private void onMouseClickedHelpTitledPane()
	{
		settingsTitledPane.setExpanded(false);
		clearToggleButtonSelection();
	}
	
	@FXML
	private void onMouseClickedSettingsTitledPane()
	{
		helpTitledPane.setExpanded(false);
		clearToggleButtonSelection();
	}

	private void clearTitledPaneSelection()
	{
		helpTitledPane.setExpanded(false);
		settingsTitledPane.setExpanded(false);
	}
	
	private void clearToggleButtonSelection()
	{
		overviewToggleButton.setSelected(false);
		transactionsToggleButton.setSelected(false);
		statisticsToggleButton.setSelected(false);
		budgetplanToggleButton.setSelected(false);

		manualToggleButton.setSelected(false);
		reportBugToggleButton.setSelected(false);
		suggestFeatureToggleButton.setSelected(false);
		participateToggleButton.setSelected(false);
		aboutToggleButton.setSelected(false);

		generalSettingsToggleButton.setSelected(false);
		bankaccountsSettingsToggleButton.setSelected(false);
		categoriesSettingsToggleButton.setSelected(false);
	}

	private void clearContentPaneVisibility()
	{
		MainSceneController.getInstance().getOverviewScenePane().setVisible(false);
		MainSceneController.getInstance().getTransactionsScenePane().setVisible(false);
		MainSceneController.getInstance().getStatisticsScenePane().setVisible(false);
		MainSceneController.getInstance().getBudgetplanScenePane().setVisible(false);

		MainSceneController.getInstance().getAboutScenePane().setVisible(false);

		MainSceneController.getInstance().getGeneralSettingsScenePane().setVisible(false);
		MainSceneController.getInstance().getBankaccountsSettingsScenePane().setVisible(false);
		MainSceneController.getInstance().getCategoriesSettingsScenePane().setVisible(false);
	}
}
