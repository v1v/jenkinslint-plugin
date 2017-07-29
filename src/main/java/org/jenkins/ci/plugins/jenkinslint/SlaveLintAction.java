package org.jenkins.ci.plugins.jenkinslint;

import hudson.model.Action;
import hudson.model.Computer;
import org.jenkins.ci.plugins.jenkinslint.model.AbstractAction;
import org.jenkins.ci.plugins.jenkinslint.model.InterfaceSlaveCheck;
import org.jenkins.ci.plugins.jenkinslint.model.Lint;
import org.jenkins.ci.plugins.jenkinslint.model.Slave;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ExportedBean
public class SlaveLintAction extends AbstractAction implements Action {

	private static final Logger LOG = Logger.getLogger(SlaveLintAction.class.getName());
	private Computer computer;
	private Slave slave;

	public SlaveLintAction(Computer target) {
		this.computer = target;
	}

	public Computer getComputer() {
		return computer;
	}

	@Exported
	public Slave getSlave() {
		return slave;
	}

	public void getData() throws IOException {
		this.reloadSlaveCheckList();
		this.slave = new Slave(this.computer.getName(), this.computer.getSearchUrl());
		for (InterfaceSlaveCheck checker : this.getSlaveCheckList()) {
			boolean status = checker.executeCheck(this.computer.getNode());
			LOG.log(Level.FINER, checker.getName() + " " + this.computer.getDisplayName() + " " + status);
			this.slave.addLint(new Lint(checker.getName(), status, checker.isIgnored(this.computer.getNode().getNodeDescription())));
		}
	}

}
