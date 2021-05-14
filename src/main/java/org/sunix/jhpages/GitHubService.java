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

    public void init(String repoName) {
        this.repoName = repoName;
        try {
            gitHub = GitHubBuilder //
                    .fromEnvironment() //
                    .withConnector(new OkHttpConnector(new OkHttpClient())).build();
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

}
