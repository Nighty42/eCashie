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
	
	private int id;
	private BankaccountType typeID;
	private String name;
	private String iconID;
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public BankaccountType getBankaccountType()
	{
		return typeID;
	}

	public void setBankaccountType(BankaccountType bankaccountType)
	{
		this.typeID = bankaccountType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getIcon()
	{
		return iconID;
	}

	public void setIcon(String icon)
	{
		this.iconID = icon;
	}

	public Bankaccount()
	{
	}
	
	public Bankaccount(BankaccountType bankaccountType, String name)
	{
		this.typeID = bankaccountType;
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
