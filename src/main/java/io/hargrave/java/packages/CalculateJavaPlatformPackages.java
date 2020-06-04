package io.hargrave.java.packages;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;

import java.lang.module.ModuleDescriptor;
import java.util.Collection;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

public class CalculateJavaPlatformPackages {
	public static void main(String[] args) {
		SortedMap<String, SortedSet<String>> moduleExports = modulesExports();
		System.out.println(packagesProperty(moduleExports));
		System.out.println();
		System.out.println(modulesProperty(moduleExports));
	}

	public static String packagesProperty(SortedMap<String, SortedSet<String>> moduleExports) {
		return moduleExports.values()
			.stream()
			.flatMap(Collection<String>::stream)
			.sorted()
			.distinct()
			.collect(joining(",\\\n ", "org.osgi.framework.system.packages = \\\n ", ""));
	}

	public static String modulesProperty(SortedMap<String, SortedSet<String>> moduleExports) {
		return moduleExports.entrySet()
			.stream()
			.filter(entry -> !entry.getValue()
				.isEmpty())
			.map(entry -> entry.getValue()
				.stream()
				.collect(joining(",\\\n   ", " " + entry.getKey() + ";\\\n  exports:List<String>='\\\n   ", "'")))
			.collect(joining(",\\\n", "jpms.modules = \\\n", ""));
	}

	public static SortedMap<String, SortedSet<String>> modulesExports() {
		SortedMap<String, SortedSet<String>> moduleExports = ModuleLayer.boot()
			.modules()
			.stream()
			.map(Module::getDescriptor)
			.collect(toMap(ModuleDescriptor::name, CalculateJavaPlatformPackages::moduleExports,
				CalculateJavaPlatformPackages::merger, TreeMap<String, SortedSet<String>>::new));
		return moduleExports;
	}

	private static SortedSet<String> moduleExports(ModuleDescriptor descriptor) {
		Stream<String> stream = descriptor.isAutomatic() ? descriptor.packages()
			.stream()
			: descriptor.exports()
				.stream()
				.filter(e -> !e.isQualified())
				.map(ModuleDescriptor.Exports::source);
		return stream.collect(toCollection(TreeSet<String>::new));
	}

	private static <E, SET extends Set<E>> SET merger(SET left, SET right) {
		left.addAll(right);
		return left;
	}

}
