package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.Project;
import hudson.tasks.Builder;
import hudson.tasks.BuildWrapper;
import hudson.util.DescribableList;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class TimeoutChecker extends AbstractCheck {

    public TimeoutChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.TimeoutCheckerDesc());
        this.setSeverity(Messages.TimeoutCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        boolean notfound = true;
        if (Jenkins.getInstance().pluginManager.getPlugin("build-timeout") != null) {
            if (item.getClass().getName().endsWith("hudson.maven.MavenModuleSet")) {
                try {
                    // Wrappers
                    Method method = item.getClass().getMethod("getBuildWrappersList");
                    DescribableList<BuildWrapper,Descriptor<BuildWrapper>> buildWrapperList = ((DescribableList<BuildWrapper,Descriptor<BuildWrapper>>) method.invoke(item));
                    boolean isWrapperTimeout = isTimeout(buildWrapperList);
                    // builders
                    boolean isBuildTimeout = false;
                    Object getPrebuilders = item.getClass().getMethod("getPrebuilders", null).invoke(item);
                    if (getPrebuilders instanceof List) {
                        isBuildTimeout = isBuildStepTimeout((List) getPrebuilders);
                    }
                    notfound = !(isBuildTimeout || isWrapperTimeout);
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                    notfound = false;
                }
            }
            if (item instanceof Project) {
                notfound = !(isTimeout(((Project) item).getBuildWrappersList()) ||
                              isBuildStepTimeout (((Project)item).getBuilders()));
            }
            if (item.getClass().getSimpleName().equals("MatrixProject")) {
                try {
                    Object getBuildWrappersList = item.getClass().getMethod("getBuildWrappersList", null).invoke(item);
                    boolean isWrapperTimeout = false;
                    if (getBuildWrappersList instanceof List) {
                        isWrapperTimeout = isTimeout((List) getBuildWrappersList);
                    }
                    Object getBuilders = item.getClass().getMethod("getBuilders", null).invoke(item);
                    boolean isBuildTimeout = false;
                    if (getBuilders instanceof List) {
                        isBuildTimeout = isBuildStepTimeout((List) getBuilders);
                    }
                    notfound = !(isBuildTimeout || isWrapperTimeout);
                }catch (Exception e) {
                    LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                    notfound = false;
                }
            }
        } else {
            LOG.log(Level.FINE, "It's highly recommended to use  the plugin build-timeout");
        }
        return notfound;
    }

    private boolean isTimeout(List<BuildWrapper> builders) {
        boolean status = false;
        if (builders != null && builders.size() > 0 ) {
            for (BuildWrapper builder : builders) {
                if (builder.getClass().getName().endsWith("BuildTimeoutWrapper")) {
                    status = true;
                }
            }
        }
        return status;
    }

    private boolean isBuildStepTimeout(List<Builder> builders) {
        boolean found = false;
        if (builders != null && builders.size() > 0 ) {
            for (Builder builder : builders) {
                if (builder.getClass().getName().endsWith("BuildStepWithTimeout")) {
                    found = true;
                }
            }
        } else {
            found = false;
        }
        return found;
    }

}
