package net.java.dev.weblets.security;

import junit.framework.TestCase;
import net.java.dev.weblets.sandbox.SandboxGuard;

/**
 * Created by IntelliJ IDEA.
 * User: werpu
 * Date: 28.03.2008
 * Time: 16:01:53
 * To change this template use File | Settings | File Templates.
 */
public class JailbreakTest  extends TestCase {

            String resourceRoot = "/resources/";
        String jailbreak1 = "/resources/..";
        String jailbreak2 = "/resources/../boogaloo/../..";
        String jailbreak3 = "/resources/../../../bla";

        String nojailbreak1 =  "/resources/dummy/../bla";

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


}
