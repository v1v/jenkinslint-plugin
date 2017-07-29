package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Computer;
import hudson.model.TransientComputerActionFactory;

import java.util.Collection;
import java.util.Collections;

@Extension
public class SlaveLintActionFactory extends TransientComputerActionFactory {
	@Override
	public Collection<? extends Action> createFor(Computer target) {
		return Collections.singleton(new SlaveLintAction(target));
	}
}
