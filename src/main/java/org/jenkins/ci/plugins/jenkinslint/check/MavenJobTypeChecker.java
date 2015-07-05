package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class MavenJobTypeChecker extends AbstractCheck{

    public MavenJobTypeChecker() {
        super();
        this.setDescription("Maven job type builds considerably slowly compared to the freestyle projects.<br/>Besides " +
                            "of that it has its own set of bugs. t's worth to use freeStyle jobs and specify the maven " +
                            "build wrapper instead.<br/>" +
                            "<a href=https://issues.jenkins-ci.org/browse/JENKINS-22354>Open ticket</a>.");
        this.setSeverity("High");
    }

    public boolean executeCheck(Item item) {
        return item instanceof hudson.maven.MavenModuleSet;
    }
}
