package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.AbstractProject;
import hudson.triggers.TimerTrigger;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;
import org.jenkins.ci.plugins.jenkinslint.utils.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Victor Martinez
 */
public class TimerTriggerChecker extends AbstractCheck{

    public TimerTriggerChecker(boolean enabled) {
        super(enabled);
        this.setDescription(Messages.TimerTriggerCheckerDesc());
        this.setSeverity(Messages.TimerTriggerCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        boolean found = false;
        if (item instanceof AbstractProject && ((AbstractProject) item).getTrigger(TimerTrigger.class) != null ) {
            String spec = ((AbstractProject) item).getTrigger(TimerTrigger.class).getSpec().toLowerCase();
            String[] myData = spec.split("\n");
            for (String line: myData) {
                if (!StringUtils.isH(line) && !StringUtils.isComment(line) &&
                    !StringUtils.isAt(line) && !StringUtils.isEmptyOrBlank(line)) {
                    found = true;
                }
            }
        } else {
            found = false;
        }
        return found;
    }
}
