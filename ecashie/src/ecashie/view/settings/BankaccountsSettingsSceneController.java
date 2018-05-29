package ecashie.view.settings;

import java.util.Comparator;
import java.util.Optional;

import ecashie.controller.exception.UnexpectedBehaviourException;
import ecashie.controller.settings.UserData;
import ecashie.controller.validation.Validation;
import ecashie.model.database.Bankaccount;
import ecashie.model.database.BankaccountType;
import ecashie.view.inputfields.InputField;
import ecashie.view.inputfields.TextFieldController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class BankaccountsSettingsSceneController
{
	@FXML
	private TitledPane accountTypeATitledPane;
	@FXML
	private ListView<String> accountTypeAListView;
	@FXML
	private TitledPane accountTypeBTitledPane;
	@FXML
	private ListView<String> accountTypeBListView;
	@FXML
	private TitledPane accountTypeCTitledPane;
	@FXML
	private ListView<String> accountTypeCListView;
	@FXML
	private TitledPane accountTypeDTitledPane;
	@FXML
	private ListView<String> accountTypeDListView;
	@FXML
	private TitledPane accountTypeETitledPane;
	@FXML
	private ListView<String> accountTypeEListView;
	
	@FXML
	private ComboBox<BankaccountType> accountTypeComboBox;
	@FXML
	private ImageView accountIconImageView;

	@FXML
	private GridPane accountNameGridPane;
	@FXML
	private TextField accountNameTextField;
	@FXML
	private Label accountNameStatusLabel;

	@FXML
	private Button resetButton;
	@FXML
	private Button saveButton;

	private TextFieldController accountNameFieldController;

	private ObjectProperty<Bankaccount> selectedBankaccount = new SimpleObjectProperty<>();
	private TreeItem<String> selectedBankaccountType = null;

	@FXML
	private void initialize()
	{
		initSelectionBankaccount();

		initAccountTypeList();

		initAccountNameTextField();

		initAccountTypeTitledPanes();

		initAccountTypeListViews();

		initAccountTypeComboBox();
	}

	private void initSelectionBankaccount()
	{
		selectedBankaccount.addListener(new ChangeListener<Bankaccount>()
		{
			@Override
			public void changed(ObservableValue<? extends Bankaccount> observable, Bankaccount oldValue, Bankaccount newValue)
			{
				accountTypeComboBox.getSelectionModel().select(newValue.getBankaccountType());
			}
		});
	}

	private void initAccountTypeList()
	{
		FXCollections.sort(BankaccountType.getList(), new Comparator<BankaccountType>()
		{
			@Override
			public int compare(BankaccountType o1, BankaccountType o2)
			{
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	private void initAccountNameTextField()
	{
		InputField inputField = new InputField(accountNameTextField, true, Optional.of(1), Optional.of(50), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

		accountNameFieldController = new TextFieldController(inputField, accountNameGridPane, accountNameTextField, accountNameStatusLabel);

		accountNameFieldController.getInputField().setText(UserData.getCashJournalName());

		accountNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (Validation.fireOnTextChangeInputField)
			{
				setButtonStatusLoginData();
			}
		});

		accountNameFieldController.setEnableLiveValidation(true);
	}

	private void initAccountTypeTitledPanes()
	{
		for (int i = 0; i < BankaccountType.getList().size(); ++i)
		{
			BankaccountType bankAccountType = BankaccountType.getList().get(i);
			TitledPane accountTypeTitledPane = null;

			switch (i)
			{
				case 0:
					accountTypeTitledPane = accountTypeATitledPane;
					break;
				case 1:
					accountTypeTitledPane = accountTypeBTitledPane;
					break;
				case 2:
					accountTypeTitledPane = accountTypeCTitledPane;
					break;
				case 3:
					accountTypeTitledPane = accountTypeDTitledPane;
					break;
				case 4:
					accountTypeTitledPane = accountTypeETitledPane;
					break;
			}

			accountTypeTitledPane.setText(bankAccountType.getName());
		}

		addListenerAccountTypeTitledPane(accountTypeATitledPane);
		addListenerAccountTypeTitledPane(accountTypeBTitledPane);
		addListenerAccountTypeTitledPane(accountTypeCTitledPane);
		addListenerAccountTypeTitledPane(accountTypeDTitledPane);
		addListenerAccountTypeTitledPane(accountTypeETitledPane);
	}

	private void initAccountTypeListViews()
	{
		addListenerAccountTypeListView(accountTypeATitledPane, accountTypeAListView);
		addListenerAccountTypeListView(accountTypeBTitledPane, accountTypeBListView);
		addListenerAccountTypeListView(accountTypeCTitledPane, accountTypeCListView);
		addListenerAccountTypeListView(accountTypeDTitledPane, accountTypeDListView);
		addListenerAccountTypeListView(accountTypeETitledPane, accountTypeEListView);

		addItemsAccountTypeListView(accountTypeAListView);
		addItemsAccountTypeListView(accountTypeBListView);
		addItemsAccountTypeListView(accountTypeCListView);
		addItemsAccountTypeListView(accountTypeDListView);
		addItemsAccountTypeListView(accountTypeEListView);
	}

	private void addListenerAccountTypeTitledPane(TitledPane accountTypeTitledPane)
	{
		accountTypeTitledPane.expandedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldBoolean, Boolean newBoolean)
			{
				expandRequestAccountTypeTitledPane(accountTypeTitledPane);
			}
		});
	}

	protected void expandRequestAccountTypeTitledPane(TitledPane accountTypeTitledPane)
	{
		ListView<?> listViewAccountType = (ListView<?>) accountTypeTitledPane.getContent();

		if (listViewAccountType.getItems().isEmpty())
		{
			accountTypeTitledPane.setExpanded(false);
		}
		else
		{
			accountTypeTitledPane.setExpanded(true);
		}
	}

	private void addListenerAccountTypeListView(TitledPane accountTypeTitledPane, ListView<String> accountTypeListView)
	{
		accountTypeListView.getItems().addListener(new ListChangeListener<String>()
		{
			@Override
			public void onChanged(Change<? extends String> change)
			{
				while (change.next())
				{
					if (change.wasAdded())
					{
						accountTypeTitledPane.setCollapsible(true);
						accountTypeListView.setPrefHeight(accountTypeListView.getPrefHeight() + 29);
					}
					else if (change.wasRemoved())
					{
						if (accountTypeListView.getItems().size() - 1 == 0)
						{
							accountTypeTitledPane.setCollapsible(false);
						}

						accountTypeListView.setPrefHeight(accountTypeListView.getPrefHeight() - 29);
					}
				}
			}
		});
	}

	private void addItemsAccountTypeListView(ListView<String> accountTypeListView)
	{
		// accountTypeListView.getItems().add("Testkonto 1");
	}

	private void initAccountTypeComboBox()
	{
		accountTypeComboBox.setItems(BankaccountType.getList());

		accountTypeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BankaccountType>()
		{
			@Override
			public void changed(ObservableValue<? extends BankaccountType> observable, BankaccountType oldBankaccountType, BankaccountType newBankaccountType)
			{
				if (newBankaccountType.getName().equals(accountTypeATitledPane.getText()))
				{
					expandRequestAccountTypeTitledPane(accountTypeATitledPane);
				}
				else if (newBankaccountType.getName().equals(accountTypeBTitledPane.getText()))
				{
					expandRequestAccountTypeTitledPane(accountTypeBTitledPane);
				}
				else if (newBankaccountType.getName().equals(accountTypeCTitledPane.getText()))
				{
					expandRequestAccountTypeTitledPane(accountTypeCTitledPane);
				}
				else if (newBankaccountType.getName().equals(accountTypeDTitledPane.getText()))
				{
					expandRequestAccountTypeTitledPane(accountTypeDTitledPane);
				}
				else if (newBankaccountType.getName().equals(accountTypeETitledPane.getText()))
				{
					expandRequestAccountTypeTitledPane(accountTypeETitledPane);
				}
			}
		});

		accountTypeComboBox.setConverter(new StringConverter<BankaccountType>()
		{
			@Override
			public String toString(BankaccountType object)
			{
				return object.getName();
			}

			@Override
			public BankaccountType fromString(String string)
			{
				return accountTypeComboBox.getItems().stream().filter(ap -> ap.getName().equals(string)).findFirst().orElse(null);
			}
		});

		accountTypeComboBox.setCellFactory(new Callback<ListView<BankaccountType>, ListCell<BankaccountType>>()
		{
			@Override
			public ListCell<BankaccountType> call(ListView<BankaccountType> p)
			{
				final ListCell<BankaccountType> cell = new ListCell<BankaccountType>()
				{
					@Override
					protected void updateItem(BankaccountType bankaccountType, boolean isEmpty)
					{
						super.updateItem(bankaccountType, isEmpty);

						if (bankaccountType == null || isEmpty)
						{
							setText(null);
						}
						else
						{
							setText(bankaccountType.getName());
						}
					}
				};

				return cell;
			}
		});

		accountTypeComboBox.getSelectionModel().select(0);
	}

	@FXML
	private void onActionExpandAllButton()
	{

	}

	@FXML
	private void onActionCollapseAllButton()
	{

	}

	@FXML
	private void onActionResetButton()
	{

	}

	@FXML
	private void onActionSaveButton()
	{
		try
		{
			saveChanges();

			resetButton.setDisable(true);
			saveButton.setDisable(true);
			accountNameFieldController.getInputField().end();
		}
		catch (Exception e)
		{
			new UnexpectedBehaviourException(e);
		}
	}

	private void saveChanges()
	{
		if (selectedBankaccountType != null)
		{
			new Bankaccount(accountTypeComboBox.getSelectionModel().getSelectedItem(), accountNameTextField.getText());

			selectedBankaccountType.getChildren().add(new TreeItem<String>(accountNameTextField.getText()));

			// TODO: TreeView is not being rebuilt automatically
			selectedBankaccountType.setExpanded(true);

			accountNameTextField.clear();
		}
		else if (selectedBankaccount.get() != null)
		{
			// TODO: Save changed bankaccount
		}
	}

	private void setButtonStatusLoginData()
	{
		if (!accountNameFieldController.getInputField().getText().equals(UserData.getCashJournalName()))
		{
			resetButton.setDisable(false);

			if (accountNameFieldController.getInputField().isValid())
			{
				saveButton.setDisable(false);
			}
			else
			{
				saveButton.setDisable(true);
			}
		}
		else
		{
			resetButton.setDisable(true);
			saveButton.setDisable(true);
		}
	}
}
