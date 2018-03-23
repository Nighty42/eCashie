package ecashie.view.main;

import java.io.IOException;

import ecashie.controller.appdetails.AppDetails;
import ecashie.controller.gui.HyperlinkUtils;
import ecashie.controller.utilities.GeneralOperations;
import ecashie.main.MainApp;
import ecashie.model.appdetails.AppVersion;
import ecashie.model.appdetails.Contributor;
import ecashie.model.appdetails.ImageSource;
import ecashie.model.i18n.ResourceBundleString;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;

public class AboutSceneController
{
	/*
	 * General
	 */
	@FXML
	private Label versionNumberLabel;
	@FXML
	private Label releaseDateLabel;
	@FXML
	private Hyperlink lastReleaseHyperlink;

	/*
	 * Tab "License"
	 */
	@FXML
	private Label copyrightTextLabel;

	/*
	 * Tab "Contributor"
	 */
	@FXML
	private TableView<Contributor> contributorTableView;
	@FXML
	private TableColumn<Contributor, String> contributorNameTableColumn;
	@FXML
	private TableColumn<Contributor, String> contributorRolesTableColumn;

	/*
	 * Tab "Graphic Design"
	 */
	@FXML
	private TableView<ImageSource> imageSourcesTableView;
	@FXML
	private TableColumn<ImageSource, String> imageSourcesIdentifierTableColumn;
	@FXML
	private TableColumn<ImageSource, String> imageSourcesDesignerTableColumn;
	@FXML
	private TableColumn<ImageSource, String> imageSourcesHomepageTableColumn;
	@FXML
	private TableColumn<ImageSource, String> imageSourcesLicenseTableColumn;
	@FXML
	private TableColumn<ImageSource, String> imageSourcesSourceTableColumn;
	@FXML
	private TableColumn<ImageSource, String> imageSourcesCommentTableColumn;

	/*
	 * Tab "Version Notes"
	 */
	@FXML
	private ComboBox<AppVersion> versionNumberComboBox;

	@FXML
	private GridPane newFunctionsGridPane;
	@FXML
	private GridPane importantChangesGridPane;
	@FXML
	private GridPane bugFixesGridPane;

	@FXML
	private void initialize() throws IOException
	{
		initializeVersionDetails();

		initializeCopyrightText();

		initializeVersionNumberComboBox();

		initializeContributorTableView();

		initializeImageSourcesTableView();
	}

	private void initializeVersionDetails()
	{
		versionNumberLabel.setText(AppDetails.VersionNumber);
		releaseDateLabel.setText(AppDetails.ReleaseDate);
	}

	private void initializeCopyrightText()
	{
		String copyrightText = "";
		String currentYear = GeneralOperations.currentYear();
		String contributor = ResourceBundleString.getLocaleString("contributor", null);

		if (currentYear.equals(AppDetails.DevelopmentBegin))
		{
			copyrightText = "Copyright (c) " + currentYear + " eCashie " + contributor;
		}
		else
		{
			copyrightText = "Copyright (c) " + AppDetails.DevelopmentBegin + " - " + currentYear + " eCashie "
					+ contributor;
		}

		copyrightTextLabel.setText(copyrightText);
	}

