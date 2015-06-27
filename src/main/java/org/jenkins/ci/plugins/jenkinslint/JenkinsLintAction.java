package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.Project;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.checker.*;
import org.jenkins.ci.plugins.jenkinslint.model.Check;
import org.jenkins.ci.plugins.jenkinslint.model.CheckInterface;
import org.jenkins.ci.plugins.jenkinslint.model.Job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Extension
public final class JenkinsLintAction implements RootAction {

    private static final Logger LOG = Logger.getLogger(JenkinsLintAction.class.getName());
    private Hashtable<String, Job> jobSet = new Hashtable<String, Job>();
    private ArrayList<CheckInterface> checkList = new ArrayList<CheckInterface>();

    public void getData() throws IOException {
        LOG.log(Level.FINE, "getData()");
        jobSet.clear();
        checkList.clear();

        checkList.add(new JobNameChecker("JobName", "", "", false, false));
        checkList.add(new JobDescriptionChecker("JobDescription", "", "", false, false));
        checkList.add(new JobAssignedLabelChecker("JobAssignedLabel", "", "", false, false));
        checkList.add(new JobLogRotatorChecker("JobLogRotator", "", "", false, false));
        checkList.add(new MavenJobTypeChecker("MavenJob", "", "", false, false));
        checkList.add(new CleanupWorkspaceChecker("CleanUp", "", "", false, false));
        checkList.add(new JavadocChecker("Javadoc", "", "", false, false));
        checkList.add(new ArtifactChecker("Artifact", "", "", false, false));
        checkList.add(new NullSCMChecker("NullSCM", "", "", false, false));
        checkList.add(new PollingSCMTriggerChecker("PollingSCM", "", "", false, false));
        checkList.add(new GitShallowChecker("GitShallow", "", "", false, false));

        for (Project item : Jenkins.getInstance().getAllItems(Project.class)) {
            LOG.log(Level.FINER, "queryChecks " + item.getDisplayName());
            Job newJob = new Job(item.getName(), item.getUrl());
            for (CheckInterface checker : checkList) {
                LOG.log(Level.INFO, checker.getClass().getName() + " " + item.getName() + " " + checker.executeCheck(item));
                newJob.addCheck(new Check(checker.getClass().getName(),checker.executeCheck(item),false));
            }
            jobSet.put(item.getName(),newJob);
            LOG.log(Level.FINER, newJob.toString());
        }
    }

    private boolean isIgnored(String ruleName, String jobDescription) {
        return ruleName != null && jobDescription != null && jobDescription.contains("lint:ignored:" + ruleName);
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

    public ArrayList<CheckInterface> getCheckList() {
        return checkList;
    }
}
