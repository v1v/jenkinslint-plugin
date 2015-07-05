package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.AbstractProject;
import hudson.model.Item;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class MasterLabelChecker extends AbstractCheck{

    public MasterLabelChecker() {
        super();
        this.setDescription("When setting Jenkins Jobs you should set where those Jobs can run. and " +
                            "avoid master label.<br/>Jenkins master shouldn't consume any CPU for building jobs.");
        this.setSeverity("Medium");
    }

    public boolean executeCheck(Item item) {
        return item instanceof AbstractProject && ((AbstractProject) item).getAssignedLabel() != null &&
                ((AbstractProject) item).getAssignedLabel().getName().toLowerCase().contains("master");
    }
}
