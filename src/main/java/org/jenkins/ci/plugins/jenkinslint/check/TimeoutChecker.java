package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.matrix.MatrixProject;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.Project;
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

    public TimeoutChecker() {
        super();
        this.setDescription(Messages.TimeoutCheckerDesc());
        this.setSeverity(Messages.TimeoutCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        boolean notfound = true;
        if (Jenkins.getInstance().pluginManager.getPlugin("build-timeout") != null) {
            if (item.getClass().getName().endsWith("hudson.maven.MavenModuleSet")) {
                try {
                    Method method = item.getClass().getMethod("getBuildWrappersList");
                    DescribableList<BuildWrapper,Descriptor<BuildWrapper>> buildWrapperList = ((DescribableList<BuildWrapper,Descriptor<BuildWrapper>>) method.invoke(item));
                    notfound = !isTimeout(buildWrapperList);
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                    notfound = false;
                }
            }
            if (item instanceof Project) {
                notfound = !isTimeout(((Project) item).getBuildWrappersList());
            }
            if (item instanceof MatrixProject) {
                notfound = !isTimeout(((MatrixProject) item).getBuildWrappersList());
            }
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
}
