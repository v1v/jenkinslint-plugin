package org.jenkins.ci.plugins.jenkinslint.model;

import hudson.model.Node;

/**
 * Check interface.
 * @author Victor Martinez
 */
public interface InterfaceSlaveCheck {
    public String getName();
    boolean executeCheck(Node item);
    boolean isIgnored(String jobDescription);
    public boolean isEnabled();
}
