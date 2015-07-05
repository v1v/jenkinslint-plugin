package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.check.ArtifactChecker;
import org.jenkins.ci.plugins.jenkinslint.check.CleanupWorkspaceChecker;
import org.jenkins.ci.plugins.jenkinslint.check.GitShallowChecker;
import org.jenkins.ci.plugins.jenkinslint.check.JavadocChecker;
import org.jenkins.ci.plugins.jenkinslint.check.JobAssignedLabelChecker;
import org.jenkins.ci.plugins.jenkinslint.check.JobDescriptionChecker;
import org.jenkins.ci.plugins.jenkinslint.check.JobLogRotatorChecker;
import org.jenkins.ci.plugins.jenkinslint.check.JobNameChecker;
import org.jenkins.ci.plugins.jenkinslint.check.MasterLabelChecker;
import org.jenkins.ci.plugins.jenkinslint.check.MavenJobTypeChecker;
import org.jenkins.ci.plugins.jenkinslint.check.MultibranchJobTypeChecker;
import org.jenkins.ci.plugins.jenkinslint.check.NullSCMChecker;
import org.jenkins.ci.plugins.jenkinslint.check.PollingSCMTriggerChecker;
import org.jenkins.ci.plugins.jenkinslint.model.InterfaceCheck;
import org.jenkins.ci.plugins.jenkinslint.model.Job;
import org.jenkins.ci.plugins.jenkinslint.model.Lint;

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

        checkList.add(new JobNameChecker());
        checkList.add(new JobDescriptionChecker());
        checkList.add(new JobAssignedLabelChecker());
        checkList.add(new MasterLabelChecker());
        checkList.add(new JobLogRotatorChecker());
        checkList.add(new MavenJobTypeChecker());
        checkList.add(new CleanupWorkspaceChecker());
        checkList.add(new JavadocChecker());
        checkList.add(new ArtifactChecker());
        checkList.add(new NullSCMChecker());
        checkList.add(new PollingSCMTriggerChecker( ));
        checkList.add(new GitShallowChecker());
        checkList.add(new MultibranchJobTypeChecker());

        for (AbstractProject item : Jenkins.getInstance().getAllItems(AbstractProject.class)) {
            LOG.log(Level.FINER, "queryChecks " + item.getDisplayName());
            Job newJob = new Job(item.getName(), item.getUrl());
            for (InterfaceCheck checker : checkList) {
                LOG.log(Level.FINER, checker.getClass().getName() + " " + item.getName() + " " + checker.executeCheck(item));
                newJob.addLint(new Lint(checker.getClass().getName(), checker.executeCheck(item), checker.isIgnored(item.getDescription())));
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
