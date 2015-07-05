package org.jenkins.ci.plugins.jenkinslint.check;

import hudson.model.Item;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractCheck;

/**
 * @author Victor Martinez
 */
public class JobNameChecker extends AbstractCheck{

    public JobNameChecker() {
        super();
        this.setDescription("When creating Jenkins Jobs you must avoid whitespace. In order to comply with the style " +
                            "guide");
        this.setSeverity("High");
    }

    public boolean executeCheck(Item item) {
        return item.getName().contains(" ");
    }
}
