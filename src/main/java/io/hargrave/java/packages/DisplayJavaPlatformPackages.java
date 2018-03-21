package io.hargrave.java.packages;

import java.lang.module.ModuleDescriptor;
import java.util.Set;
import java.util.stream.Collectors;

public class DisplayJavaPlatformPackages {
    public static void main(String[] args) {
		System.out.println(new DisplayJavaPlatformPackages().calculateVMPackages());
    }

	String calculateVMPackages() {
		String packages = ModuleLayer.boot()
			.modules()
			.stream()
			.map(Module::getDescriptor)
			.map(ModuleDescriptor::exports)
			.flatMap(Set::stream)
			.filter(e -> e.targets()
				.isEmpty())
			.map(ModuleDescriptor.Exports::source)
			.sorted()
			.distinct()
			.collect(Collectors.joining(",\\\n ", "platform.packages = \\\n ", "\n"));
		return packages;
	}
}
