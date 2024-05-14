package io.devpl.backend.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring上下文获取
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    private static final ContextHolder HOLDER = new ContextHolder();

    private static class ContextHolder {
        private ApplicationContext context;

        public void setApplicationContext(ApplicationContext context) {
            this.context = context;
        }

        public ApplicationContext getContext() {
            return this.context;
        }
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        HOLDER.setApplicationContext(applicationContext);
    }

    public static ApplicationContext getContext() {
        return HOLDER.getContext();
    }

    /**
     * 通过name获取Bean
     *
     * @param name bean名称
     * @return bean实例
     */
    public static Object getBean(String name) {
        return getContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz bean类型
     * @param <T>   bean类型
     * @return bean实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return getContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name  bean名称
     * @param clazz bean类型
     * @param <T>   bean类型
     * @return bean实例
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getContext().getBean(name, clazz);
    }
}
