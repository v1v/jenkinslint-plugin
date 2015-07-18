package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;
import org.jenkins.ci.plugins.jenkinslint.model.InterfaceCheck;
import org.jenkins.ci.plugins.jenkinslint.model.Job;
import org.jenkins.ci.plugins.jenkinslint.model.Lint;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
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

        Reflections reflections = new Reflections("org.jenkins.ci.plugins.jenkinslint.check");
        Set<Class<? extends AbstractCheck>> classes = reflections.getSubTypesOf(AbstractCheck.class);
        for (Class<? extends AbstractCheck> reflectionClass : classes) {
            try {
                checkList.add(reflectionClass.newInstance());
            } catch (InstantiationException e) {
                LOG.log(Level.WARNING, "InstantiationException when running Reflection", e.getCause());
            } catch (IllegalAccessException e) {
                LOG.log(Level.WARNING, "IllegalAccessException when running Reflection", e.getCause());
            }
        }

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
