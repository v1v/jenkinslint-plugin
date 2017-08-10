package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Project;
import hudson.tasks.Builder;
import hudson.tasks.CommandInterpreter;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;
import org.jenkins.ci.plugins.jenkinslint.utils.StringUtils;

import java.util.List;
import java.util.logging.Level;

/**
 * @author Victor Martinez
 */
public class HardcodedScriptChecker extends AbstractCheck {

    public final static int THRESHOLD = 2;
    private int threshold;

    public HardcodedScriptChecker(boolean enabled, int threshold) {
        super(enabled);
        this.setDescription(Messages.HardcodedScriptCheckerDesc());
        this.setSeverity(Messages.HardcodedScriptCheckerSeverity());
        this.setThreshold(threshold);
    }

    public boolean executeCheck(Item item) {
        LOG.log(Level.FINE, "executeCheck " + item);
        boolean found = false;
        if (item.getClass().getSimpleName().equals("MavenModuleSet")) {
            try {
                Object getPrebuilders = item.getClass().getMethod("getPrebuilders", null).invoke(item);
                if (getPrebuilders instanceof List) {
                    found = isBuilderHarcoded((List) getPrebuilders);
                }
            }catch (Exception e) {
                LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
            }
        }
        if (item instanceof Project) {
            found = isBuilderHarcoded (((Project)item).getBuilders());
        }
        if (item.getClass().getSimpleName().equals("MatrixProject")) {
            try {
                Object getBuilders = item.getClass().getMethod("getBuilders", null).invoke(item);
                if (getBuilders instanceof List) {
                    found = isBuilderHarcoded((List) getBuilders);
                }
            }catch (Exception e) {
                LOG.log(Level.WARNING, "Exception " + e.getMessage(), e.getCause());
            }
        }
        return found;
    }

    private boolean isBuilderHarcoded (List<Builder> builders) {
        boolean found = false;
        if (builders != null && builders.size() > 0 ) {
            for (Builder builder : builders) {
                if (builder instanceof hudson.tasks.Shell || builder instanceof hudson.tasks.BatchFile) {
                    if (isHarcoded (((CommandInterpreter)builder).getCommand(), this.getThreshold())) {
                        found = true;
                    }
                }
            }
        } else {
            found = false;
        }
        return found;
    }

    private boolean isHarcoded (String content, int threshold) {
        if (content != null) {
            int length = 0;
            for (String line : content.split("\r\n|\r|\n")) {
                if (!StringUtils.isEmptyOrBlank(line)) {
                    length++;
                }
            }
            return length > threshold;
        } else {
            return false;
        }
    }

    public int getThreshold () {
        return this.threshold;
    }


    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
