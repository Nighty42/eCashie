package ecashie.view.menu;

import ecashie.controller.gui.HyperlinkUtils;
import ecashie.view.main.MainSceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
	private Button manualButton;
	@FXML
	private Button reportBugButton;
	@FXML
	private Button suggestFeatureButton;
	@FXML
	private Button participateButton;
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

		overviewToggleButton.setSelected(true);

		clearContentPaneVisibility();

		MainSceneController.getInstance().getOverviewScenePane().setVisible(true);
	}

	@FXML
	private void onActionTransactionsToggleButton()
	{
		clearTitledPaneSelection();
		
		clearToggleButtonSelection();

		transactionsToggleButton.setSelected(true);

		clearContentPaneVisibility();

		MainSceneController.getInstance().getTransactionsScenePane().setVisible(true);
	}

	@FXML
	private void onActionStatisticsToggleButton()
	{
		clearTitledPaneSelection();
		
		clearToggleButtonSelection();

		statisticsToggleButton.setSelected(true);

		clearContentPaneVisibility();

		MainSceneController.getInstance().getStatisticsScenePane().setVisible(true);
	}

	@FXML
	private void onActionBudgetplanToggleButton()
	{
		clearTitledPaneSelection();
		
		clearToggleButtonSelection();

		budgetplanToggleButton.setSelected(true);

		clearContentPaneVisibility();

		MainSceneController.getInstance().getBudgetplanScenePane().setVisible(true);
	}

	@FXML
	private void onActionManualButton()
	{
		// TODO: Create Manual
	}

	@FXML
	private void onActionReportBugButton()
	{
		HyperlinkUtils.openHyperlink("https://sourceforge.net/p/ecashie/tickets/new/");
	}

	@FXML
	private void onActionSuggestFeatureButton()
	{
		HyperlinkUtils.openHyperlink("https://sourceforge.net/p/ecashie/feature-requests/new/");
	}

	@FXML
	private void onActionParticipateButton()
	{
		HyperlinkUtils.openHyperlink("https://github.com/Nighty42/eCashie");
	}

	@FXML
	private void onActionAboutToggleButton()
	{
		clearToggleButtonSelection();

		aboutToggleButton.setSelected(true);

		clearContentPaneVisibility();

		MainSceneController.getInstance().getAboutScenePane().setVisible(true);
	}

	@FXML
	private void onActionGeneralSettingsToggleButton()
	{
		clearToggleButtonSelection();

		generalSettingsToggleButton.setSelected(true);

		clearContentPaneVisibility();

		MainSceneController.getInstance().getGeneralSettingsScenePane().setVisible(true);
	}

	@FXML
	private void onActionBankaccountsSettingsToggleButton()
	{
		clearToggleButtonSelection();

		bankaccountsSettingsToggleButton.setSelected(true);

		clearContentPaneVisibility();

		MainSceneController.getInstance().getBankaccountsSettingsScenePane().setVisible(true);
	}

	@FXML
	private void onActionCategoriesSettingsToggleButton()
	{
		clearToggleButtonSelection();

		categoriesSettingsToggleButton.setSelected(true);

		clearContentPaneVisibility();

		MainSceneController.getInstance().getCategoriesSettingsScenePane().setVisible(true);
	}

	@FXML
	private void onMouseClickedHelpTitledPane()
	{
		settingsTitledPane.setExpanded(false);
	}
	
	@FXML
	private void onMouseClickedSettingsTitledPane()
	{
		helpTitledPane.setExpanded(false);
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
