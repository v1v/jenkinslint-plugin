package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.AbstractProject;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.logging.Level;
import hudson.PluginWrapper;

/**
 * @author Victor Martinez
 */
public class GitRefChecker extends AbstractCheck {

    public GitRefChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.GitRefCheckerDesc());
        this.setSeverity(Messages.GitRefCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        if (item instanceof AbstractProject) {
            PluginWrapper plugin = Jenkins.getInstance().pluginManager.getPlugin("git");
            if (plugin!=null && !plugin.getVersionNumber().isOlderThan(new hudson.util.VersionNumber("2.0"))) {
                if (((AbstractProject) item).getScm().getClass().getName().endsWith("GitSCM")) {
                    boolean status = true;
                    try {
                        Method method = ((AbstractProject) item).getScm().getClass().getMethod("getExtensions", null);
                        Object extensionsList = method.invoke( ((AbstractProject) item).getScm());
                        if (extensionsList instanceof AbstractList) {
                            for (Object extension : ((AbstractList) extensionsList) ) {
                                if (extension.getClass().getName().endsWith("CloneOption")) {
                                    Object reference = extension.getClass().getMethod("getReference", null).invoke(extension);
                                    if (reference instanceof String) {
                                        status = ((String) reference).isEmpty();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
                        status = false;
                    } finally {
                        return status;
                    }
                } else {
                    LOG.log(Level.FINE, "Plugin GIT hasn't been configured in this project");
                    return false;
                }
            } else {
                LOG.log(Level.FINE, "Plugin GIT doesn't exist");
                return false;
            }
        }
        return false;
    }
}