	private void initializeVersionNumberComboBox()
	{
		versionNumberComboBox.setItems(AppVersion.AppVersionList);

		Callback<ListView<AppVersion>, ListCell<AppVersion>> cellFactory = new Callback<ListView<AppVersion>, ListCell<AppVersion>>()
		{
			@Override
			public ListCell<AppVersion> call(ListView<AppVersion> l)
			{
				return new ListCell<AppVersion>()
				{
					@Override
					protected void updateItem(AppVersion item, boolean empty)
					{
						super.updateItem(item, empty);

						if (item == null || empty)
						{
							setGraphic(null);
						}
						else
						{
							setText(item.getVersionNumber());
						}
					}
				};
			}
		};

		versionNumberComboBox.setButtonCell(cellFactory.call(null));
		versionNumberComboBox.setCellFactory(cellFactory);

		versionNumberComboBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					onSelectionChangedVersionNumberComboBox(newValue);
				});

		versionNumberComboBox.getSelectionModel().selectLast();
	}

	private void initializeContributorTableView()
	{
		contributorNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		contributorRolesTableColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

		contributorTableView.setItems(Contributor.ContributorList);
	}

	private void initializeImageSourcesTableView()
	{
		initializeImageSourceTableColumnImage(imageSourcesIdentifierTableColumn, "identifier");

		initializeImageSourceTableColumnDesigner(imageSourcesDesignerTableColumn, "designer");

		initializeImageSourceTableColumnLicense(imageSourcesLicenseTableColumn, "license");

		initializeImageSourceTableColumnHomepage(imageSourcesHomepageTableColumn, "homepage");

		initializeImageSourceTableColumnSource(imageSourcesSourceTableColumn, "source");

		initializeImageSourceTableColumnComment(imageSourcesCommentTableColumn, "comment");

		imageSourcesTableView.setItems(ImageSource.ImageSourceList);
		imageSourcesTableView.getSortOrder().add(imageSourcesDesignerTableColumn);
	}

	private void initializeImageSourceTableColumnImage(TableColumn<ImageSource, String> tableColumn,
			String cellValueProperty)
	{
		tableColumn.setCellValueFactory(new PropertyValueFactory<>(cellValueProperty));
		tableColumn.setCellFactory(new Callback<TableColumn<ImageSource, String>, TableCell<ImageSource, String>>()
		{
			@Override
			public TableCell<ImageSource, String> call(TableColumn<ImageSource, String> param)
			{
				final ImageView imageView = new ImageView();
				imageView.setFitHeight(30);
				imageView.setFitWidth(30);

				TableCell<ImageSource, String> tableCell = new TableCell<ImageSource, String>()
				{
					@Override
					public void updateItem(String item, boolean empty)
					{
						if (item != null)
						{
							imageView.setImage(
									new Image(MainApp.class.getResourceAsStream("/ecashie/resources/img/" + item)));
						}
					}
				};

				tableCell.setAlignment(Pos.CENTER_LEFT);
				tableCell.setGraphic(imageView);

				return tableCell;
			}
		});
	}

	private void initializeImageSourceTableColumnDesigner(TableColumn<ImageSource, String> tableColumn,
			String cellValueProperty)
	{
		tableColumn.setCellValueFactory(new PropertyValueFactory<>(cellValueProperty));
		tableColumn.setCellFactory(new Callback<TableColumn<ImageSource, String>, TableCell<ImageSource, String>>()
		{
			@Override
			public TableCell<ImageSource, String> call(TableColumn<ImageSource, String> param)
			{
				final Label label = new Label();

				TableCell<ImageSource, String> tableCell = new TableCell<ImageSource, String>()
				{
					@Override
					public void updateItem(String item, boolean empty)
					{
						if (item != null && !item.isEmpty())
						{
							label.setText(item);
						}
					}
				};

				tableCell.setAlignment(Pos.CENTER_LEFT);
				tableCell.setGraphic(label);

				return tableCell;
			}
		});
	}

	private void initializeImageSourceTableColumnComment(TableColumn<ImageSource, String> tableColumn,
			String cellValueProperty)
	{
		tableColumn.setCellValueFactory(new PropertyValueFactory<>(cellValueProperty));
		tableColumn.setCellFactory(new Callback<TableColumn<ImageSource, String>, TableCell<ImageSource, String>>()
		{
			@Override
			public TableCell<ImageSource, String> call(TableColumn<ImageSource, String> param)
			{
				final Label label = new Label();

				TableCell<ImageSource, String> tableCell = new TableCell<ImageSource, String>()
				{
					@Override
					public void updateItem(String item, boolean empty)
					{
						if (item != null)
						{
							label.setText(item);
						}
					}
				};

				tableCell.setAlignment(Pos.CENTER_LEFT);
				tableCell.setGraphic(label);

				return tableCell;
			}
		});
	}

	private void initializeImageSourceTableColumnLicense(TableColumn<ImageSource, String> tableColumn,
			String cellValueProperty)
	{
		tableColumn.setCellValueFactory(new PropertyValueFactory<>(cellValueProperty));
		tableColumn.setCellFactory(new Callback<TableColumn<ImageSource, String>, TableCell<ImageSource, String>>()
		{
			@Override
			public TableCell<ImageSource, String> call(TableColumn<ImageSource, String> param)
			{
				final Label label = new Label();

				TableCell<ImageSource, String> tableCell = new TableCell<ImageSource, String>()
				{
					@Override
					public void updateItem(String item, boolean empty)
					{
						if (item != null)
						{
							label.setText(item);
						}
					}
				};

				tableCell.setAlignment(Pos.CENTER_LEFT);
				tableCell.setGraphic(label);

				return tableCell;
			}
		});
	}

	private void initializeImageSourceTableColumnHomepage(TableColumn<ImageSource, String> tableColumn,
			String cellValueProperty)
	{
		tableColumn.setCellValueFactory(new PropertyValueFactory<>(cellValueProperty));
		tableColumn.setCellFactory(new Callback<TableColumn<ImageSource, String>, TableCell<ImageSource, String>>()
		{
			@Override
			public TableCell<ImageSource, String> call(TableColumn<ImageSource, String> param)
			{
				final Hyperlink hyperlink = new Hyperlink();

				TableCell<ImageSource, String> tableCell = new TableCell<ImageSource, String>()
				{
					@Override
					public void updateItem(String item, boolean empty)
					{
						if (item != null && !item.isEmpty())
						{
							hyperlink.setText(item);
							hyperlink.setOnAction(new EventHandler<ActionEvent>()
							{
								@Override
								public void handle(ActionEvent t)
								{
									Hyperlink hyperlink = (Hyperlink) t.getSource();

									HyperlinkUtils.openHyperlink(hyperlink.getText());
								}
							});
						}
					}
				};

				tableCell.setAlignment(Pos.CENTER_LEFT);
				tableCell.setGraphic(hyperlink);

				return tableCell;
			}
		});
	}

	private void initializeImageSourceTableColumnSource(TableColumn<ImageSource, String> tableColumn,
			String cellValueProperty)
	{
		tableColumn.setCellValueFactory(new PropertyValueFactory<>(cellValueProperty));
		tableColumn.setCellFactory(new Callback<TableColumn<ImageSource, String>, TableCell<ImageSource, String>>()
		{
			@Override
			public TableCell<ImageSource, String> call(TableColumn<ImageSource, String> param)
			{
				final Hyperlink hyperlink = new Hyperlink();

				TableCell<ImageSource, String> tableCell = new TableCell<ImageSource, String>()
				{
					@Override
					public void updateItem(String item, boolean empty)
					{
						if (item != null)
						{
							hyperlink.setText(item);
							hyperlink.setOnAction(new EventHandler<ActionEvent>()
							{
								@Override
								public void handle(ActionEvent t)
								{
									Hyperlink hyperlink = (Hyperlink) t.getSource();

									HyperlinkUtils.openHyperlink(hyperlink.getText());
								}
							});
						}
					}
				};

				tableCell.setAlignment(Pos.CENTER_LEFT);
				tableCell.setGraphic(hyperlink);

				return tableCell;
			}
		});
	}

	protected void onSelectionChangedVersionNumberComboBox(AppVersion newAppVersion)
	{
		AppVersion selectedAppVersion = versionNumberComboBox.getSelectionModel().getSelectedItem();

		fillInNewFunctions(selectedAppVersion);

		fillInImportantChanges(selectedAppVersion);

		fillInBugFixes(selectedAppVersion);
	}

	private void fillInNewFunctions(AppVersion selectedAppVersion)
	{
		newFunctionsGridPane.getChildren().clear();
		newFunctionsGridPane.getRowConstraints().clear();

		for (int i = 0; i < selectedAppVersion.getNewFunctions().size(); i++)
		{
			setRowConstraints(newFunctionsGridPane, i);

			Label bullet = createBulletLabel();
			Label value = createEntryLabel(selectedAppVersion.getNewFunctions().get(i));

			newFunctionsGridPane.add(bullet, 0, i);
			newFunctionsGridPane.add(value, 1, i);
		}
	}

	private void fillInImportantChanges(AppVersion selectedAppVersion)
	{
		importantChangesGridPane.getChildren().clear();
		importantChangesGridPane.getRowConstraints().clear();

		for (int i = 0; i < selectedAppVersion.getImportantChanges().size(); i++)
		{
			setRowConstraints(importantChangesGridPane, i);

			Label bullet = createBulletLabel();
			Label value = createEntryLabel(selectedAppVersion.getImportantChanges().get(i));

			importantChangesGridPane.add(bullet, 0, i);
			importantChangesGridPane.add(value, 1, i);
		}
	}

	private void fillInBugFixes(AppVersion selectedAppVersion)
	{
		bugFixesGridPane.getChildren().clear();
		bugFixesGridPane.getRowConstraints().clear();

		for (int i = 0; i < selectedAppVersion.getBugFixes().size(); i++)
		{
			setRowConstraints(bugFixesGridPane, i);

			Label bullet = createBulletLabel();
			Label value = createEntryLabel(selectedAppVersion.getBugFixes().get(i));

			bugFixesGridPane.add(bullet, 0, i);
			bugFixesGridPane.add(value, 1, i);
		}
	}

	private void setRowConstraints(GridPane gridPane, int i)
	{
		RowConstraints rowConstraints = defineRowConstraints();

		gridPane.addRow(i);
		gridPane.getRowConstraints().add(i, rowConstraints);
	}

	private RowConstraints defineRowConstraints()
	{
		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setVgrow(Priority.NEVER);

		return rowConstraints;
	}

	private Label createBulletLabel()
	{
		Label bullet = new Label("\u25CF");
		setCssStyleForBullet(bullet);
		setSettingsForBullet(bullet);

		return bullet;
	}

	private void setCssStyleForBullet(Label bullet)
	{
		bullet.setStyle("-fx-font-size: 16px;");
	}

	private void setSettingsForBullet(Label bullet)
	{
		GridPane.setValignment(bullet, VPos.TOP);
	}

	private Label createEntryLabel(String value)
	{
		Label entry = new Label(value);
		setCssStyleForEntry(entry);
		setSettingsForEntry(entry);

		return entry;
	}

	private void setCssStyleForEntry(Label value)
	{
		value.setStyle("-fx-font-size: 16px; -fx-font-family: Open Sans; -fx-wrap-text: true;");
	}

	private void setSettingsForEntry(Label value)
	{
		GridPane.setValignment(value, VPos.TOP);
	}

	@FXML
	private void onActionLastReleaseHyperlink()
	{
		String lastReleaseHyperlinkText = lastReleaseHyperlink.getText();

		HyperlinkUtils.openHyperlink(lastReleaseHyperlinkText);
	}
}