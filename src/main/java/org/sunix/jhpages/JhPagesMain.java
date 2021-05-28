package org.sunix.jhpages;

import java.io.IOException;
import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class JhPagesMain implements QuarkusApplication {

    @Inject
    GitHubService gitHubService;

    @Override
    public int run(String... args) throws InvalidRemoteException, TransportException, GitAPIException, IOException {

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
        } else {
            System.out.println("branch gh-pages for repo " + gitHubService.getFullRepoName() + " does NOT exist");
        }

        /**
         * Git gittt = Git.cloneRepository() //
         * .setURI("https://github.com/jh-pages/jh-pages") // .setDirectory(new
         * File("/projects/jh-pages/target/test")) // .call();
         */
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

        return 0;
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
