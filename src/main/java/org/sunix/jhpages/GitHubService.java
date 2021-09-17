package org.sunix.jhpages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.GHCreateRepositoryBuilder;
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
            return gitHub.getRepository(getFullRepoName()).getBranch("gh-pages") != null;
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

            // gitHub.createRepository(this.repoName).create();
            new GHCreateRepositoryBuilder(this.repoName, gitHub, "/user/repos") //
                    .autoInit(true) //
                    .defaultBranch("gh-pages") //
                    .create();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getFullRepoName() {
        return this.repoOrgName + "/" + this.repoName;
    }

    public void createBranch(String branchName) {

        try {

            deleteRecursif(cloneDirectory);
            UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(
                    System.getenv("GITHUB_LOGIN"), System.getenv("GITHUB_PASSWORD"));
            Git git = Git.cloneRepository() //
                    .setURI(getRepoURL()) //
                    .setCredentialsProvider(credentialsProvider) //
                    .setDirectory(cloneDirectory) //
                    .call();

            // only to be used if no gh-pages branch
            git.checkout().setOrphan(true).setName(branchName).call();
            // remove all the content
            deleteGitRepoContent(cloneDirectory);
            // copy a directory content to be pushed
            Arrays.stream( //
                    new File("/projects/jh-pages/mywebsite").listFiles()) //
                    .forEach(file -> {
                        try {
                            System.out.println("trying to copy " + file.getAbsolutePath() + " to "
                                    + cloneDirectory.getAbsolutePath());
                            copyDirectory(file.getAbsolutePath(),
                                    cloneDirectory.getAbsolutePath() + "/" + file.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            // git add .
            git.add().addFilepattern(".") //
                    .call();
            git.add().addFilepattern(".") //
                    .setUpdate(true)//
                    .call();
            git.commit()//
                    .setMessage("from jh-pages ...")//
                    .call();
            git.push() //
                    .setCredentialsProvider(credentialsProvider) //
                    .call();

        } catch (Exception e) {
            throw new RuntimeException("An error occured while trying cloning the repo " + branchName, e);
        }
    }

    public void copyContentAndPush() {
        try {

            deleteRecursif(cloneDirectory);
            UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(
                    System.getenv("GITHUB_LOGIN"), System.getenv("GITHUB_PASSWORD"));
            Git git = Git.cloneRepository() //
                    .setURI(getRepoURL()) //
                    .setCredentialsProvider(credentialsProvider) //
                    .setDirectory(cloneDirectory) //
                    .call();

            // only to be used if no gh-pages branch
            git.checkout() //
                    .setCreateBranch(true) //
                    .setStartPoint("origin/gh-pages") //
                    .setName("gh-pages") //
                    .call();
            // remove all the content
            deleteGitRepoContent(cloneDirectory);
            // copy a directory content to be pushed
            Arrays.stream( //
                    new File("/projects/jh-pages/mywebsite").listFiles()) //
                    .forEach(file -> {
                        try {
                            System.out.println("trying to copy " + file.getAbsolutePath() + " to "
                                    + cloneDirectory.getAbsolutePath());
                            copyDirectory(file.getAbsolutePath(),
                                    cloneDirectory.getAbsolutePath() + "/" + file.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            // git add .
            git.add().addFilepattern(".") //
                    .call();
            git.add().addFilepattern(".") //
                    .setUpdate(true)//
                    .call();
            git.commit()//
                    .setMessage("from jh-pages ...")//
                    .call();
            git.push() //
                    .setCredentialsProvider(credentialsProvider) //
                    .call();

        } catch (Exception e) {
            throw new RuntimeException("An error occured while trying do something in the repo gh-pages", e);
        }

    }

    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)
            throws IOException {
        Files.walk(Paths.get(sourceDirectoryLocation)).forEach(source -> {
            Path destination = Paths.get(destinationDirectoryLocation,
                    source.toString().substring(sourceDirectoryLocation.length()));
            try {
                Files.copy(source, destination);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    boolean deleteRecursif(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteRecursif(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    void deleteGitRepoContent(File gitRepoDirToBeDeleted) {
        Arrays.stream( //
                gitRepoDirToBeDeleted.listFiles()) //
                .filter(file -> !file.getName().equals(".git")) //
                .forEach(file -> deleteRecursif(file));
    }

    public String getRepoURL() {
        return "https://github.com/" + getFullRepoName();
    }

}
