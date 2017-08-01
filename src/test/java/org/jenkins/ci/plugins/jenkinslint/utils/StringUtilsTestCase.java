package org.jenkins.ci.plugins.jenkinslint.utils;

import org.junit.Test;
import org.jvnet.hudson.test.WithoutJenkins;
import org.jenkins.ci.plugins.jenkinslint.utils.StringUtils;
import java.lang.NullPointerException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * StringUtils Test Case.
 *
 * @author Victor Martinez
 */
public class StringUtilsTestCase {

    @WithoutJenkins
    @Test public void testIsAt() {
        assertTrue(StringUtils.isAt("@bob"));
        assertTrue(StringUtils.isAt("    @bob"));
        assertFalse(StringUtils.isAt("bob@"));
    }
    @WithoutJenkins
    @Test(expected = NullPointerException.class)
    public void testIsAtNull() throws Exception {
        StringUtils.isAt(null);
    }
    @WithoutJenkins
    @Test public void testIsH() {
        assertTrue(StringUtils.isH("H bob"));
        assertTrue(StringUtils.isH("h bob"));
        assertTrue(StringUtils.isH("    h bob"));
        assertTrue(StringUtils.isH("    H bob"));
        assertFalse(StringUtils.isH("bobH"));
    }
    @WithoutJenkins
    @Test(expected = NullPointerException.class)
    public void testIsHNull() throws Exception {
        StringUtils.isH(null);
    }
    @WithoutJenkins
    @Test public void testIsComment() {
        assertTrue(StringUtils.isComment("# bob"));
        assertTrue(StringUtils.isComment("    # bob"));
        assertFalse(StringUtils.isComment("bob # bob"));
    }
    @WithoutJenkins
    @Test(expected = NullPointerException.class)
    public void testIsCommentNull() throws Exception {
        StringUtils.isComment(null);
    }
    @WithoutJenkins
    @Test public void testIsEmptyOrBlank() {
        assertTrue(StringUtils.isEmptyOrBlank(""));
        assertTrue(StringUtils.isEmptyOrBlank("     "));
        assertTrue(StringUtils.isEmptyOrBlank("\n\n"));
        assertTrue(StringUtils.isEmptyOrBlank("\n   \n"));
        assertFalse(StringUtils.isEmptyOrBlank("bob"));
    }
    @WithoutJenkins
    @Test(expected = NullPointerException.class)
    public void testIsEmptyOrBlankNull() throws Exception {
        StringUtils.isEmptyOrBlank(null);
    }
}
