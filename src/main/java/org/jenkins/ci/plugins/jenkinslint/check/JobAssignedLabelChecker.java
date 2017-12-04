package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.AbstractProject;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class JobAssignedLabelChecker extends AbstractCheck{

    public JobAssignedLabelChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.JobAssignedLabelCheckerDesc());
        this.setSeverity(Messages.JobAssignedLabelCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        try {
            return item instanceof AbstractProject && (((AbstractProject) item).getAssignedLabelString() == null ||
                    ((AbstractProject) item).getAssignedLabelString().length() == 0);
        } catch (java.lang.ClassCastException cce) {
            // Workaround https://issues.jenkins-ci.org/browse/JENKINS-46383
            return false;
        }
    }
}
