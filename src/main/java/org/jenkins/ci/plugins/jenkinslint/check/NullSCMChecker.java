package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import hudson.model.Project;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class NullSCMChecker extends AbstractCheck {

    public NullSCMChecker() {
        super();
        this.setDescription("Jenkins works fine with cron/batch tasks, It's strongly recommended to use any " +
                            "SCM tool and therefore to have in that repo whatever script you need to run.");
        this.setSeverity("Low");
    }

    public boolean executeCheck(Item item) {
        if (item instanceof Project) {
            return (((Project) item).getScm() instanceof hudson.scm.NullSCM);
        }
        return false;
    }
}
