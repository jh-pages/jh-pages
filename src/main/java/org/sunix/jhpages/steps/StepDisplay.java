package org.sunix.jhpages.steps;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.fusesource.jansi.Ansi;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.Display;

public class StepDisplay {

    final char[] spinnerChars = new char[] { '⣾', '⣽', '⣻', '⢿', '⡿', '⣟', '⣯', '⣷' };
    int spinnerInt = 0;
    String text;
    boolean done = false;
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public StepDisplay(String initialText) {
        text = initialText;

        final Display display = buildDisplay();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<String> lines = Arrays.asList(
                        Ansi.ansi().fgBlue().a(spinnerChars[spinnerInt++ % spinnerChars.length]).reset() + " "+ text);
                display.updateAnsi(lines, 0);
            }
        },0L, 200L, TimeUnit.MILLISECONDS);
    }

    public void updateText(String text) {
        this.text = text;
    }

    public void done(String text) {
        scheduledExecutorService.shutdown();
        System.out.println(Ansi.ansi().fgGreen().a("✔️").reset() + " " + text);
    }

    protected Display buildDisplay() {
        try {
            Terminal terminal = TerminalBuilder.builder().dumb(true).build();

            Display display = new Display(terminal, false);
            Size size = terminal.getSize();
            display.resize(size.getRows(), size.getColumns());
            return display;
        } catch (IOException e) {
            throw new RuntimeException("error while building display", e);
        }
    }

	public boolean isDone() {
		return done;
	}

}
