#!/bin/bash
echo -n 'Enter your gh-user:';
read user;
export GITHUB_LOGIN=$user;
echo -n 'Enter your gh-password:';
read -s password; export GITHUB_PASSWORD=$password;
#read -p 'Enter the program args [jh-pages]: ' name; name=${name:-'jh-pages'};
set -o xtrace;
# mvn compile quarkus:dev -Dquarkus.args=\"$name\"
mvn compile quarkus:dev -Dquarkus.args="sunix/test-something /projects/jh-pages/sandbox"
