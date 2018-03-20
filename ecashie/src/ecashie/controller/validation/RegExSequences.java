package ecashie.controller.validation;

public class RegExSequences
{
	public static String getByID(String identifier)
	{
		String sequence = "";

		switch (identifier)
		{
			case "IP_OR_HOST":
				sequence = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$|^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";
				break;
			case "ONLY_NUMBERS":
				sequence = "^[0-9]+$";
				break;
			case "PASSWORD":
				sequence = "(?=^.*[0-9].*[0-9].*$)(?=^.*[a-z].*[a-z].*$)(?=^.*[A-Z].*[A-Z].*$)^.*[^a-zA-Z0-9].*[^a-zA-Z0-9].*$";
				break;
			case "ECDB_FILE":
				sequence = "^.+(\\.ecdb)$";
				break;
			case "ONLY_CHARACTERS":
				sequence = "^[a-zA-Z]+$";
			case "CONTAINS_CHARACTERS":
				sequence = "[a-zA-Z]+";
		}

		return sequence;
	}
}
