# jh-pages

```
jh-pages ./folder/ username repo
```

should push the `./folder` to `username.github.io/repo` github pages. 

The flow to implement:

 - ✔️ check if repo exist, if not create it -> need github lib https://github.com/quarkiverse/quarkus-github-api
 - check if gh-pages activated in repo and if not ... create it.
 - generate the git url to clone
 - ✔️ check if gh-pages branch exist
 - clone the project with only gh-pages
 - remove content
 - copy content
 - add, commit push


This project is under development as part of the Happy Live Coding in Twitch. See https://twitter.com/__sunix_/status/1377887535565799425.

Live stream: https://www.twitch.tv/happylivecoding
Replay: https://www.youtube.com/playlist?list=PLAtUK_ilLV_0sutSI_rDW8_fmD1k2lux_

Next stream: [14 May 2021 - 6pmCEST](https://www.google.com/calendar/render?action=TEMPLATE&text=Happy+live+coding+with+Sun+%234&details=Happy+live+coding+with+%40sun_ix+I+will+live+code+for+1+hour+on+an+opensource+project+with+you.+every+3+weeks.+Next%3A+fri+14+may+2021+at+6pm+CEST&location=https%3A%2F%2Fwww.twitch.tv%2Fhappylivecoding&dates=20210514T160000Z%2F20210514T170000Z)
