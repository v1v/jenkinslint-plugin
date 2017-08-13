package org.jenkins.ci.plugins.jenkinslint.model;

import hudson.model.Api;
import org.jenkins.ci.plugins.jenkinslint.JenkinsLintGlobalConfiguration;
import org.jenkins.ci.plugins.jenkinslint.Messages;
import org.jenkins.ci.plugins.jenkinslint.check.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AbstractAction class.
 * @author Victor Martinez
 */
public abstract class AbstractAction {
    protected static final Logger LOG = Logger.getLogger(AbstractAction.class.getName());
    private ArrayList<InterfaceCheck> checkList = new ArrayList<InterfaceCheck>();
    private ArrayList<InterfaceSlaveCheck> slaveCheckList = new ArrayList<InterfaceSlaveCheck>();

    public AbstractAction() {
        super();
    }

    public String getDisplayName() {
        return Messages.DisplayName();
    }

    public String getIconFileName() {
        return Messages.IconFileName();
    }

    public String getUrlName() {
        return Messages.UrlName();
    }

    public Api getApi() {
        try {
            this.getData();
        } catch (IOException ioe) {
            LOG.log(Level.WARNING, "Failing when getting the JenkinsLint data through the API");
        }
        return new Api(this);
    }

    public abstract void getData() throws IOException;

    public ArrayList<InterfaceCheck> getCheckList() {
        return checkList;
    }

    public ArrayList<InterfaceSlaveCheck> getSlaveCheckList() {
        return slaveCheckList;
    }

    protected void reloadCheckList() {
        checkList.clear();

        JenkinsLintGlobalConfiguration config = JenkinsLintGlobalConfiguration.get();
        checkList.add(new JobNameChecker((config.isJobNameCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new JobDescriptionChecker((config.isJobDescriptionCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new JobAssignedLabelChecker((config.isJobAssignedLabelCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new MasterLabelChecker((config.isMasterLabelCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new JobLogRotatorChecker((config.isJobLogRotatorCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new MavenJobTypeChecker((config.isMavenJobTypeCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new CleanupWorkspaceChecker((config.isCleanupWorkspaceCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new JavadocChecker((config.isJavadocCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new ArtifactChecker((config.isArtifactCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new NullSCMChecker((config.isNullSCMCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new PollingSCMTriggerChecker((config.isPollingSCMTriggerCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new GitShallowChecker((config.isGitShallowCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new MultibranchJobTypeChecker((config.isMultibranchJobTypeCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new HardcodedScriptChecker((config.isHardcodedScriptCheckerEnabled() && config.isGlobalEnabled()),
                                                    config.getHardcodedScriptThreshold(),
                                                    config.isHardcodedScriptIgnoredComment()));
        checkList.add(new GradleWrapperChecker((config.isGradleWrapperCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new TimeoutChecker((config.isTimeoutCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new GroovySystemExitChecker((config.isGroovySystemExitCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new GitRefChecker((config.isGitRefCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new TimerTriggerChecker((config.isTimerTriggerCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new GitRefSubmoduleChecker((config.isGitRefSubmoduleCheckerEnabled() && config.isGlobalEnabled())));
        checkList.add(new BFAChecker((config.isBfaCheckerEnabled() && config.isGlobalEnabled())));
    }

    protected void reloadSlaveCheckList() {
        slaveCheckList.clear();

        JenkinsLintGlobalConfiguration config = JenkinsLintGlobalConfiguration.get();
        slaveCheckList.add(new SlaveDescriptionChecker((config.isSlaveDescriptionCheckerEnabled() && config.isGlobalEnabled())));
        slaveCheckList.add(new SlaveVersionChecker((config.isSlaveVersionCheckerEnabled() && config.isGlobalEnabled())));
        slaveCheckList.add(new SlaveLabelChecker((config.isSlaveLabelCheckerEnabled() && config.isGlobalEnabled())));
        slaveCheckList.add(new WindowsSlaveLaunchChecker((config.isWindowsSlaveLaunchCheckerEnabled() && config.isGlobalEnabled())));
    }
}
