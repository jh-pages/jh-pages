package org.sunix.jhpages;

import org.sunix.jhpages.steps.Step;
import org.sunix.jhpages.steps.StepDisplay;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.fusesource.jansi.Ansi;
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

        // ⣽ checking if repo `blabla` exists
        // ✔️ repo `blabla` exists
        // if it doesn't exists
        // ✔️ repo `blabla` doesn't exist
        // ⣽ creating repo `blabla`
        // ✔️ repo `blabla` created
        // startTask(new CheckIfRepoExistsTask(repourl))

        new Step("Create the github repo if needed", (StepDisplay display) -> {
            display.updateText("doing something");
            int i = 0;
            while (i < 100) {
                display.updateText("Creating Github repo ... " + i++ + "%");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            display.done("Done creating the github repo !");
        }).execute();

        new Step("Doing something else ...", (StepDisplay display) -> {
            int i = 0;
            while (i < 100) {
                display.updateText("doing something else ..." + i++  + "%");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            display.done("Done doing something else !");
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

    private void experimentationRun2() {

        Display display = buildDisplay();

        char[] spinnerChars = new char[] { '⣾', '⣽', '⣻', '⢿', '⡿', '⣟', '⣯', '⣷' };
        int i = 0;
        while (i < 50) {
            i++;
            List<String> lines = Arrays
                    .asList(Ansi.ansi().fgBlue().a(spinnerChars[i % spinnerChars.length]).reset() + " something ....");
            display.updateAnsi(lines, 0);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Ansi.ansi().fgGreen().a("✔️").reset() + " something 1 done !");

        i = 0;
        while (i < 50) {
            i++;
            List<String> lines = Arrays.asList(
                    Ansi.ansi().fgBlue().a(spinnerChars[i % spinnerChars.length]).reset() + " something 2 ....");
            display.updateAnsi(lines, 0);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Ansi.ansi().fgGreen().a("✔️").reset() + " something 2 done !");

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
            System.out
                    .println("repo " + gitHubService.getFullRepoName() + " does NOT exist ... creating the repo !!!!");
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
