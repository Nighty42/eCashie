package org.ecashie.model.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Bankaccount
{
	private static ObservableList<Bankaccount> bankaccountList = FXCollections.observableArrayList();

	public static ObservableList<Bankaccount> getList()
	{
		return bankaccountList;
	}

	private int identifier;

	public int getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(int identifier)
	{
		this.identifier = identifier;
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

	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	private String description;

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Bankaccount(int identifier, String name, String description)
	{
		this.identifier = identifier;
		this.name = name;
		this.description = description;
	}
}
