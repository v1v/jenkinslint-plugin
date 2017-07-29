package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class MultibranchJobTypeChecker extends AbstractCheck{

    public MultibranchJobTypeChecker() {
        super();
        this.setDescription(Messages.MultibranchJobTypeCheckerDesc());
        this.setSeverity(Messages.MultibranchJobTypeCheckerSeverity());
    }

    public boolean executeCheck(Item item) {
        return item.getClass().getName().endsWith("FreeStyleMultiBranchProject");
    }
}
