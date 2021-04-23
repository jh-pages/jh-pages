package org.sunix.jhpages;

import java.io.File;
import java.util.Arrays;

import javax.inject.Inject;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class GreetingMain implements QuarkusApplication {

    @Inject
    GreetingService service;

    @Override
    public int run(String... args) throws InvalidRemoteException, TransportException, GitAPIException {
        // push the current folder to github
        // jh-page ./folder/ username repo
        Git.cloneRepository() //
                .setURI("https://github.com/jh-pages/jh-pages") //
                .setBranchesToClone(Arrays.asList("refs/heads/gh-pages"))
                .setBranch("refs/heads/gh-pages")
                .setDirectory(new File("/projects/jh-pages/target/test")) //
                .call();
        // strategies:
        // - use jgit
        // - call git installed in my system

        // Flow:
        // - check if repo exist, if not create it -> need github lib
        // - check if gh-pages activated in repo and if not ... create it.
        // - generate the git url to clone
        // - check if gh-pages branch exist
        // - clone the project with only gh-pages (sparse checkout?)
        // - remove content
        // - copy content
        // - add, commit push

        return 0;
    }

}
