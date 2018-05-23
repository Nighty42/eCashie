package ecashie.model.database;

import ecashie.main.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BankaccountType
{
	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	private final static ObservableList<BankaccountType> accountTypeList = FXCollections.observableArrayList(
			new BankaccountType(MainApp.ResourceBundle.getString("checkAccount")),
			new BankaccountType(MainApp.ResourceBundle.getString("loanAccount")),
			new BankaccountType(MainApp.ResourceBundle.getString("savingAccount")),
			new BankaccountType(MainApp.ResourceBundle.getString("depotAccount")),
			new BankaccountType(MainApp.ResourceBundle.getString("creditCardAccount")));

	public static ObservableList<BankaccountType> getList()
	{
		return accountTypeList;
	}

	public BankaccountType(String name)
	{
		this.name = name;
	}

	public static BankaccountType get(String bankaccountTypeName)
	{
		for (BankaccountType bankaccountType : accountTypeList)
		{
			if (bankaccountType.getName().equals(bankaccountTypeName))
			{
				return bankaccountType;
			}
		}

		return null;
	}
}
