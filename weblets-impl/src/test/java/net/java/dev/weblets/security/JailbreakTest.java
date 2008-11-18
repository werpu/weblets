package net.java.dev.weblets.security;

import junit.framework.TestCase;
import net.java.dev.weblets.impl.misc.SandboxGuard;

public class JailbreakTest extends TestCase {

    String resourceRoot = "/resources/";
    String jailbreak1 = "/resources/..";
    String jailbreak2 = "/resources/../boogaloo/../..";
    String jailbreak3 = "/resources/../../../bla";

    String nojailbreak1 = "/resources/dummy/../bla";
    String nojailbreak2 = "/./resources/./dummy/./././../bla";

    /**
     * security test for a url induced resource jailbreak!
     */
    public void testJailbreak1() {
        assertTrue(SandboxGuard.isJailBreak(jailbreak1));
    }

    public void testJailbreak2() {
        assertTrue(SandboxGuard.isJailBreak(jailbreak2));
    }

    public void testJailbreak3() {
        assertTrue(SandboxGuard.isJailBreak(jailbreak3));
    }

    public void testJailbreak4() {
        assertFalse(SandboxGuard.isJailBreak(nojailbreak1));
    }

    public void testJailbreak5() {
        assertFalse(SandboxGuard.isJailBreak(nojailbreak2));
    }
}
