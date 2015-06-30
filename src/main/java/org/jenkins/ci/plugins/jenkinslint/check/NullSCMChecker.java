package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Project;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class NullSCMChecker extends AbstractCheck {

    public NullSCMChecker() {
        super();
        this.setDescription("In order to comply with the style guide, Jenkins projects must avoid whitespace.");
        this.setSeverity("Low");
    }

    public boolean executeCheck(Item item) {
        if (item instanceof Project) {
            return (((Project) item).getScm() instanceof hudson.scm.NullSCM);
        }
        return false;
    }
}
