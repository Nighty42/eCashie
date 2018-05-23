package ecashie.model.database;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Bankaccount
{
	private static ObservableList<Bankaccount> bankaccountList = FXCollections.observableArrayList();

	public static ObservableList<Bankaccount> getList()
	{
		return bankaccountList;
	}
	
	private BankaccountType bankaccountType;

	public BankaccountType getBankaccountType()
	{
		return bankaccountType;
	}

	public void setBankaccountType(BankaccountType bankaccountType)
	{
		this.bankaccountType = bankaccountType;
	}

	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	private String icon;

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public Bankaccount(BankaccountType bankaccountType, String name)
	{
		this.bankaccountType = bankaccountType;
		this.name = name;
		
		bankaccountList.add(this);
	}

	public static ObjectProperty<Bankaccount> get(String bankaccountName)
	{
		for (Bankaccount bankaccount : bankaccountList)
		{
			if (bankaccount.getName().equals(bankaccountName))
			{
				return new SimpleObjectProperty<Bankaccount>(bankaccount);
			}
		}

		return null;
	}
}
