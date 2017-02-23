package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class MavenJobTypeChecker extends AbstractCheck{

    public MavenJobTypeChecker() {
        super();
        this.setDescription(Messages.MavenJobTypeCheckerDesc());
        this.setSeverity(Messages.MavenJobTypeCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        return item.getClass().getName().endsWith("hudson.maven.MavenModuleSet");
    }
}
