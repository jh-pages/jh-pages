package org.sunix.jhpages;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.Display;

import picocli.CommandLine;

@CommandLine.Command
public class JhPagesMain implements Runnable {

    @Inject
    GitHubService gitHubService;

    @Override
    public void run() {

        Display display = buildDisplay();

        char[] spinnerChars = new char[] { '-', '/', '|',  '\\' };
        int i = 0;
        while (true) {
            i ++;
            List<String> lines = Arrays.asList(spinnerChars[i % spinnerChars.length] + " something ....");
            display.updateAnsi(lines, 0);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    private void experimentationRun() {

        // push the current folder to github
        // jh-page ./folder/ username repo

        gitHubService.withFullRepoName("sunix/mynewwebsite") //
                .init();

        if (gitHubService.checkRepoExist()) {
            System.out.println("repo " + gitHubService.getFullRepoName() + " does exist");
        } else {
            System.out.println("repo " + gitHubService.getFullRepoName() + " does NOT exist ... creating the repo !!!!");
            gitHubService.createRepo();

        }

        if (gitHubService.checkGhPagesBranchExist()) {
            System.out.println("branch gh-pages for repo " + gitHubService.getFullRepoName() + " does exist");
            gitHubService.copyContentAndPush();
        } else {
            System.out.println("branch gh-pages for repo " + gitHubService.getFullRepoName() + " does NOT exist");
            gitHubService.createBranch("gh-pages");
        }

        
         
        // strategies:
        // - use jgit

        // Flow:
        // - check if repo exist, if not create it -> need github lib
        // - check if gh-pages activated in repo and if not ... create it.
        // - generate the git url to clone
        // - check if gh-pages branch exist
        // - clone the project with only gh-pages
        // - remove content
        // - copy content
        // - add, commit push

    }

    private boolean ghPagesBranchExistsGeneric(String repo) {
        try {
            Collection<Ref> refs = Git.lsRemoteRepository() //
                    .setRemote(repo) //
                    .call();

            for (Ref ref : refs) {
                if ("refs/heads/gh-pages".equals(ref.getName())) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
