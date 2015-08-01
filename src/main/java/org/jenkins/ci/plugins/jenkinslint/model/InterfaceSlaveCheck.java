package org.jenkins.ci.plugins.jenkinslint.model;

import hudson.model.Node;

/**
 * Check interface.
 * @author Victor Martinez
 */
public interface InterfaceSlaveCheck {

    boolean executeCheck(Node item);
    boolean isIgnored(String jobDescription);
}
