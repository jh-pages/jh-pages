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
        StepDisplay stepDisplay = new StepDisplay(initialText);
        stepRunner.accept(stepDisplay);
        if (!stepDisplay.isDone()) {
            stepDisplay.done("Done !");
        }
    }

}