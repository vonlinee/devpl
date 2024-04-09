package io.devpl.codegen.generator;

import io.devpl.codegen.generator.config.PluginConfiguration;
import io.devpl.codegen.util.Messages;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class creates the different objects needed by the generator.
 */
public class ObjectFactory {

    private static final List<ClassLoader> externalClassLoaders;

    static {
        externalClassLoaders = new ArrayList<>();
    }

    /**
     * Utility class. No instances allowed.
     */
    private ObjectFactory() {
        super();
    }

    /**
     * Clears the class loaders.  This method should be called at the beginning of
     * a generation run so that and change to the classloading configuration
     * will be reflected.  For example, if the eclipse launcher changes configuration
     * it might not be updated if eclipse hasn't been restarted.
     */
    public static void reset() {
        externalClassLoaders.clear();
    }

    /**
     * Adds a custom classloader to the collection of classloaders searched for "external" classes. These are classes
     * that do not depend on any of the generator's classes or interfaces. Examples are JDBC drivers, root classes, root
     * interfaces, etc.
     *
     * @param classLoader the class loader
     */
    public static synchronized void addExternalClassLoader(ClassLoader classLoader) {
        externalClassLoaders.add(classLoader);
    }

    /**
     * Returns a class loaded from the context classloader, or the classloader supplied by a client. This is
     * appropriate for JDBC drivers, model root classes, etc. It is not appropriate for any class that extends one of
     * the supplied classes or interfaces.
     *
     * @param type the type
     * @return the Class loaded from the external classloader
     * @throws ClassNotFoundException the class not found exception
     */
    public static Class<?> externalClassForName(String type) throws ClassNotFoundException {

        Class<?> clazz;

        for (ClassLoader classLoader : externalClassLoaders) {
            try {
                clazz = Class.forName(type, true, classLoader);
                return clazz;
            } catch (Exception e) {
                // ignore - fail safe below
            }
        }

        return internalClassForName(type);
    }

    public static Object createExternalObject(String type) {
        Object answer;
        try {
            Class<?> clazz = externalClassForName(type);
            answer = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(Messages.getString("RuntimeError.6", type), e);
        }

        return answer;
    }

    public static Class<?> internalClassForName(String type) throws ClassNotFoundException {
        Class<?> clazz = null;

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            clazz = Class.forName(type, true, cl);
        } catch (Exception e) {
            // ignore - failsafe below
        }

        if (clazz == null) {
            clazz = Class.forName(type, true, ObjectFactory.class.getClassLoader());
        }

        return clazz;
    }

    public static URL getResource(String resource) {
        URL url;

        for (ClassLoader classLoader : externalClassLoaders) {
            url = classLoader.getResource(resource);
            if (url != null) {
                return url;
            }
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        url = cl.getResource(resource);

        if (url == null) {
            url = ObjectFactory.class.getClassLoader().getResource(resource);
        }

        return url;
    }

    public static Object createInternalObject(String type) {
        Object answer;
        try {
            Class<?> clazz = internalClassForName(type);
            answer = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(Messages.getString("RuntimeError.6", type), e);
        }
        return answer;
    }

    public static JavaTypeResolver createJavaTypeResolver(Context context, List<String> warnings) {
        JavaTypeResolverConfiguration config = context.getObject(JavaTypeResolverConfiguration.class);
        String type;

        if (config != null && config.getConfigurationType() != null) {
            type = config.getConfigurationType();
            if ("DEFAULT".equalsIgnoreCase(type)) {
                type = JavaTypeResolverDefaultImpl.class.getName();
            }
        } else {
            type = JavaTypeResolverDefaultImpl.class.getName();
        }

        JavaTypeResolver answer = (JavaTypeResolver) createInternalObject(type);
        answer.setWarnings(warnings);
        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }
        answer.setContext(context);
        return answer;
    }

    public static Plugin createPlugin(Context context, PluginConfiguration pluginConfiguration) {
        Plugin plugin = (Plugin) createInternalObject(pluginConfiguration.getConfigurationType());
        plugin.setContext(context);
        plugin.setProperties(pluginConfiguration.getProperties());
        return plugin;
    }
}
