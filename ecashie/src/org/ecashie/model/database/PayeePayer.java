package org.ecashie.model.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ecashie.controller.database.DatabaseAccess;

public class PayeePayer
{
	public static ResultSet getPayeePayerByID(int identifier, String payerTypeToken) throws SQLException
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

	public static String getName(ResultSet resultSet) throws SQLException
	{
		String name = "";

		while (resultSet.next())
		{
			name = resultSet.getString("name");
		}

		return name;
	}
}
