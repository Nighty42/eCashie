package ecashie.controller.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;

public class ApplicationLogger
{
	private static final Logger logger = LogManager.getLogger(ApplicationLogger.class);
	private static List<String> messageList = new ArrayList<String>();

	public static void logException(Exception e)
	{
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		e.printStackTrace(printWriter);
		String exceptionAsString = stringWriter.toString();

		logger.error(exceptionAsString);

		try
		{
			printWriter.flush();
			printWriter.close();

			stringWriter.flush();
			stringWriter.close();
		}
		catch (Exception e1)
		{
		}
	}

	public static void addMessage(LogEvent logEvent)
	{
		messageList.add(logEvent.getMessage().getFormattedMessage());
	}

	public static List<String> getMessages()
	{
		return messageList;
	}
}
