package ecashie.controller.database;

import ecashie.controller.exception.DatabaseBuildException;

public class DatabaseBuilder
{
	public static void createTables()
	{
		try
		{
			createExternalParticipantTable();

			createBankaccountTable();

			createCategoryTable();

			createTransactionTable();
			
			createTransactionCategoryTable();
		}
		catch (Exception e)
		{
			new DatabaseBuildException(e);
		}
	}

	private static void createExternalParticipantTable() throws Exception
	{
		String sqlStatement = "CREATE TABLE external_participant (identifier INT IDENTITY PRIMARY KEY NOT NULL, name VARCHAR(50) NOT NULL);";

		DatabaseAccess.Statement.execute(sqlStatement);
	}

	private static void createBankaccountTable() throws Exception
	{
		String sqlStatement = "CREATE TABLE bankaccount (identifier INT IDENTITY PRIMARY KEY NOT NULL, name VARCHAR(50) NOT NULL UNIQUE, description VARCHAR(250));";

		DatabaseAccess.Statement.execute(sqlStatement);
	}

	private static void createCategoryTable() throws Exception
	{
		String sqlStatement = "CREATE TABLE category (identifier INT IDENTITY PRIMARY KEY NOT NULL, name VARCHAR(50) NOT NULL UNIQUE, description VARCHAR(250));";

		DatabaseAccess.Statement.execute(sqlStatement);
	}

	private static void createTransactionTable() throws Exception
	{
		String sqlStatement = "CREATE TABLE transaction (identifier INT IDENTITY PRIMARY KEY NOT NULL, description VARCHAR(250), type_id VARCHAR(1) NOT NULL, payer_id INT NOT NULL, payer_type VARCHAR(1) NOT NULL, payee_id INT NOT NULL, payee_type VARCHAR(1) NOT NULL);";

		DatabaseAccess.Statement.execute(sqlStatement);
	}

	private static void createTransactionCategoryTable() throws Exception
	{
		String sqlStatement = "CREATE TABLE transaction_category (identifier INT IDENTITY PRIMARY KEY NOT NULL, transaction_id INT NOT NULL, category_id INT NOT NULL, amount DECIMAL(20,2) NOT NULL);";

		DatabaseAccess.Statement.execute(sqlStatement);
		
		sqlStatement = "ALTER TABLE transaction_category ADD FOREIGN KEY(transaction_id) REFERENCES transaction(identifier);";

		DatabaseAccess.Statement.execute(sqlStatement);

		sqlStatement = "ALTER TABLE transaction_category ADD FOREIGN KEY(category_id) REFERENCES category(identifier);";

		DatabaseAccess.Statement.execute(sqlStatement);
	}
}
