package org.sunix.jhpages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

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
        String githubPagesProjectRef = "sunix/mywebsite-createrepo";
        cleanUpRepoForTest(githubPagesProjectRef);
        createRepoForTest(githubPagesProjectRef);
        assertTrue(gitHubService.checkRepoExist());
    }

    private void assertFalseWith5Retries(Supplier<Boolean> supplier, String errorMessage) {
        for (int i = 0; i < 5; i++) {
            if (supplier.get()) {
                try {
                    Thread.sleep(1000);
                } finally {
                    continue;
                }
            }
            break;
        }
        assertFalse(supplier.get());
    }

    @Test
    void should_createGhpagesBranch() {
        String githubPagesProjectRef = "sunix/mywebsite-createbranch";
        cleanUpRepoForTest(githubPagesProjectRef);
        createRepoForTest(githubPagesProjectRef);

        // making sure there is not gh-pages
        assertFalse(gitHubService.checkRemoteGhPagesBranchExist(),
                "after repo creation, gh-pages branch should not be created");

        // create the gh-pages branch
        gitHubService.checkoutLocalGhPagesBranch();

        assertTrue(gitHubService.checkLocalGhPagesBranchExist(),
                "after checkout, gh-pages branch should have been created");
    }

    @Test
    void shouldOverrideContent() {
        // having that
        assertEqualsRetry1min(200, () -> {
            return get("https://sunix.github.io/mywebsite-push/index.html");
        }, "should be pushed and return code is");

        // gitHubService.checkout
    }

    @Test
    void shouldPushContent() throws IOException {
        // use the folder test/resources/mywebsite/ and push it
        // initialise the repo and clone/create gh-branch it if needed in a tmp folder
        String githubPagesProjectRef = "sunix/mywebsite-push";
        cleanUpRepoForTest(githubPagesProjectRef);
        createRepoForTest(githubPagesProjectRef);
        assertFalse(gitHubService.checkRemoteGhPagesBranchExist(),
                "after repo creation, gh-pages branch should not be created");
        gitHubService.checkoutLocalGhPagesBranch();

        // remove all the existing content but .git from the temp folder
        // copy the content of test/resources/mywebsite/ to the temp folder
        // add/commit/push
        String resourceName = "mywebsite";
        ClassLoader classLoader = getClass().getClassLoader();
        File folder = new File(classLoader.getResource(resourceName).getFile());
        gitHubService.copyContentAndPush(folder);

        // verify that the content is available in gh-pages
        // check that https://sunix.github.io/mywebsite-push/index.html is published

        assertEqualsRetry1min(200, () -> {
            return get("https://sunix.github.io/mywebsite-push/index.html");
        }, "should be pushed and return code is");
    }

    private Integer get(String urlstr) {
        try {
            URL url = new URL(urlstr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            return con.getResponseCode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void assertEqualsRetry1min(int expectedValue, Supplier<Integer> supplier, String errorMessage) {
        for (int i = 0; i < 60; i++) {
            if (!supplier.get().equals(expectedValue)) {
                try {
                    Thread.sleep(1000);
                } finally {
                    continue;
                }
            }
            break;
        }
        assertEquals(expectedValue, supplier.get(), "should be pushed and return code is");
    }

    private void createRepoForTest(String githubPagesProjectRef) {
        assertFalseWith5Retries(() -> gitHubService.checkRepoExist(),
                "The repo " + githubPagesProjectRef + " should not exist in the test context");
        gitHubService.createRepo();
    }

    private void cleanUpRepoForTest(String githubPagesProjectRef) {

        gitHubService.withFullRepoName(githubPagesProjectRef) //
                .init();
        // remove the repo first before starting the test
        if (gitHubService.checkRepoExist()) {
            gitHubService.deleteRepo();
        }
    }

}