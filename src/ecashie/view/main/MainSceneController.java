package ecashie.view.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import ecashie.controller.gui.GuiBuilder;
import ecashie.controller.utilities.HyperlinkUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class MainSceneController
{
	@FXML
	private Label timeLabel;

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
	private AnchorPane aboutScenePane;
	@FXML
	private AnchorPane generalSettingsScenePane;
	@FXML
	private AnchorPane bankaccountsSettingsScenePane;
	@FXML
	private AnchorPane categoriesSettingsScenePane;

	private static MainSceneController instance = null;

	public static MainSceneController getInstance()
	{
		return instance;
	}

	public AnchorPane getOverviewScenePane()
	{
		return overviewScenePane;
	}

	public AnchorPane getTransactionsScenePane()
	{
		return transactionsScenePane;
	}

	public AnchorPane getStatisticsScenePane()
	{
		return statisticsScenePane;
	}

	public AnchorPane getBudgetplanScenePane()
	{
		return budgetplanScenePane;
	}

	public AnchorPane getAboutScenePane()
	{
		return aboutScenePane;
	}

	public AnchorPane getGeneralSettingsScenePane()
	{
		return generalSettingsScenePane;
	}

	public AnchorPane getBankaccountsSettingsScenePane()
	{
		return bankaccountsSettingsScenePane;
	}

	public AnchorPane getCategoriesSettingsScenePane()
	{
		return categoriesSettingsScenePane;
	}

	@FXML
	private ToggleButton menuToggleButton;

	@FXML
	private GridPane sceneGridPane;

	@FXML
	private void initialize()
	{
		instance = this;

		GuiBuilder.embedPaneIntoScene(menuScenePane, "MenuScene");
		GuiBuilder.embedPaneIntoScene(overviewScenePane, "OverviewScene");
		GuiBuilder.embedPaneIntoScene(transactionsScenePane, "TransactionsScene");
		GuiBuilder.embedPaneIntoScene(statisticsScenePane, "StatisticsScene");
		GuiBuilder.embedPaneIntoScene(budgetplanScenePane, "BudgetplanScene");
		GuiBuilder.embedPaneIntoScene(aboutScenePane, "AboutScene");
		GuiBuilder.embedPaneIntoScene(generalSettingsScenePane, "GeneralSettingsScene");
		GuiBuilder.embedPaneIntoScene(bankaccountsSettingsScenePane, "BankaccountsSettingsScene");
		GuiBuilder.embedPaneIntoScene(categoriesSettingsScenePane, "CategoriesSettingsScene");

		menuToggleButton.fire();

		sceneGridPane.getColumnConstraints().get(0).setMinWidth(Region.USE_COMPUTED_SIZE);
		sceneGridPane.getColumnConstraints().get(0).setPrefWidth(Region.USE_COMPUTED_SIZE);
		sceneGridPane.getColumnConstraints().get(0).setMaxWidth(Region.USE_COMPUTED_SIZE);

		menuScenePane.setVisible(true);
	}

	@SuppressWarnings("unused")
	private void showTime()
	{
		final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {

			SimpleDateFormat sdf = new SimpleDateFormat("EEEE - dd. MMMM yyyy HH:mm:ss");
			Date now = new Date();
			String strDate = sdf.format(now);

			timeLabel.setText(strDate);

		}));

		timeline.setCycleCount(Animation.INDEFINITE);

		timeline.play();
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
		HyperlinkUtils.openHyperlink("https://github.com/Nighty42/eCashie");
	}
}
