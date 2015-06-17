package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.Item;
import hudson.model.Project;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.Check;
import org.jenkins.ci.plugins.jenkinslint.model.Job;

import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Extension
public final class JenkinsLintAction implements RootAction {

    private static final Logger LOG = Logger.getLogger(JenkinsLintAction.class.getName());
    private Hashtable<String, Job> jobSet = new Hashtable<String, Job>();

    public void getData() throws IOException {
        LOG.log(Level.FINE, "getData()");
        jobSet.clear();

        for (Project item : Jenkins.getInstance().getAllItems(Project.class)) {
            LOG.log(Level.FINER, "queryChecks " + item.getDisplayName());
            Check newCheck = new Check ("CheckJobName", checkJobName(item.getName()), isIgnored("CheckJobName", item.getDescription()));
            Job newJob = new Job(item.getName(), item.getUrl());
            newJob.addCheck(newCheck);
            jobSet.put(item.getName(),newJob);
            LOG.log(Level.INFO, newJob.toString());
        }
    }

    private boolean isIgnored(String ruleName, String jobDescription){
        if (ruleName != null && jobDescription != null) {
            if (jobDescription.indexOf("lint:ignored:"+ ruleName) > -1) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean checkJobName(String jobName) {
        if (jobName.contains(" ")){
            return true;
        }
        return false;
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

    public Hashtable<String, Job> getJobSet() {
        return jobSet;
    }
}
