# jh-pages

```
jh-pages ./folder/ username repo
```

should push the `./folder` to `username.github.io/repo` github pages. 

The flow to implement:

 - ✔️ check if repo exist, if not create it -> need github lib https://github.com/quarkiverse/quarkus-github-api
   - ✔️ creating the repo
 - check if gh-pages activated in repo and if not ... create it. -> not needed
 - ✔️ generate the git url to clone
 - ✔️ check if gh-pages branch exist
 - ✔️ clone the project with only gh-pages
    - clone the project
    - if gh-pages branch doesn't exist -> give a try with git CLI to see what we could do when the repo empty. 
 - ✔️ remove content
 - ✔️ copy content
 - ✔️ add, commit push


This project is under development as part of the Happy Live Coding in Twitch. See https://twitter.com/__sunix_/status/1377887535565799425.

Every Friday 6pm CEST.

Live stream: https://www.twitch.tv/happylivecoding
Replay: https://www.youtube.com/playlist?list=PLAtUK_ilLV_0sutSI_rDW8_fmD1k2lux_

TODO next:
- also see if other projects using quarkus-github-api contains tests
- if not, start "copying" wiremock tests from https://github.com/hub4j/github-api/blob/main/src/test/java/org/kohsuke/github/AbstractGitHubWireMockTest.java (because the tests artifacts are not published) 
