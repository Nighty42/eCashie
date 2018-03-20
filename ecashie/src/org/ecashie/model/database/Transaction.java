package org.ecashie.model.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ecashie.controller.database.DatabaseAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Transaction
{
	private static ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

	public static ObservableList<Transaction> getList()
	{
		return transactionList;
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

	private String description;

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	private String typeID;

	public String getTypeID()
	{
		return typeID;
	}

	public void setTypeID(String typeID)
	{
		this.typeID = typeID;
	}

	private int payerID;

	public int getPayerID()
	{
		return payerID;
	}

	public void setPayerID(int payerID)
	{
		this.payerID = payerID;
	}

	private int payeeID;

	public int getPayeeID()
	{
		return payeeID;
	}

	public void setPayeeID(int payeeID)
	{
		this.payeeID = payeeID;
	}

	public Transaction(int identifier, String description, String typeID, int payerID, int payeeID)
	{
		this.identifier = identifier;
		this.description = description;
		this.typeID = typeID;
		this.payerID = payerID;
		this.payeeID = payeeID;
	}

	public ObservableList<TransactionCategory> getTransactionCategoryList() throws SQLException
	{
		ObservableList<TransactionCategory> transactionCategoryList = FXCollections.observableArrayList();

		String sqlStatement = "SELECT * FROM transaction_category WHERE transaction_id = '" + identifier + "';";

		ResultSet resultTransactionCategories = DatabaseAccess.Statement.executeQuery(sqlStatement);

		while (resultTransactionCategories.next())
		{
			int identifier = resultTransactionCategories.getInt("identifier");
			int transaction_id = resultTransactionCategories.getInt("transaction_id");
			int category_id = resultTransactionCategories.getInt("category_id");
			double amount = resultTransactionCategories.getDouble("amount");

			TransactionCategory transactionCategory = new TransactionCategory(identifier, transaction_id, category_id,
					amount);

			transactionCategoryList.add(transactionCategory);
		}

		return transactionCategoryList;
	}

	public double getTotalAmount() throws SQLException
	{
		double totalAmount = 0;

		ObservableList<TransactionCategory> transactionCategoryList = getTransactionCategoryList();

		for (TransactionCategory transactionCategory : transactionCategoryList)
		{
			totalAmount += transactionCategory.getAmount();
		}

		return totalAmount;
	}
}
