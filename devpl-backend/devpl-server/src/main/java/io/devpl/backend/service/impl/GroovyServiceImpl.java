package io.devpl.backend.service.impl;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import io.devpl.backend.service.GroovyService;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 导致OOM的原因并不止GroovyShell、Script等实例过多
 * 如果脚本中的Java代码也创建了对象或者new了实例，即使销毁了GroovyShell也不会销毁脚本中的对象。
 * 例如下面这个脚本，会创建一个ArrayList对象。这个对象不会随着GroovyShell、Script等实例的消失而消失，所以还是会有问题。
 * <code>
 * def test(){
 * List<String> list = new ArrayList<>();
 * }
 * </code>
 */
@Slf4j
@Service
public class GroovyServiceImpl implements GroovyService {

    /**
     * 缓存Script实例
     * 如果脚本内容修改了的话，需要清空SCRIPT_MAP，重新装载脚本实例
     */
    private final Map<String, Script> SCRIPT_MAP = new HashMap<>();

    private static final GroovyClassLoader CLASS_LOADER = new GroovyClassLoader();

    public Script loadScript(String key, String script) {
        if (SCRIPT_MAP.containsKey(key)) {
            return SCRIPT_MAP.get(key);
        }
        Script scriptInstance = loadScript(script, new Binding());
        SCRIPT_MAP.put(key, scriptInstance);
        return scriptInstance;
    }

    public Script loadScript(String rule, Binding binding) {
        if (rule == null || rule.isEmpty()) {
            return null;
        }
        try {
            Class<?> ruleClazz = CLASS_LOADER.parseClass(rule);
            if (ruleClazz != null) {
                return InvokerHelper.createScript(ruleClazz, binding);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            CLASS_LOADER.clearCache();
        }
        return null;
    }

    @Override
    public void clearCache() {
        CLASS_LOADER.clearCache();
    }
}
