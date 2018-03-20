package ecashie.model.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import ecashie.controller.database.DatabaseAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransactionCategory
{
	private static ObservableList<TransactionCategory> transactionCategoryList = FXCollections.observableArrayList();

	public static ObservableList<TransactionCategory> getList()
	{
		return transactionCategoryList;
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

	private int transactionID;

	public int getTransactionID()
	{
		return transactionID;
	}

	public void setTransactionID(int transactionID)
	{
		this.transactionID = transactionID;
	}

	private int categoryID;

	public int getCategoryID()
	{
		return categoryID;
	}

	public void setCategoryID(int categoryID)
	{
		this.categoryID = categoryID;
	}

	private double amount;

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public TransactionCategory(int identifier, int transactionID, int categoryID, double amount)
	{
		this.identifier = identifier;
		this.transactionID = transactionID;
		this.categoryID = categoryID;
		this.amount = amount;
	}

	public Category getCategory() throws SQLException
	{
		Category category = null;

		String sqlStatement = "SELECT * FROM category WHERE identifier = '" + categoryID + "';";

		ResultSet resultCategory = DatabaseAccess.Statement.executeQuery(sqlStatement);

		resultCategory.next();

		identifier = resultCategory.getInt("identifier");
		String name = resultCategory.getString("name");
		String description = resultCategory.getString("description");

		category = new Category(identifier, name, description);

		return category;
	}
}
