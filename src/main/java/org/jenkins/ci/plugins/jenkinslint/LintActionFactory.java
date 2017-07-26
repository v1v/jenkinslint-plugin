package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.TransientProjectActionFactory;

import java.util.Collection;
import java.util.Collections;

@Extension
public class LintActionFactory extends TransientProjectActionFactory {
	@Override
	public Collection<? extends Action> createFor(AbstractProject target) {
		return Collections.singleton(new JobLintAction(target));
	}
}
