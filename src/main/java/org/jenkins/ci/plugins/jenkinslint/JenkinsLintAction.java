package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.Project;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.check.*;
import org.jenkins.ci.plugins.jenkinslint.model.Lint;
import org.jenkins.ci.plugins.jenkinslint.model.InterfaceCheck;
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
    private ArrayList<InterfaceCheck> checkList = new ArrayList<InterfaceCheck>();

    public void getData() throws IOException {
        LOG.log(Level.FINE, "getData()");
        jobSet.clear();
        checkList.clear();

        checkList.add(new JobNameChecker(false, false));
        checkList.add(new JobDescriptionChecker(false, false));
        checkList.add(new JobAssignedLabelChecker(false, false));
        checkList.add(new JobLogRotatorChecker(false, false));
        checkList.add(new MavenJobTypeChecker(false, false));
        checkList.add(new CleanupWorkspaceChecker(false, false));
        checkList.add(new JavadocChecker(false, false));
        checkList.add(new ArtifactChecker(false, false));
        checkList.add(new NullSCMChecker(false, false));
        checkList.add(new PollingSCMTriggerChecker( false, false));
        checkList.add(new GitShallowChecker(false, false));

        for (Project item : Jenkins.getInstance().getAllItems(Project.class)) {
            LOG.log(Level.FINER, "queryChecks " + item.getDisplayName());
            Job newJob = new Job(item.getName(), item.getUrl());
            for (InterfaceCheck checker : checkList) {
                LOG.log(Level.FINER, checker.getClass().getName() + " " + item.getName() + " " + checker.executeCheck(item));
                newJob.addLint(new Lint(checker.getClass().getName(), checker.executeCheck(item), false));
            }
            jobSet.put(item.getName(),newJob);
            LOG.log(Level.FINER, newJob.toString());
            //TODO: Update Job Data item.getActions().add(new GeneratedJobsAction());
        }
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

    public ArrayList<InterfaceCheck> getCheckList() {
        return checkList;
    }
}
