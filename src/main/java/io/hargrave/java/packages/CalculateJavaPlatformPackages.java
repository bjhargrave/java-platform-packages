package io.hargrave.java.packages;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.lang.module.ModuleDescriptor;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

public class CalculateJavaPlatformPackages {
	public static void main(String[] args) {
		Map<String, Set<String>> moduleExports = modulesExports();
		System.out.println(packagesProperty(moduleExports));
		System.out.println();
		System.out.println(modulesProperty(moduleExports));
	}

	public static String packagesProperty(Map<String, Set<String>> moduleExports) {
		return moduleExports.values()
			.stream()
			.flatMap(Collection<String>::stream)
			.sorted()
			.distinct()
			.collect(joining(",\\\n ", "org.osgi.framework.system.packages = \\\n ", ""));
	}

	public static String modulesProperty(Map<String, Set<String>> moduleExports) {
		return moduleExports.entrySet()
			.stream()
			.filter(entry -> !entry.getValue()
				.isEmpty())
			.sorted(Entry.comparingByKey())
			.map(entry -> entry.getValue()
				.stream()
				.sorted()
				.collect(joining(",\\\n   ", " " + entry.getKey() + ";\\\n  exports:List<String>='\\\n   ", "'")))
			.collect(joining(",\\\n", "jpms.modules = \\\n", ""));
	}

	public static Map<String, Set<String>> modulesExports() {
		Map<String, Set<String>> moduleExports = ModuleLayer.boot()
			.modules()
			.stream()
			.map(Module::getDescriptor)
			.collect(toMap(ModuleDescriptor::name, CalculateJavaPlatformPackages::moduleExports,
				CalculateJavaPlatformPackages::merger));
		return moduleExports;
	}

	private static Set<String> moduleExports(ModuleDescriptor descriptor) {
		Stream<String> stream = descriptor.isAutomatic() ? descriptor.packages()
			.stream()
			: descriptor.exports()
				.stream()
				.filter(e -> !e.isQualified())
				.map(ModuleDescriptor.Exports::source);
		return stream.collect(toSet());
	}

	private static <E, SET extends Set<E>> SET merger(SET left, SET right) {
		left.addAll(right);
		return left;
	}

}
