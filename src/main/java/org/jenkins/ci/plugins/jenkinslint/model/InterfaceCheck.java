package org.jenkins.ci.plugins.jenkinslint.model;

import hudson.model.Item;

/**
 * Check interface.
 * @author Victor Martinez
 */
public interface InterfaceCheck {

    boolean executeCheck(Item item);
    void setIgnored(String jobDescription);
    boolean isIgnored();
}
