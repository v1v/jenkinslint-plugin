package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.Item;
import hudson.model.Project;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class JobAssignedLabelChecker extends AbstractCheck{

    public JobAssignedLabelChecker(final String name, final boolean found, final boolean ignored) {
        super(name, found, ignored);
    }

    public boolean executeCheck(Item item) {
        return item instanceof Project && (((Project) item).getAssignedLabelString() == null || ((Project) item).getAssignedLabelString().length() == 0);
    }
}
