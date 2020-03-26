package net.devtech.craftyeet;

import org.objectweb.asm.tree.*;
import java.lang.reflect.Modifier;

public class Corrector {
	public static boolean widen(FieldInsnNode node) {
		ClassNode type = MCMap.get(node.owner);
		if(type != null) {
			for (FieldNode field : type.fields) {
				if (!Modifier.isPublic(field.access) && field.name.equals(node.name) && field.desc.equals(node.desc)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean widen(MethodInsnNode node) {
		ClassNode type = MCMap.get(node.owner);
		if(type != null) {
			for (MethodNode method : type.methods) {
				if (!Modifier.isPublic(method.access) && method.name.equals(node.name) && method.desc.equals(node.desc)) {
					return true;
				}
			}
		}
		return false;
	}
}
