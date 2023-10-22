package io.devpl.generator.controller;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

public class ExtensionLoader<C> {

    public C LoadClass(String directory, String classpath, Class<C> parentClass) throws ClassNotFoundException {
        File pluginsDir = new File(System.getProperty("user.dir") + directory);
        for (File jar : Objects.requireNonNull(pluginsDir.listFiles())) {
            try {
                ClassLoader loader = URLClassLoader.newInstance(new URL[]{jar.toURL()},
                    getClass().getClassLoader()
                );
                Class<?> clazz = Class.forName(classpath, true, loader);
                Class<? extends C> newClass = clazz.asSubclass(parentClass);
                // Apparently it's bad to use Class.newInstance, so we use
                // newClass.getConstructor() instead
                Constructor<? extends C> constructor = newClass.getConstructor();
                return constructor.newInstance();

            } catch (ClassNotFoundException e) {
                // There might be multiple JARs in the directory,
                // so keep looking
                continue;
            } catch (MalformedURLException | NoSuchMethodException | InvocationTargetException |
                     InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new ClassNotFoundException("Class " + classpath
            + " wasn't found in directory " + System.getProperty("user.dir") + directory);
    }
}
