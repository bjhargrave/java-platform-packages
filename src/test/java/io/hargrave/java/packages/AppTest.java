package io.hargrave.java.packages;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

public class AppTest {
	@Test
	public void testApp() {
		List<String> packages = CalculateJavaPlatformPackages.calculateVMPackages()
			.collect(toList());
		assertThat(packages).isNotEmpty();
		System.out.println(CalculateJavaPlatformPackages.packagesProperty(packages.stream()));
	}
}
