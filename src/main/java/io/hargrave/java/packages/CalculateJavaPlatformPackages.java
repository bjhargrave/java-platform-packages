package io.hargrave.java.packages;

import static java.util.stream.Collectors.joining;

import java.lang.module.ModuleDescriptor;
import java.util.stream.Stream;

public class CalculateJavaPlatformPackages {
	public static void main(String[] args) {
		System.out.println(packagesProperty(calculateVMPackages()));
	}

	public static String packagesProperty(Stream<String> packages) {
		return packages.collect(joining(",\\\n ", "platform.packages = \\\n ", "\n"));
	}

	public static Stream<String> calculateVMPackages() {
		return ModuleLayer.boot()
			.modules()
			.stream()
			.map(Module::getDescriptor)
			.flatMap(descriptor -> descriptor.isAutomatic() ? descriptor.packages()
				.stream()
				: descriptor.exports()
					.stream()
					.filter(e -> !e.isQualified())
					.map(ModuleDescriptor.Exports::source))
			.sorted()
			.distinct();
	}
}
