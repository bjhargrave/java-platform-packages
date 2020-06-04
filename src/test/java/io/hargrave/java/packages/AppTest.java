package io.hargrave.java.packages;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AppTest {
	@Test
	public void testApp() {
		Map<String, Set<String>> moduleExports = CalculateJavaPlatformPackages.modulesExports();
		assertThat(moduleExports).isNotEmpty();
		System.out.println(CalculateJavaPlatformPackages.packagesProperty(moduleExports));
		System.out.println();
		System.out.println(CalculateJavaPlatformPackages.modulesProperty(moduleExports));
	}
}
