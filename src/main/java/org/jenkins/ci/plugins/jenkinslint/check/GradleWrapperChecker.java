package org.jenkins.ci.plugins.jenkinslint.check;

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

    public GradleWrapperChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.GradleWrapperCheckerDesc());
        this.setSeverity(Messages.GradleWrapperCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        boolean found = false;
        if (Jenkins.getInstance().pluginManager.getPlugin("gradle") != null) {
            if (item.getClass().getSimpleName().equals("MavenModuleSet")) {
                try {
                    Object getPrebuilders = item.getClass().getMethod("getPrebuilders", null).invoke(item);
                    if (getPrebuilders instanceof List) {
                        found = isGradlew((List) getPrebuilders);
                    }
                }catch (Exception e) {
                    LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                }
            }
            if (item instanceof Project) {
                found = isGradlew(((Project) item).getBuilders());
            }
            if (item.getClass().getSimpleName().equals("MatrixProject")) {
                try {
                    Object getBuilders = item.getClass().getMethod("getBuilders", null).invoke(item);
                    if (getBuilders instanceof List) {
                        found = isGradlew((List) getBuilders);
                    }
                }catch (Exception e) {
                    LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                }
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
