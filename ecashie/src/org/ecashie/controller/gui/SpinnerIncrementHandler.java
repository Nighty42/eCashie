package org.ecashie.controller.gui;

import javafx.animation.AnimationTimer;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class SpinnerIncrementHandler implements EventHandler<MouseEvent>
{
	private static final PseudoClass PRESSED = PseudoClass.getPseudoClass("pressed");

	private Spinner<?> spinner;
	private boolean increment;
	private long startTimestamp;

	private static final long initialDelay = 1000L * 1000L * 750L;
	private int currentFrame = 0;
	private int previousFrame = 0;
	private Node button;

	private final AnimationTimer timer = new AnimationTimer()
	{
		@Override
		public void handle(long now)
		{
			if (now - startTimestamp >= initialDelay)
			{
				// Single or holded mouse click
				if (currentFrame == previousFrame || currentFrame % 10 == 0)
				{
					if (increment)
					{
						spinner.increment();
					}
					else
					{
						spinner.decrement();
					}
				}
			}

			++currentFrame;
		}
	};

	@Override
	public void handle(MouseEvent event)
	{
		if (event.getButton() == MouseButton.PRIMARY)
		{
			Spinner<?> source = (Spinner<?>) event.getSource();
			Node node = event.getPickResult().getIntersectedNode();

			Boolean increment = null;

			while (increment == null && node != source)
			{
				if (node.getStyleClass().contains("increment-arrow-button"))
				{
					increment = Boolean.TRUE;
				}
				else if (node.getStyleClass().contains("decrement-arrow-button"))
				{
					increment = Boolean.FALSE;
				}
				else
				{
					node = node.getParent();
				}
			}

			if (increment != null)
			{
				event.consume();
				source.requestFocus();
				spinner = source;
				this.increment = increment;

				startTimestamp = System.nanoTime();

				button = node;

				node.pseudoClassStateChanged(PRESSED, true);

				timer.handle(startTimestamp + initialDelay);

				timer.start();
			}
		}
	}

	public void stop()
	{
		if (button != null)
		{
			previousFrame = currentFrame;

			timer.stop();
			button.pseudoClassStateChanged(PRESSED, false);
			button = null;
			spinner = null;
		}
	}
}
