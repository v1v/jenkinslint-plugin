package org.jenkins.ci.plugins.jenkinslint.model;

import hudson.model.Item;

/**
 * Check interface.
 * @author Victor Martinez
 */
public interface CheckInterface {

    boolean executeCheck(Item item);
}