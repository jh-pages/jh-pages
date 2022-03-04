package org.sunix.jhpages;

import java.io.File;

import javax.inject.Inject;

import org.sunix.jhpages.steps.Step;
import org.sunix.jhpages.steps.StepDisplay;

import picocli.CommandLine;
import picocli.CommandLine.Parameters;

@CommandLine.Command
public class JhPagesMain implements Runnable {

    @Parameters(index = "0", description = "Something like sunix/myrepo")
    private String githubPagesProjectRef;

    @Parameters(index = "1", description = "The local folder to push to gh-pages")
    private File folder;

    @Inject
    GitHubService gitHubService;

    @Override
    public void run() {

        new Step("Create the github repo if needed", (StepDisplay display) -> {
            gitHubService.withFullRepoName(githubPagesProjectRef) //
                    .init();
            if (gitHubService.checkRepoExist()) {
                display.done("Repo " + gitHubService.getFullRepoName() + " (" + gitHubService.getRepoURL()
                        + ") already exist. Skipping creation.");
                return;
            }
            display.updateText(
                    "Repo " + gitHubService.getFullRepoName() + " does NOT exist ... creating the repo !!!!");
            gitHubService.createRepo();
            display.done("Repo " + gitHubService.getFullRepoName() + " (" + gitHubService.getRepoURL() + ") created");

        }).execute();

        new Step("Checkout gh-pages branch", (StepDisplay display) -> {
            gitHubService.checkoutLocalGhPagesBranch();
            display.done("Branch gh-pages checked out " + folder);// folder.getPath());
        }).execute();

        new Step("Push gh-pages branch", (StepDisplay display) -> {
            gitHubService.copyContentAndPush(folder);
            display.done("Pushed content from " + folder);// folder.getPath());
        }).execute();

        new Step("Display url", (StepDisplay display) -> {
            String ghPagesUrl = gitHubService.getGhPagesUrl();
            display.done("The site is available at the URL: " + ghPagesUrl);// folder.getPath());
        }).execute();

        // - ✔️ generate the git url to clone
        // - ✔️ check if gh-pages branch exist - create a branch if not
        // - ✔️ clone the project with only gh-pages
        // - clone the project
        // - if gh-pages branch doesn't exist -> give a try with git CLI to see what we
        // could do when the repo empty.
        // - ✔️ remove content
        // - ✔️ copy content
        // - ✔️ add, commit push

    }

}
