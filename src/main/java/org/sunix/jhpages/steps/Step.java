package org.sunix.jhpages.steps;

import java.util.function.Consumer;

/**
 * Step
 */
public class Step {

    private String initialText;
    private Consumer<StepDisplay> stepRunner;

    public Step(String initialText, Consumer<StepDisplay> stepRunner) {
        this.initialText = initialText;
        this.stepRunner = stepRunner;

    }

	public void execute() {
        stepRunner.accept(new StepDisplay(initialText));
	}

}