package ecashie.model.database;

import java.sql.ResultSet;

import ecashie.controller.database.DatabaseAccess;

public class PayeePayer
{
	public static ResultSet getPayeePayerByID(int identifier, String payerTypeToken) throws Exception
	{
		ResultSet resultSet = null;
		String tableName = "";

		switch (payerTypeToken)
		{
			case "B":
				tableName = "bankaccount";
				break;
			case "E":
				tableName = "external_participant";
				break;
		}

		if (!tableName.isEmpty())
		{
			String sqlStatement = "SELECT * FROM " + tableName + " WHERE identifier = '" + identifier + "';";

			resultSet = DatabaseAccess.Statement.executeQuery(sqlStatement);
		}

		return resultSet;
	}

	public static String getName(ResultSet resultSet) throws Exception
	{
		String name = "";

		while (resultSet.next())
		{
			name = resultSet.getString("name");
		}

		return name;
	}
}
