package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class MultibranchJobTypeChecker extends AbstractCheck{

    public MultibranchJobTypeChecker() {
        super();
        this.setDescription("Polling in Jenkins is inefficient, MultibranchJobProject polls for new branches and also " +
                            "every branch polls for new changes.<br/>It's strongly recommended to use another approach, " +
                            "such as: webhooks, Configuration as Code, ...");
        this.setSeverity("High");
    }

    public boolean executeCheck(Item item) {
        return item.getClass().getName().endsWith("FreeStyleMultiBranchProject");
    }
}
