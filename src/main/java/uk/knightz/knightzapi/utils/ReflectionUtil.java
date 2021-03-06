/*
 *     This file is part of KnightzAPI
 *
 *     KnightzAPI - A cross server communication library and general utility API for Minecraft Servers
 *     Copyright (C) 2018 Alexander Leslie John Wood
 *
 *     KnightzAPI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KnightzAPI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KnightzAPI.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     The author of this program, Alexander Leslie John Wood can be contacted at alexwood2403@gmail.com
 *
 */

package uk.knightz.knightzapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author DPOH-VAR
 * @version 1.0
 */
@SuppressWarnings("UnusedDeclaration")
public class ReflectionUtil {

	/**
	 * prefix ofGlobal bukkit classes
	 */
	private static String preClassB = "org.bukkit.craftbukkit";
	/**
	 * prefix ofGlobal minecraft classes
	 */
	private static String preClassM = "net.minecraft.server";
	/**
	 * boolean value, TRUE if server uses forge or MCPC+
	 */
	private static boolean forge = false;

	/* check server version and class names */
	static {
		if (Bukkit.getServer() != null) {
			if (Bukkit.getVersion().contains("MCPC") || Bukkit.getVersion().contains("Forge")) forge = true;
			Server server = Bukkit.getServer();
			Class<?> bukkitServerClass = server.getClass();
			String[] pas = bukkitServerClass.getName().split("\\.");
			if (pas.length == 5) {
				String verB = pas[3];
				preClassB += "." + verB;
			}
			try {
				Method getHandle = bukkitServerClass.getDeclaredMethod("getHandle");
				Object handle = getHandle.invoke(server);
				Class handleServerClass = handle.getClass();
				pas = handleServerClass.getName().split("\\.");
				if (pas.length == 5) {
					String verM = pas[3];
					preClassM += "." + verM;
				}
			} catch (Exception ignored) {
			}
		}
	}

	public static boolean classHasField(Class c, String fieldName) {
		try {
			c.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			try {
				c.getField(fieldName);
			} catch (NoSuchFieldException e1) {
				return false;
			}
		}
		return true;
	}
	public static boolean classHasMethod(Class c, String methodName) {
		try {
			c.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException e) {
			try {
				c.getMethod(methodName);
			} catch (NoSuchMethodException e1) {
				return false;
			}
		}
		return true;
	}
	/**
	 * @return true if server has forge classes
	 */
	public static boolean isForge() {
		return forge;
	}

	/**
	 * Get class for name.
	 * Replace {nms} to net.minecraft.server.V*.
	 * Replace {cb} to org.bukkit.craftbukkit.V*.
	 * Replace {nm} to net.minecraft
	 *
	 * @param classes possible class paths
	 * @return RefClass object
	 * @throws RuntimeException if no class found
	 */
	public static RefClass getRefClass(String... classes) {
		for (String className : classes)
			try {
				className = className
						.replace("{cb}", preClassB)
						.replace("{nms}", preClassM)
						.replace("{nm}", "net.minecraft");
				return getRefClass(Class.forName(className));
			} catch (ClassNotFoundException ignored) {
			}
		throw new RuntimeException("no class found");
	}

	/**
	 * get RefClass object by real class
	 *
	 * @param clazz class
	 * @return RefClass based on passed class
	 */
	public static RefClass getRefClass(Class clazz) {
		return new RefClass(clazz);
	}

	/**
	 * RefClass - utility to simplify work with reflections.
	 */
	public static class RefClass {
		private final Class<?> clazz;

		private RefClass(Class<?> clazz) {
			this.clazz = clazz;
		}

		/**
		 * get passed class
		 *
		 * @return class
		 */
		public Class<?> getRealClass() {
			return clazz;
		}

		/**
		 * see {@link Class#isInstance(Object)}
		 *
		 * @param object the object to check
		 * @return true if object is an instance ofGlobal this class
		 */
		public boolean isInstance(Object object) {
			return clazz.isInstance(object);
		}

