package org.jenkins.ci.plugins.jenkinslint;

import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.jenkins.ci.plugins.jenkinslint.check.HardcodedScriptChecker;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.servlet.ServletException;
import java.io.IOException;

@Extension
public final class JenkinsLintGlobalConfiguration extends GlobalConfiguration {

    private boolean globalEnabled = true;
    private boolean jobActionEnabled = true;
    private boolean lintDisabledJobEnabled = true;

    private boolean artifactCheckerEnabled = true;
    private boolean bfaCheckerEnabled = true;
    private boolean cleanupWorkspaceCheckerEnabled = true;
    private boolean gitRefCheckerEnabled = true;
    private boolean gitRefSubmoduleCheckerEnabled = true;
    private boolean gitShallowCheckerEnabled = true;
    private boolean gradleWrapperCheckerEnabled = true;
    private boolean groovySystemExitCheckerEnabled = true;
    private boolean hardcodedScriptCheckerEnabled = true;
    private boolean javadocCheckerEnabled = true;
    private boolean jobAssignedLabelCheckerEnabled = true;
    private boolean jobDescriptionCheckerEnabled = true;
    private boolean jobLogRotatorCheckerEnabled = true;
    private boolean jobNameCheckerEnabled = true;
    private boolean masterLabelCheckerEnabled = true;
    private boolean mavenJobTypeCheckerEnabled = true;
    private boolean multibranchJobTypeCheckerEnabled = true;
    private boolean nullSCMCheckerEnabled = true;
    private boolean pollingSCMTriggerCheckerEnabled = true;
    private boolean slaveDescriptionCheckerEnabled = true;
    private boolean slaveLabelCheckerEnabled = true;
    private boolean slaveVersionCheckerEnabled = true;
    private boolean timeoutCheckerEnabled = true;
    private boolean timerTriggerCheckerEnabled = true;
    private boolean windowsSlaveLaunchCheckerEnabled = true;

    private boolean groovySandboxCheckerEnabled = true;

    private int hardcodedScriptThreshold = HardcodedScriptChecker.THRESHOLD;
    private boolean hardcodedScriptIgnoredComment = false;


    public JenkinsLintGlobalConfiguration() {
        load();
    }

    public static JenkinsLintGlobalConfiguration get() {
        return Jenkins.getInstance().getInjector().getInstance(JenkinsLintGlobalConfiguration.class);
    }

    public boolean isArtifactCheckerEnabled() {
        return artifactCheckerEnabled;
    }

    public void setArtifactCheckerEnabled(boolean artifactCheckerEnabled) {
        this.artifactCheckerEnabled = artifactCheckerEnabled;
    }

    @Override
    public String getDisplayName() {
        return Messages.DisplayName();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this, json.getJSONObject("jenkinslint"));
        req.bindJSON(this, json.getJSONObject("jenkinslint-checkers"));

