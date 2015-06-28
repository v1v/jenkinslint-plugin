Jenkins Lint Plugin
===================

This plugin has mainly two goals:
- To make it easier to detect issues in your Jenkins configuration that will cause Jenkins to blow up when you attempt to run those jobs.
- To encourage discussion within the Jenkins community on the more subjective stuff. Having a set of checks to base discussion on helps drive out what we as a community think is good style.

See [Jenkins Lint Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Jenkins+Lint+Plugin) for more information.

[![Build Status](https://buildhive.cloudbees.com/job/jenkinsci/job/jenkins-lint-plugin/badge/icon)](https://buildhive.cloudbees.com/job/jenkinsci/job/jenkins-lint-plugin/)


Motivation
==========



List available Checks
=====================



Development
===========

Start the local Jenkins instance:

    mvn hpi:run


How to install
--------------

Run

	mvn clean package

to create the plugin .hpi file.


To install:

1. copy the resulting ./target/jenkins-lint-plugin.hpi file to the $JENKINS_HOME/plugins directory. Don't forget to restart Jenkins afterwards.

2. or use the plugin management console (http://example.com:8080/pluginManager/advanced) to upload the hpi file. You have to restart Jenkins in order to find the plugin in the installed plugins list.


Plugin releases
---------------

	mvn release:prepare release:perform


Authors
=======

Victor Martinez


References
==========


License
=======

    The MIT License

    Copyright (c) 2015, Victor Martinez

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
