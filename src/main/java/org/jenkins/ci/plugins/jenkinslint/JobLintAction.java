package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.model.Action;
import jenkins.model.TransientActionFactory;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractAction;
import org.jenkins.ci.plugins.jenkinslint.model.InterfaceCheck;
import org.jenkins.ci.plugins.jenkinslint.model.Job;
import org.jenkins.ci.plugins.jenkinslint.model.Lint;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@ExportedBean
public final class JobLintAction extends AbstractAction implements Action {

	private static final Logger LOG = Logger.getLogger(JobLintAction.class.getName());
	private hudson.model.Job project;
	private Job job;

	public static boolean isDisabled () {
		return !JenkinsLintGlobalConfiguration.get().isJobActionEnabled();
	}

	public JobLintAction(hudson.model.Job project) {
		this.project = project;
	}

	public final hudson.model.Job<?, ?> getProject() {
		return project;
	}

	@Exported
	public Job getJob() {
		return job;
	}

	public void getData() throws IOException {
		this.reloadCheckList();
		this.job = new Job(this.project.getName(), this.project.getUrl());
		for (InterfaceCheck checker : this.getCheckList()) {
			this.job.addLint(new Lint(checker.getName(), checker.executeCheck(this.project), checker.isIgnored(this.project.getDescription()), checker.isEnabled()) );
		}
		LOG.log(Level.FINE, this.job.getLintList().toString());
	}

	@Extension
	public static class Factory extends TransientActionFactory<hudson.model.Job> {
		@Override
		public Class<hudson.model.Job> type() {
			return hudson.model.Job.class;
		}

		@Override
		public Collection<? extends Action> createFor(hudson.model.Job target) {
			return Collections.singleton(new JobLintAction(target));
		}
	}
}
