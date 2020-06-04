package io.hargrave.java.packages;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.SortedMap;
import java.util.SortedSet;

import org.junit.Test;

public class AppTest {
	@Test
	public void testApp() {
		SortedMap<String, SortedSet<String>> moduleExports = CalculateJavaPlatformPackages.modulesExports();
		assertThat(moduleExports).isNotEmpty();
		System.out.println(CalculateJavaPlatformPackages.packagesProperty(moduleExports));
		System.out.println();
		System.out.println(CalculateJavaPlatformPackages.modulesProperty(moduleExports));
	}
}
