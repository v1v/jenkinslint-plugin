package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.AbstractProject;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class NullSCMChecker extends AbstractCheck {

    public NullSCMChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.NullSCMCheckerDesc());
        this.setSeverity(Messages.NullSCMCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        if (item instanceof AbstractProject) {
            return (((AbstractProject) item).getScm() instanceof hudson.scm.NullSCM);
        }
        return false;
    }
}
