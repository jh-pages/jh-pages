package org.sunix.jhpages;

import javax.enterprise.context.ApplicationScoped;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.okhttp3.OkHttpConnector;

import okhttp3.OkHttpClient;

@ApplicationScoped
public class GitHubService {

    private String repoName;
    private GitHub gitHub;
    private String ghPassword;
    private String ghUser;

    public void init() {
        try {
            GitHubBuilder gitHubBuilder = GitHubBuilder //
                    .fromEnvironment() //
                    .withConnector(new OkHttpConnector(new OkHttpClient()));
            if (ghUser != null) {
                gitHubBuilder.withPassword(ghUser, ghPassword);
            }
            gitHub = gitHubBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkRepoExist() {
        try {
            return gitHub.getRepository(repoName) != null;
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

    public GitHubService withRepoName(String repoName) {
        this.repoName = repoName;
        return this;
    }

    public GitHubService withGhPassword(String ghPassword) {
        this.ghPassword = ghPassword;
        return this;
    }

    public GitHubService withGhUser(String ghUser) {
        this.ghUser = ghUser;
        return this;
    }

    public void createRepo() {
        try {
            // TODO split with (in TDD) String[] tokens = name.split("/");
            gitHub.createRepository(repoName).create();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
