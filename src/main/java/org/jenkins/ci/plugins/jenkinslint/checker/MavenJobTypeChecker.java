package org.jenkins.ci.plugins.jenkinslint.checker;

import hudson.model.Item;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class MavenJobTypeChecker extends AbstractCheck{

    public MavenJobTypeChecker(final boolean found, final boolean ignored) {
        super(found, ignored);
        this.setDescription("In order to comply with the style guide, Jenkins projects must avoid whitespace.");
        this.setSeverity("High");
    }

    public boolean executeCheck(Item item) {
        return item instanceof hudson.maven.MavenModuleSet;
    }
}
