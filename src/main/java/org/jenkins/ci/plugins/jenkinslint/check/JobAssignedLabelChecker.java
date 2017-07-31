package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.AbstractProject;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

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
        return item instanceof AbstractProject && (((AbstractProject) item).getAssignedLabelString() == null ||
                                                    ((AbstractProject) item).getAssignedLabelString().length() == 0);
    }
}
