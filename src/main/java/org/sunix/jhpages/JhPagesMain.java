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

        new Step("Create gh-pages branch if needed", (StepDisplay display) -> {
            if (gitHubService.checkRemoteGhPagesBranchExist()) {
                display.updateText("Branch gh-pages for repo " + gitHubService.getFullRepoName() + " already exist");

                gitHubService.checkoutLocalGhPagesBranch();


                gitHubService.copyContentAndPush(folder);

                return;
            }


            gitHubService.checkoutLocalGhPagesBranch();


            display.updateText("Branch gh-pages for repo " + gitHubService.getFullRepoName() + " does NOT exist");
            gitHubService.createBranch("gh-pages", folder);

            display.done("Created branch gh-pages ! " + folder);// folder.getPath());
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
