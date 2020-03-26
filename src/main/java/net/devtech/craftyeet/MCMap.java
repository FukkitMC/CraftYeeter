package net.devtech.craftyeet;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MCMap {
	private static final Map<String, ClassNode> MINECRAFT_CLASSES = new HashMap<>();
	private static final ZipFile MINECRAFT_JAR = Yeeter.minecraft;
	public static ClassNode get(String fullName) {

		ClassNode node = MINECRAFT_CLASSES.get(fullName);
		if(node == null) {
			ZipEntry entry = MINECRAFT_JAR.getEntry(fullName+".class"); // lazy man's thing
			if(entry != null) {
				MINECRAFT_CLASSES.put(fullName, node = from(MINECRAFT_JAR, entry));
			}
		}
		return node;
	}

	public static ClassNode from(ZipFile file, ZipEntry entry) {
		try {
			InputStream stream = file.getInputStream(entry);
			ClassReader reader = new ClassReader(stream);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);
			stream.close();
			return  node;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static void init() {}
}
