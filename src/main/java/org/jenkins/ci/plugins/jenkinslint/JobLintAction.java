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

	/**
	 * Whether the project has been disabled, since isDisabled is part of the AbstractProject and WorkflowJob doesn't
	 * extend it then let's use reflection.
	 *
	 * @return
	 */
	public boolean isProjectEnabled() {
		boolean enabled = true;
		if (this.project != null) {
			try {
				Object isDisabled = this.project.getClass().getMethod("isDisabled", null).invoke(this.project);
				if (isDisabled instanceof Boolean) {
					enabled = !(Boolean) isDisabled;
				}
			} catch (Exception e) {
				LOG.log(Level.FINE, "Exception " + e.getMessage(), e.getCause());
			}
		}
		return enabled;

	}

	public static boolean isDisabled () {
		return !JenkinsLintGlobalConfiguration.get().isJobActionEnabled();
	}

	public static boolean isLintDisabledJobEnabled() {
		return JenkinsLintGlobalConfiguration.get().isLintDisabledJobEnabled();
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

		// In case it has been disabled from the global settings then no run lint analysis
		if (isProjectEnabled() || isLintDisabledJobEnabled()) {
			this.job = new Job(this.project.getName(), this.project.getUrl());
			for (InterfaceCheck checker : this.getCheckList()) {
				this.job.addLint(new Lint(checker.getName(), checker.executeCheck(this.project), checker.isIgnored(this.project.getDescription()), checker.isEnabled()));
			}
			LOG.log(Level.FINE, this.job.getLintList().toString());
		}
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
