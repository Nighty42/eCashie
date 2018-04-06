package ecashie.controller.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import ecashie.controller.utilities.GeneralOperations;

public class HtmlLogFormatter extends Formatter
{
	private int rowCount = 0;
	private boolean hasNewEntries = false;

	@Override
	public String getHead(Handler h)
	{
		String htmlHead = "<!DOCTYPE html>\n<head>\n<style type=\"text/css\">\n"
				+ "table { width: 100%; border-collapse: collapse; }\n"
				+ "th { font:bold 10pt Tahoma; border: 1px solid darkgrey; }\n"
				+ "td { border: 1px solid darkgrey; font:normal 10pt Tahoma; vertical-align: top; }\n"
				+ ".row_0 { background-color: #FFFFFF; }\n" + ".severe { color: red; }\n"
				+ ".warning { color: orange; }\n" + ".info { color: blue; }\n"
				+ ".row_1 { background-color: #E1E8F1; }\n" + "h1 {font:normal 11pt Tahoma;}\n" + "</style>\n"
				+ "</head>\n";

		String htmlBody = "<body>\n";
		String oldLogFileContent = ApplicationLogger.oldLogFileContent;
		String contentSeparation = "";
		String contentHeader = "";
		String tableHead = "";

		if (hasNewEntries)
		{
			if (!oldLogFileContent.isEmpty())
			{
				contentSeparation = "<p><hr></p>";
			}

			SimpleDateFormat date_format = new SimpleDateFormat("dd. MMMM yyyy");
			contentHeader = "<h1><b>" + (date_format.format(new Date())) + "</b></h1>\n";

			tableHead = "<table cellpadding=\"5\" cellspacing=\"3\">\n" + "<tr align=\"left\">\n"
					+ "<th style=\"width:1%; white-space:nowrap; text-align: center;\">L</th>\n"
					+ "<th style=\"width:1%; white-space:nowrap;\">Exception</th>\n"
					+ "<th style=\"width:1%; white-space:nowrap; text-align: center;\">Date</th>\n"
					+ "<th style=\"width:1%; white-space:nowrap; text-align: center;\">Time</th>\n"
					+ "<th style=\"width:1%; white-space:nowrap; text-align: center;\">Message Key</th>\n"
					+ "<th style=\"width:50%; text-align:left;\">Log Message</th>\n" + "</tr>\n";
		}

		return htmlHead.concat(htmlBody).concat(oldLogFileContent).concat(contentSeparation).concat(contentHeader)
				.concat(tableHead);
	}

	@Override
	public String format(LogRecord logRecord)
	{
		if (!hasNewEntries)
		{
			hasNewEntries = true;
		}

		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append("<tr class=\"row_" + ((++rowCount) % 2) + "\">\n");
		stringBuffer.append("<td style=\"text-align:center;\" ");

		if (logRecord.getLevel().intValue() > Level.WARNING.intValue())
		{
			stringBuffer.append("class=\"severe\">");
		}
		else if (logRecord.getLevel() == Level.WARNING)
		{
			stringBuffer.append("class=\"warning\">");
		}
		else if (logRecord.getLevel() == Level.INFO)
		{
			stringBuffer.append("class=\"info\">");
		}

		stringBuffer.append("<b>");
		stringBuffer.append(logRecord.getLevel().toString().substring(0, 1));
		stringBuffer.append("</b>");

		stringBuffer.append("</td>\n");

		String exceptionName = "---";
		String stackTrace = "---";

		if (logRecord.getThrown() != null)
		{
			exceptionName = logRecord.getThrown().getClass().getSimpleName();
			stackTrace = GeneralOperations.getStackTraceAsString(logRecord.getThrown());
		}

		stringBuffer.append("<td>");
		stringBuffer.append(exceptionName);
		stringBuffer.append("</td>\n");

		stringBuffer.append("<td>");
		stringBuffer.append(calcDate(logRecord.getMillis()));
		stringBuffer.append("</td>\n");

		stringBuffer.append("<td>");
		stringBuffer.append(calcTime(logRecord.getMillis()));
		stringBuffer.append("</td>\n");

		String messageKey = extractMessageKey(logRecord);

		stringBuffer.append("<td>");
		stringBuffer.append(messageKey);
		stringBuffer.append("</td>\n");

		stringBuffer.append("<td>");
		stringBuffer.append(stackTrace);
		stringBuffer.append("</td>\n");

		stringBuffer.append("</tr>\n");

		stringBuffer.append("</table>\n");

		return stringBuffer.toString();
	}

	private String extractMessageKey(LogRecord logRecord)
	{
		String messageKey = "---";

		if (logRecord.getMessage() != null)
		{
			int endIndex = logRecord.getMessage().indexOf("]");

			if (endIndex > -1)
			{
				messageKey = logRecord.getMessage().substring(1, endIndex);
			}
		}

		return messageKey;
	}

	private String calcDate(long millisecs)
	{
		SimpleDateFormat date_format = new SimpleDateFormat("dd.MM.yyyy");
		Date resultdate = new Date(millisecs);

		return date_format.format(resultdate);
	}

	private String calcTime(long millisecs)
	{
		SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss.S");
		Date resultdate = new Date(millisecs);

		return date_format.format(resultdate);
	}

	@Override
	public String getTail(Handler h)
	{
		return "</body>\n</html>";
	}
}
