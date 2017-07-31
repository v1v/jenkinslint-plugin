package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Job;
import hudson.tasks.LogRotator;
import jenkins.model.BuildDiscarder;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class JobLogRotatorChecker extends AbstractCheck{

    private final int EMPTY = -1;

    public JobLogRotatorChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.JobLogRotatorCheckerDesc());
        this.setSeverity(Messages.JobLogRotatorCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        if (item instanceof Job) {
            Job project = (Job) item;
            if ( project.getBuildDiscarder() != null &&
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
