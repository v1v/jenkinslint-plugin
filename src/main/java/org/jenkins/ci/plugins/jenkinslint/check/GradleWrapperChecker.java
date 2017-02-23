package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModuleSet;
import hudson.model.Item;
import hudson.model.Project;
import hudson.tasks.Builder;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.List;
import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class GradleWrapperChecker extends AbstractCheck {

    public GradleWrapperChecker() {
        super();
        this.setDescription(Messages.GradleWrapperCheckerDesc());
        this.setSeverity(Messages.GradleWrapperCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        boolean found = false;
        if (Jenkins.getInstance().pluginManager.getPlugin("gradle") != null) {

            if (Jenkins.getInstance().pluginManager.getPlugin("maven-plugin")!=null) {
                if (item instanceof MavenModuleSet) {
                    found = isGradlew(((MavenModuleSet) item).getPrebuilders());
                }
            }
            if (item instanceof Project) {
                found = isGradlew(((Project) item).getBuilders());
            }
            if (item instanceof MatrixProject) {
                found = isGradlew(((MatrixProject) item).getBuilders());
            }

        }
        return found;
    }

    private boolean isGradlew (List<Builder> builders) {
        boolean status = false;
        if (builders != null && builders.size() > 0 ) {
            for (Builder builder : builders) {
                if (builder.getClass().getName().endsWith("Gradle")) {
                    try {
                        Object isUseWrapper = builder.getClass().getMethod("isUseWrapper", null).invoke(builder);
                        if (isUseWrapper instanceof Boolean) {
                            status = ! ((Boolean) isUseWrapper).booleanValue();
                            LOG.log(Level.FINE, "isGradlew " + !status);
                        }
                    } catch (Exception e) {
                        LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                        status = false;
                    }
                }
            }
        }
        return status;
    }
}
