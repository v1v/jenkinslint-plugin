package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Project;
import hudson.triggers.TimerTrigger;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Victor Martinez
 */
public class TimerTriggerChecker extends AbstractCheck{

    public TimerTriggerChecker() {
        super();
        this.setDescription(Messages.TimerTriggerCheckerDesc());
        this.setSeverity(Messages.TimerTriggerCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        boolean found = false;
        if (item instanceof Project && ((Project) item).getTrigger(TimerTrigger.class) != null ) {
            String spec = ((Project) item).getTrigger(TimerTrigger.class).getSpec().toLowerCase();
            if (spec.contains("h")) {
                String[] myData = spec.split("\n");
                for (String line: myData) {
                    if (!isH(line) && !isComment(line)) {
                        found = true;
                    }
                }
            } else {
                found = true;
            }
        } else {
            found = false;
        }
        return found;
    }

    private boolean isComment (String line) {
        boolean found = false;
        Pattern p = Pattern.compile("^\\s*#\\s*.*");
        Matcher m = p.matcher(line);
        found = m.matches();
        return found;
    }

    private boolean isH (String line) {
        boolean found = false;
        Pattern p = Pattern.compile("^\\s*h.*");
        Matcher m = p.matcher(line);
        found = m.matches();
        return found;
    }
}
