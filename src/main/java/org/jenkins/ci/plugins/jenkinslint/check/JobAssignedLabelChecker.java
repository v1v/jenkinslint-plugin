package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Project;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class JobAssignedLabelChecker extends AbstractCheck{

    public JobAssignedLabelChecker() {
        super();
        this.setDescription(Messages.JobAssignedLabelCheckerDesc());
        this.setSeverity(Messages.JobAssignedLabelCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        return item instanceof Project && (((Project) item).getAssignedLabelString() == null || ((Project) item).getAssignedLabelString().length() == 0);
    }
}