        save();
        return true;
    }

    public boolean isBfaCheckerEnabled() {
        return get().bfaCheckerEnabled;
    }

    public void setBfaCheckerEnabled(boolean bfaCheckerEnabled) {
        this.bfaCheckerEnabled = bfaCheckerEnabled;
    }

    public boolean isCleanupWorkspaceCheckerEnabled() {
        return get().cleanupWorkspaceCheckerEnabled;
    }

    public void setCleanupWorkspaceCheckerEnabled(boolean cleanupWorkspaceCheckerEnabled) {
        this.cleanupWorkspaceCheckerEnabled = cleanupWorkspaceCheckerEnabled;
    }

    public boolean isGitRefCheckerEnabled() {
        return get().gitRefCheckerEnabled;
    }

    public void setGitRefCheckerEnabled(boolean gitRefCheckerEnabled) {
        this.gitRefCheckerEnabled = gitRefCheckerEnabled;
    }

    public boolean isGitRefSubmoduleCheckerEnabled() {
        return get().gitRefSubmoduleCheckerEnabled;
    }

    public void setGitRefSubmoduleCheckerEnabled(boolean gitRefSubmoduleCheckerEnabled) {
        this.gitRefSubmoduleCheckerEnabled = gitRefSubmoduleCheckerEnabled;
    }

    public boolean isGitShallowCheckerEnabled() {
        return get().gitShallowCheckerEnabled;
    }

    public void setGitShallowCheckerEnabled(boolean gitShallowCheckerEnabled) {
        this.gitShallowCheckerEnabled = gitShallowCheckerEnabled;
    }

    public boolean isGradleWrapperCheckerEnabled() {
        return get().gradleWrapperCheckerEnabled;
    }

    public void setGradleWrapperCheckerEnabled(boolean gradleWrapperCheckerEnabled) {
        this.gradleWrapperCheckerEnabled = gradleWrapperCheckerEnabled;
    }

    public boolean isGroovySystemExitCheckerEnabled() {
        return get().groovySystemExitCheckerEnabled;
    }

    public void setGroovySystemExitCheckerEnabled(boolean groovySystemExitCheckerEnabled) {
        this.groovySystemExitCheckerEnabled = groovySystemExitCheckerEnabled;
    }

    public boolean isHardcodedScriptCheckerEnabled() {
        return get().hardcodedScriptCheckerEnabled;
    }

    public void setHardcodedScriptCheckerEnabled(boolean hardcodedScriptCheckerEnabled) {
        this.hardcodedScriptCheckerEnabled = hardcodedScriptCheckerEnabled;
    }

    public boolean isJavadocCheckerEnabled() {
        return get().javadocCheckerEnabled;
    }

    public void setJavadocCheckerEnabled(boolean javadocCheckerEnabled) {
        this.javadocCheckerEnabled = javadocCheckerEnabled;
    }

    public boolean isJobAssignedLabelCheckerEnabled() {
        return get().jobAssignedLabelCheckerEnabled;
    }

    public void setJobAssignedLabelCheckerEnabled(boolean jobAssignedLabelCheckerEnabled) {
        this.jobAssignedLabelCheckerEnabled = jobAssignedLabelCheckerEnabled;
    }

    public boolean isJobDescriptionCheckerEnabled() {
        return get().jobDescriptionCheckerEnabled;
    }

    public void setJobDescriptionCheckerEnabled(boolean jobDescriptionCheckerEnabled) {
        this.jobDescriptionCheckerEnabled = jobDescriptionCheckerEnabled;
    }

    public boolean isJobLogRotatorCheckerEnabled() {
        return get().jobLogRotatorCheckerEnabled;
    }

    public void setJobLogRotatorCheckerEnabled(boolean jobLogRotatorCheckerEnabled) {
        this.jobLogRotatorCheckerEnabled = jobLogRotatorCheckerEnabled;
    }

    public boolean isJobNameCheckerEnabled() {
        return get().jobNameCheckerEnabled;
    }

    public void setJobNameCheckerEnabled(boolean jobNameCheckerEnabled) {
        this.jobNameCheckerEnabled = jobNameCheckerEnabled;
    }

    public boolean isMasterLabelCheckerEnabled() {
        return get().masterLabelCheckerEnabled;
    }

    public void setMasterLabelCheckerEnabled(boolean masterLabelCheckerEnabled) {
        this.masterLabelCheckerEnabled = masterLabelCheckerEnabled;
    }

    public boolean isMavenJobTypeCheckerEnabled() {
        return get().mavenJobTypeCheckerEnabled;
    }

    public void setMavenJobTypeCheckerEnabled(boolean mavenJobTypeCheckerEnabled) {
        this.mavenJobTypeCheckerEnabled = mavenJobTypeCheckerEnabled;
    }

    public boolean isMultibranchJobTypeCheckerEnabled() {
        return get().multibranchJobTypeCheckerEnabled;
    }

    public void setMultibranchJobTypeCheckerEnabled(boolean multibranchJobTypeCheckerEnabled) {
        this.multibranchJobTypeCheckerEnabled = multibranchJobTypeCheckerEnabled;
    }

    public boolean isNullSCMCheckerEnabled() {
        return get().nullSCMCheckerEnabled;
    }

    public void setNullSCMCheckerEnabled(boolean nullSCMCheckerEnabled) {
        this.nullSCMCheckerEnabled = nullSCMCheckerEnabled;
    }

    public boolean isPollingSCMTriggerCheckerEnabled() {
        return get().pollingSCMTriggerCheckerEnabled;
    }

    public void setPollingSCMTriggerCheckerEnabled(boolean pollingSCMTriggerCheckerEnabled) {
        this.pollingSCMTriggerCheckerEnabled = pollingSCMTriggerCheckerEnabled;
    }

    public boolean isSlaveDescriptionCheckerEnabled() {
        return get().slaveDescriptionCheckerEnabled;
    }

    public void setSlaveDescriptionCheckerEnabled(boolean slaveDescriptionCheckerEnabled) {
        this.slaveDescriptionCheckerEnabled = slaveDescriptionCheckerEnabled;
    }

    public boolean isSlaveLabelCheckerEnabled() {
        return get().slaveLabelCheckerEnabled;
    }

    public void setSlaveLabelCheckerEnabled(boolean slaveLabelCheckerEnabled) {
        this.slaveLabelCheckerEnabled = slaveLabelCheckerEnabled;
    }

    public boolean isSlaveVersionCheckerEnabled() {
        return get().slaveVersionCheckerEnabled;
    }

    public void setSlaveVersionCheckerEnabled(boolean slaveVersionCheckerEnabled) {
        this.slaveVersionCheckerEnabled = slaveVersionCheckerEnabled;
    }

    public boolean isTimeoutCheckerEnabled() {
        return get().timeoutCheckerEnabled;
    }

    public void setTimeoutCheckerEnabled(boolean timeoutCheckerEnabled) {
        this.timeoutCheckerEnabled = timeoutCheckerEnabled;
    }

    public boolean isTimerTriggerCheckerEnabled() {
        return get().timerTriggerCheckerEnabled;
    }

    public void setTimerTriggerCheckerEnabled(boolean timerTriggerCheckerEnabled) {
        this.timerTriggerCheckerEnabled = timerTriggerCheckerEnabled;
    }

    public boolean isWindowsSlaveLaunchCheckerEnabled() {
        return get().windowsSlaveLaunchCheckerEnabled;
    }

    public void setWindowsSlaveLaunchCheckerEnabled(boolean windowsSlaveLaunchCheckerEnabled) {
        this.windowsSlaveLaunchCheckerEnabled = windowsSlaveLaunchCheckerEnabled;
    }

    public boolean isGlobalEnabled() {
        return get().globalEnabled;
    }

    public void setGlobalEnabled(boolean globalEnabled) {
        this.globalEnabled = globalEnabled;
    }

    public boolean isJobActionEnabled() {
        return get().jobActionEnabled;
    }

    public void setJobActionEnabled(boolean jobActionEnabled) {
        this.jobActionEnabled = jobActionEnabled;
    }

    public int getHardcodedScriptThreshold() {
        return hardcodedScriptThreshold;
    }

    public void setHardcodedScriptThreshold(int hardcodedScriptThreshold) {
        this.hardcodedScriptThreshold = hardcodedScriptThreshold;
    }

    public boolean isHardcodedScriptIgnoredComment() {
        return hardcodedScriptIgnoredComment;
    }

    public void setHardcodedScriptIgnoredComment(boolean hardcodedScriptIgnoredComment) {
        this.hardcodedScriptIgnoredComment = hardcodedScriptIgnoredComment;
    }

    public boolean isGroovySandboxCheckerEnabled() {
        return groovySandboxCheckerEnabled;
    }

    public void setWorkflowSandboxCheckerEnabled(boolean groovySandboxCheckerEnabled) {
        this.groovySandboxCheckerEnabled = groovySandboxCheckerEnabled;
    }

    public boolean isLintDisabledJobEnabled() {
        return lintDisabledJobEnabled;
    }

    public void setLintDisabledJobEnabled(boolean lintDisabledJobEnabled) {
        this.lintDisabledJobEnabled = lintDisabledJobEnabled;
    }

    /**
     * Performs on-the-fly validation of the form field 'name'.
     *
     * @param value
     *      This parameter receives the value that the user has typed.
     * @return
     *      Indicates the outcome of the validation. This is sent to the browser.
     *      <p>
     *      Note that returning {@link FormValidation#error(String)} does not
     *      prevent the form from being saved. It just means that a message
     *      will be displayed to the user.
     */
    public FormValidation doCheckHardcodedScriptThreshold(@QueryParameter int value)
            throws IOException, ServletException {
        if (value <= 1)
            return FormValidation.error("Please set a value greater than 1");
        return FormValidation.ok();
    }

}
