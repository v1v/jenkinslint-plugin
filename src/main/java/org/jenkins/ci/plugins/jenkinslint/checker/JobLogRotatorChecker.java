package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.Item;
import hudson.model.Project;
import jenkins.model.BuildDiscarder;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;
import hudson.tasks.LogRotator;

/**
 * @author Victor Martinez
 */
public class JobLogRotatorChecker extends AbstractCheck{

    private final int EMPTY = -1;

    public JobLogRotatorChecker(final boolean found, final boolean ignored) {
        super(found, ignored);
        this.setDescription("When setting Jenkins Jobs with some Log Rotator configuration helps to reduce the amount of disk space and speed up Jenkins UI.");
        this.setSeverity("High");
    }

    public boolean executeCheck(Item item) {
        if (item instanceof Project) {
            Project project = (Project) item;
            if ( project.getBuildDiscarder() != null ||
                 isLogRotatorOkConfigured(project.getBuildDiscarder())) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean isLogRotatorOkConfigured (BuildDiscarder buildDiscarder) {
        if (buildDiscarder instanceof LogRotator) {

            LogRotator rotator = (LogRotator) buildDiscarder;

            return (rotator.getArtifactDaysToKeep() > EMPTY ||
                    rotator.getArtifactNumToKeep() > EMPTY ||
                    rotator.getDaysToKeep() > EMPTY ||
                    rotator.getNumToKeep() > EMPTY);
        }
        return false;
    }
}
