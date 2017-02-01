package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.Item;
import hudson.model.Project;
import hudson.plugins.groovy.StringScriptSource;
import hudson.tasks.Builder;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.List;
import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class GroovySystemExitChecker extends AbstractCheck {

    public GroovySystemExitChecker() {
        super();
        this.setDescription("System groovy scripts run in same JVM as Jenkins master, so there's no surprise that System.exit() kills your Jenkins master. " +
                            "Throwing an exception is definitely better approach how to announce some problem in the script. Further details: " +
                            "<a href=https://issues.jenkins-ci.org/browse/JENKINS-14023>JENKINS-14023</a>.");
        this.setSeverity("High");
    }

    public boolean executeCheck(Item item) {
        boolean found = false;
        if (Jenkins.getInstance().pluginManager.getPlugin("groovy") != null) {

            if (Jenkins.getInstance().pluginManager.getPlugin("maven-plugin")!=null) {
                if (item instanceof MavenModuleSet) {
                    found = isSystemExit(((MavenModuleSet) item).getPrebuilders());
                }
            }
            if (item instanceof Project) {
                found = isSystemExit(((Project) item).getBuilders());
            }
            if (item instanceof MatrixProject) {
                found = isSystemExit(((MatrixProject) item).getBuilders());
            }

        } else {
            LOG.log(Level.INFO, "Groovy is not installed");
        }
        return found;
    }

    private boolean isSystemExit (List<Builder> builders) {
        boolean status = false;
        if (builders != null && builders.size() > 0 ) {
            for (Builder builder : builders) {
                // TODO: Reflection to decouple groovy plugin classs dependencies

                if (builder instanceof hudson.plugins.groovy.SystemGroovy) {
                    hudson.plugins.groovy.SystemGroovy builder1 = (hudson.plugins.groovy.SystemGroovy) builder;
                    String command = ((hudson.plugins.groovy.StringScriptSource) builder1.getScriptSource()).getCommand();
                    LOG.log(Level.INFO, "groovy " + command);
                    // TODO: Parse to search for non comments, otherwise some false positives!
                    if (command != null && command.toLowerCase().contains("system.exit") ) {
                        status = true;
                    }
                }
            }
        }
        return status;
    }
}
