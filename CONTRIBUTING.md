# Contributing to the JenkinsLint plugin Project

## Ways To Help
We are always happy for folk to help us out on this project.  Here are some ways how:

### Documentation

* Small Changes (fixes to spelling, inaccuracies, errors) - just do it.

### New Features
* Feature Requests - "New Feature" issue on the [Jenkins JIRA](https://issues.jenkins-ci.org/secure/Dashboard.jspa). Remember to add the `jenkinslint-plugin` component.
* Feature Implementations - Even better than this is an implementation. Simply fork this repo, create a branch (named after the JIRA "New Feature" you created earlier), implement it yourself and submit a Pull Request. Get started with the _Git Protocol_ section below
* Feature Implementations - Even better than this is an implementation. Simply fork this repo, create a branch (named after the JIRA "New Feature" you created earlier), implement it yourself and submit a Pull Request. Get started with the _Git Protocol_ section below

### Bugs
* New "Bug" issue on the [Jenkins JIRA](https://issues.jenkins-ci.org/secure/Dashboard.jspa). Remember to add the `jenkinslint-plugin` component.
* Bug Fixes - Even better than this is a fix. Simply fork our repo, create a branch (named after the JIRA "Bug" you created earlier), implement it yourself and submit a Pull Request. Remember to follow the _Git Protocol_ section below.

## Git Protocol
If you want to make a change to the code on `jenkinsci/jenkinslint-plugin`, here's the protocol we follow (you need a Github account in order to do this):

1. Fork the `jenkinsci/jenkinslint-plugin` repository to your account.
2. On your local machine, clone your copy of the `jenkinslint-plugin` repo.
3. Again on your local machine, create a branch, ideally named after a JIRA issue you've created for the work.
4. Switch to the local branch and make your changes. Commit them as you go, and when you're happy, push them to your repo branch.
5. Also update the documentation, see below.
6. Then, on the GitHub website, find the branch you created for your work, and submit a Pull Request. This will then poke us and we'll take a look at it. We might ask you to rebase (if the trunk has moved on and there are some conflicts) or we might suggest some more changes.
7. If the all looks good, we'll merge the Pull Request.

Try to focus. It's not required to add all options for a certain plugin to get the pull request merged. In fact, it may
even delay the merge until someone finds time to review a huge change. Only implement the options you really need and
leave room so that the remaining options can be added when needed.

## Our Basic Design Decisions / Conventions
1. We write tests using [JenkinsRule](https://wiki.jenkins-ci.org/display/JENKINS/Unit+Test#UnitTest-Example), so if (for example) you add a new Check (e.g. `ScmCheck`), then add a corresponding ScmCheckTestCase in the tests directory tree.

## Code Style
* Indentation: use 4 spaces, no tabs.
* Use a maximum line length of 120 characters.
* We roughly follow the [Java](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html) style guidelines.
* When using IntelliJ IDEA, use the default code style, but disable '*' imports for Java and Groovy.
* Add a CRLF at the end of a file.
