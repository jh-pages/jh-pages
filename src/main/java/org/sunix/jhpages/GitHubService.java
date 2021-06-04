package org.sunix.jhpages;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.okhttp3.OkHttpConnector;

import okhttp3.OkHttpClient;

@ApplicationScoped
public class GitHubService {

    private String repoName;
    private GitHub gitHub;
    private String repoOrgName;
    private File cloneDirectory = new File("/projects/jh-pages/target/test");

    public void init() {
        try {
            this.gitHub = GitHubBuilder //
                    .fromEnvironment() //
                    .withConnector(new OkHttpConnector(new OkHttpClient())) //
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkRepoExist() {
        try {
            return gitHub.getRepository(getFullRepoName()) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public String getRepoName() {
        return repoName;
    }

    public boolean checkGhPagesBranchExist() {
        try {
            return gitHub.getRepository(repoName).getBranch("gh-pages") != null;
        } catch (Exception e) {
            return false;
        }
    }

    public GitHubService withFullRepoName(String fullRepoName) {
        String[] tokens = fullRepoName.split("/");
        this.repoOrgName = tokens[0];
        this.repoName = tokens[1];
        return this;
    }

    public void createRepo() {
        try {
            // TODO figure out why this is not working ....
            // GHOrganization orgmy =
            // gitHub.getUserPublicOrganizations("sunix").get("sunix");
            // orgmy.createRepository(this.repoName).create();

            gitHub.createRepository(this.repoName).create();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getFullRepoName() {
        return this.repoOrgName + "/" + this.repoName;
    }

    
    public void createBranch(String branchName) {



        try {
            Git git = Git.cloneRepository() //
                    .setURI("https://github.com/"+ getFullRepoName()) //
                    .setDirectory(cloneDirectory) //
                    .call();

 
           //  git.branchCreate().setName(branchName).call();
           //  git.checkout()

        } catch (Exception e) {
            throw new RuntimeException("An error occured while trying cloning the repo " + branchName, e);
        }
	}

}
