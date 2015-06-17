package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.RootAction;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Extension
public final class JenkinsLintAction implements RootAction {

    private static final Logger LOG = Logger.getLogger(JenkinsLintAction.class.getName());

    public void getData() throws IOException {
        LOG.log(Level.FINE, "getData()");
    }

    public String getDisplayName() {
        return Messages.DisplayName();
    }

    public String getIconFileName() {
        return Messages.IconFileName();
    }

    public String getUrlName() {
        return Messages.UrlName();
    }

}
