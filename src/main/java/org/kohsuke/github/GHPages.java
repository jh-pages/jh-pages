package org.kohsuke.github;

import java.io.IOException;
import java.net.URL;

public class GHPages extends GHObject {

    private String html_url;

    public static GHPages read(GitHub root, String owner, String name) throws IOException {
        return root.createRequest().withUrlPath("/repos/" + owner + '/' + name + "/pages").fetch(GHPages.class).wrap(root);
    }
    
    @Override
    public URL getHtmlUrl() {
        return GitHubClient.parseURL(html_url);
    }

    GHPages wrap(GitHub root) {
        this.root = root;
        return this;
    }

}
