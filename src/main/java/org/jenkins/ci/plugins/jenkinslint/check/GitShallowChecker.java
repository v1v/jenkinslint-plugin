package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Project;
import jenkins.model.Jenkins;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class GitShallowChecker extends AbstractCheck {

    public GitShallowChecker() {
        super();
        this.setDescription(Messages.GitShallowCheckerDesc());
        this.setSeverity(Messages.GitShallowCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        if (item instanceof Project) {
            if (Jenkins.getInstance().pluginManager.getPlugin("git")!=null) {
                if (((Project) item).getScm().getClass().getName().endsWith("GitSCM")) {
                    boolean status = true;
                    try {
                        Method method = ((Project) item).getScm().getClass().getMethod("getExtensions", null);
                        Object extensionsList = method.invoke( ((Project) item).getScm());
                        if (extensionsList instanceof AbstractList) {
                            for (Object extension : ((AbstractList) extensionsList) ) {
                                if (extension.getClass().getName().endsWith("CloneOption")) {
                                    Object isShallow = extension.getClass().getMethod("isShallow", null).invoke(extension);
                                    if (isShallow instanceof Boolean) {
                                        status = ! ((Boolean) isShallow).booleanValue();
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
                }
            } else {
                LOG.log(Level.FINE, "Plugin GIT doesn't exist");
                return false;
            }
        }
        return false;
    }
}
