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
        this.setDescription("By distributing the wrapper with your project, anyone can work with it without needing to " +
                            "install Gradle beforehand. Even better, users of the build <br/> are guaranteed to use the " +
                            "version of Gradle that the build was designed to work with. Further details: " +
                            "<a href=https://docs.gradle.org/current/userguide/gradle_wrapper.html> Gradle Wrapper docs</a>.");
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

        }
        return found;
    }

    private boolean isSystemExit (List<Builder> builders) {
        boolean status = false;
        if (builders != null && builders.size() > 0 ) {
            for (Builder builder : builders) {
                if (builder instanceof hudson.plugins.groovy.SystemGroovy) {
                    if ((( hudson.plugins.groovy.SystemGroovy) builder).getCommand().toLowerCase().contains("system.exit") ) {
                        status = true;
                    }
                }
            }
        }
        return status;
    }
}
