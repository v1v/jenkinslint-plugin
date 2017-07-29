package org.jenkins.ci.plugins.jenkinslint.model;

import hudson.model.Item;

/**
 * Check interface.
 * @author Victor Martinez
 */
public interface InterfaceCheck {
    public String getName();
    boolean executeCheck(Item item);
    boolean isIgnored(String jobDescription);
}
