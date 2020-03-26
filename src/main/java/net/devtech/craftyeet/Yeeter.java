package net.devtech.craftyeet;

import org.objectweb.asm.tree.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Yeeter {
	static ZipFile minecraft;
	public static void main(String[] args) throws IOException {
		File craftbukkit = new File(args[0]);
		File minecraft = new File(args[1]);
		Yeeter.minecraft = new ZipFile(minecraft);
		MCMap.init();
		ZipFile file = new ZipFile(craftbukkit);
		Set<String> fields = new HashSet<>();
		Set<String> methods = new HashSet<>();
		mod(file, fields, methods);
		write(fields, "fields.txt");
		write(methods, "methods.txt");
	}

	private static void write(Set<String> strings, String file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (String string : strings) {
			writer.write(string);
			writer.write('\n');
		}
		writer.close();
	}

	private static void mod(ZipFile file, Set<String> fields, Set<String> methods) {
		Enumeration<? extends ZipEntry> enumeration = file.entries();
		Set<String> rejectedField = new HashSet<>();
		Set<String> rejectedMethod = new HashSet<>();
		while (enumeration.hasMoreElements()) {
			ZipEntry zipEntry = enumeration.nextElement();
			if(zipEntry.getName().endsWith(".class")) { // class file
				ClassNode craftBukkitNode = MCMap.from(file, zipEntry);
				for (MethodNode method : craftBukkitNode.methods) {
					for (int i = 0; i < method.instructions.size(); i++) {
						AbstractInsnNode instruction = method.instructions.get(i);
						if(instruction instanceof FieldInsnNode) {
							FieldInsnNode node = (FieldInsnNode) instruction;
							String name = toString(node);
							if (!rejectedField.contains(name) && !fields.contains(name)) {
								if(Corrector.widen(node))
									fields.add(name);
								else
									rejectedField.add(name);
							}
						} else if(instruction instanceof MethodInsnNode) {
							MethodInsnNode node = (MethodInsnNode) instruction;
							String name = toString(node);
							if (!rejectedMethod.contains(name) && !methods.contains(name)) {
								if(Corrector.widen(node))
									methods.add(name);
								else
									rejectedMethod.add(name);
							}
						}
					}
				}
			}
			// put in new
		}
	}

	public static String toString(FieldInsnNode node) {
		return node.owner+";"+node.name+";"+node.desc;
	}

	public static String toString(MethodInsnNode node) {
		return node.owner+";"+node.name+";"+node.desc;
	}
}
