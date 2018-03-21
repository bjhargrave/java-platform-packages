package io.hargrave.java.packages;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AppTest {
    @Test public void testAppHasAGreeting() {
        DisplayJavaPlatformPackages classUnderTest = new DisplayJavaPlatformPackages();
		assertNotNull("app should display packages", classUnderTest.calculateVMPackages());
    }
}
