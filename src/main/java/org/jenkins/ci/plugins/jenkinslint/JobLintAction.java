package org.jenkins.ci.plugins.jenkinslint;

import hudson.model.AbstractProject;
import hudson.model.Action;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractAction;
import org.jenkins.ci.plugins.jenkinslint.model.InterfaceCheck;
import org.jenkins.ci.plugins.jenkinslint.model.Job;
import org.jenkins.ci.plugins.jenkinslint.model.Lint;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ExportedBean
public final class JobLintAction extends AbstractAction implements Action {

	private static final Logger LOG = Logger.getLogger(JobLintAction.class.getName());
	private AbstractProject<?, ?> project;
	private Job job;

	public static boolean isDisabled () {
		return !JenkinsLintGlobalConfiguration.get().isJobActionEnabled();
	}

	public JobLintAction(AbstractProject<?, ?> project) {
		this.project = project;
	}

	public AbstractProject<?, ?> getProject() {
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


}