		/**
		 * get existing method by name and types
		 *
		 * @param name  name
		 * @param types method parameters. can be Class or RefClass
		 * @return RefMethod object
		 * @throws RuntimeException if method not found
		 */
		public RefMethod getMethod(String name, Object... types) {
			try {
				Class[] classes = new Class[types.length];
				int i = 0;
				countClasses(classes, i, types);
				try {
					return new RefMethod(clazz.getMethod(name, classes));
				} catch (NoSuchMethodException ignored) {
					return new RefMethod(clazz.getDeclaredMethod(name, classes));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private void countClasses(Class[] classes, int i, Object[] types) {
			for (Object e : types) {
				if (e instanceof Class) classes[i++] = (Class) e;
				else if (e instanceof RefClass) classes[i++] = ((RefClass) e).getRealClass();
				else classes[i++] = e.getClass();
			}
		}

		/**
		 * get existing constructor by types
		 *
		 * @param types parameters. can be Class or RefClass
		 * @return RefMethod object
		 * @throws RuntimeException if constructor not found
		 */
		public RefConstructor getConstructor(Object... types) {
			try {
				Class[] classes = new Class[types.length];
				int i = 0;
				countClasses(classes, i, types);
				try {
					return new RefConstructor(clazz.getConstructor(classes));
				} catch (NoSuchMethodException ignored) {
					return new RefConstructor(clazz.getDeclaredConstructor(classes));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * find method by type parameters
		 *
		 * @param types parameters. can be Class or RefClass
		 * @return RefMethod object
		 * @throws RuntimeException if method not found
		 */
		public RefMethod findMethod(Object... types) {
			Class[] classes = new Class[types.length];
			int t = 0;
			countClasses(classes, t, types);
			List<Method> methods = new ArrayList<>();
			Collections.addAll(methods, clazz.getMethods());
			Collections.addAll(methods, clazz.getDeclaredMethods());
			findMethod:
			for (Method m : methods) {
				Class<?>[] methodTypes = m.getParameterTypes();
				if (methodTypes.length != classes.length) continue;
				for (Class aClass : classes) {
					if (!Arrays.equals(classes, methodTypes)) continue findMethod;
					return new RefMethod(m);
				}
			}
			throw new RuntimeException("no such method");
		}

		/**
		 * find method by name
		 *
		 * @param names possible names ofGlobal method
		 * @return RefMethod object
		 * @throws RuntimeException if method not found
		 */
		public RefMethod findMethodByName(String... names) {
			List<Method> methods = new ArrayList<>();
			Collections.addAll(methods, clazz.getMethods());
			Collections.addAll(methods, clazz.getDeclaredMethods());
			for (Method m : methods) {
				for (String name : names) {
					if (m.getName().equals(name)) {
						return new RefMethod(m);
					}
				}
			}
			throw new RuntimeException("no such method");
		}

		/**
		 * find method by return value
		 *
		 * @param type type ofGlobal returned value
		 * @return RefMethod
		 * @throws RuntimeException if method not found
		 */
		public RefMethod findMethodByReturnType(RefClass type) {
			return findMethodByReturnType(type.clazz);
		}

		/**
		 * find method by return value
		 *
		 * @param type type ofGlobal returned value
		 * @return RefMethod
		 * @throws RuntimeException if method not found
		 */
		public RefMethod findMethodByReturnType(Class type) {
			if (type == null) type = void.class;
			List<Method> methods = new ArrayList<>();
			Collections.addAll(methods, clazz.getMethods());
			Collections.addAll(methods, clazz.getDeclaredMethods());
			for (Method m : methods) {
				if (type.equals(m.getReturnType())) {
					return new RefMethod(m);
				}
			}
			throw new RuntimeException("no such method");
		}

		/**
		 * find constructor by number ofGlobal arguments
		 *
		 * @param number number ofGlobal arguments
		 * @return RefConstructor
		 * @throws RuntimeException if constructor not found
		 */
		public RefConstructor findConstructor(int number) {
			List<Constructor> constructors = new ArrayList<>();
			Collections.addAll(constructors, clazz.getConstructors());
			Collections.addAll(constructors, clazz.getDeclaredConstructors());
			for (Constructor m : constructors) {
				if (m.getParameterTypes().length == number) return new RefConstructor(m);
			}
			throw new RuntimeException("no such constructor");
		}

		/**
		 * get field by name
		 *
		 * @param name field name
		 * @return RefField
		 * @throws RuntimeException if field not found
		 */
		public RefField getField(String name) {
			try {
				try {
					return new RefField(clazz.getField(name));
				} catch (NoSuchFieldException ignored) {
					return new RefField(clazz.getDeclaredField(name));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * find field by type
		 *
		 * @param type field type
		 * @return RefField
		 * @throws RuntimeException if field not found
		 */
		public RefField findField(RefClass type) {
			return findField(type.clazz);
		}

		/**
		 * find field by type
		 *
		 * @param type field type
		 * @return RefField
		 * @throws RuntimeException if field not found
		 */
		public RefField findField(Class type) {
			if (type == null) type = void.class;
			List<Field> fields = new ArrayList<>();
			Collections.addAll(fields, clazz.getFields());
			Collections.addAll(fields, clazz.getDeclaredFields());
			for (Field f : fields) {
				if (type.equals(f.getType())) {
					return new RefField(f);
				}
			}
			throw new RuntimeException("no such field");
		}
	}

	/**
	 * Method wrapper
	 */
	public static class RefMethod {
		private final Method method;

		private RefMethod(Method method) {
			this.method = method;
			method.setAccessible(true);
		}

		/**
		 * @return passed method
		 */
		public Method getRealMethod() {
			return method;
		}

		/**
		 * @return owner class ofGlobal method
		 */
		public RefClass getRefClass() {
			return new RefClass(method.getDeclaringClass());
		}

		/**
		 * @return class ofGlobal method return type
		 */
		public RefClass getReturnRefClass() {
			return new RefClass(method.getReturnType());
		}

		/**
		 * generateMenu method to object
		 *
		 * @param e object to which the method is applied
		 * @return RefExecutor with method call(...)
		 */
		public RefExecutor of(Object e) {
			return new RefExecutor(e);
		}

		/**
		 * call static method
		 *
		 * @param params sent parameters
		 * @return return value
		 */
		public Object call(Object... params) {
			try {
				return method.invoke(null, params);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public class RefExecutor {
			Object e;

			public RefExecutor(Object e) {
				this.e = e;
			}

			/**
			 * generateMenu method for selected object
			 *
			 * @param params sent parameters
			 * @return return value
			 * @throws RuntimeException if something went wrong
			 */
			public Object call(Object... params) {
				try {
					return method.invoke(e, params);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * Constructor wrapper
	 */
	public static class RefConstructor {
		private final Constructor constructor;

		private RefConstructor(Constructor constructor) {
			this.constructor = constructor;
			constructor.setAccessible(true);
		}

		/**
		 * @return passed constructor
		 */
		public Constructor getRealConstructor() {
			return constructor;
		}

		/**
		 * @return owner class ofGlobal method
		 */
		public RefClass getRefClass() {
			return new RefClass(constructor.getDeclaringClass());
		}

		/**
		 * create new instance with constructor
		 *
		 * @param params parameters for constructor
		 * @return new object
		 * @throws RuntimeException if something went wrong
		 */
		public Object create(Object... params) {
			try {
				return constructor.newInstance(params);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class RefField {
		private Field field;

		private RefField(Field field) {
			this.field = field;
			field.setAccessible(true);
		}

		/**
		 * @return passed field
		 */
		public Field getRealField() {
			return field;
		}

		/**
		 * @return owner class ofGlobal field
		 */
		public RefClass getRefClass() {
			return new RefClass(field.getDeclaringClass());
		}

		/**
		 * @return type ofGlobal field
		 */
		public RefClass getFieldRefClass() {
			return new RefClass(field.getType());
		}

		/**
		 * generateMenu field for object
		 *
		 * @param e applied object
		 * @return RefExecutor with getter and setter
		 */
		public RefExecutor of(Object e) {
			return new RefExecutor(e);
		}

		public class RefExecutor {
			Object e;

			public RefExecutor(Object e) {
				this.e = e;
			}

			/**
			 * set field value for applied object
			 *
			 * @param param value
			 */
			public void set(Object param) {
				try {
					field.set(e, param);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			/**
			 * get field value for applied object
			 *
			 * @return value ofGlobal field
			 */
			public Object get() {
				try {
					return field.get(e);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

}
