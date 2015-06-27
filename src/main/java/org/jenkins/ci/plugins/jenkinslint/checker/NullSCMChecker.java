package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.Project;
import hudson.tasks.ArtifactArchiver;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class NullSCMChecker extends AbstractCheck {

    public NullSCMChecker(final boolean found, final boolean ignored) {
        super(found, ignored);
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
