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

    public NullSCMChecker(final String name, final String description, final String severity, final boolean found, final boolean ignored) {
        super(name, description, severity, found, ignored);
    }

    public boolean executeCheck(Item item) {
        if (item instanceof Project) {
            return (((Project) item).getScm() instanceof hudson.scm.NullSCM);
        }
        return false;
    }
}
