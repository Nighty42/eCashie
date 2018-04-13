package ecashie.controller.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class GeneralOperations
{
	public static String currentYear()
	{
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	}

	public static String getStackTraceAsString(Throwable throwable)
	{
		StringWriter stringWriter = new StringWriter();

		if (throwable != null)
		{
			throwable.printStackTrace(new PrintWriter(stringWriter));
		}

		return stringWriter.toString();
	}

	public static void copyToClipboard(String content)
	{
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent clipboardContent = new ClipboardContent();

		clipboardContent.putString(content);
		clipboard.setContent(clipboardContent);
	}

	// TODO: Implement update routine
	public static void updateApp()
	{
		try
		{
			GitHub github = GitHub.connectUsingOAuth("ae3ef49abb0da47e0b2474c4813c4122b46dcdd1");
			GHRepository ghRepository = github.getRepository("Nighty42/eCashie_java");
			GHRelease ghRelease = ghRepository.getLatestRelease();
			System.out.println(ghRelease.getUpdatedAt());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
