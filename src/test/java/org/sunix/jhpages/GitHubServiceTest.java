package org.sunix.jhpages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.sunix.jhpages.steps.Step;
import org.sunix.jhpages.steps.StepDisplay;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GitHubServiceTest {

    @Inject
    GitHubService gitHubService;

    @Test
    void should_return_repoName_without_username() {
        String githubPagesProjectRef = "sunix/mywebsite";
        gitHubService.withFullRepoName(githubPagesProjectRef);
        assertEquals(gitHubService.getRepoName(), "mywebsite",
                "the reponame should be the last part of the full repo name");
    }

    @Test
    void should_createGHRepo() {
        String githubPagesProjectRef = "sunix/mywebsite2";
        gitHubService.withFullRepoName(githubPagesProjectRef) //
                .init();
        if (gitHubService.checkRepoExist()) {
            throw new Error("the repo " + githubPagesProjectRef + " should not exist in the test context");
        }

        gitHubService.createRepo();
        assertTrue(gitHubService.checkRepoExist());

    }

}