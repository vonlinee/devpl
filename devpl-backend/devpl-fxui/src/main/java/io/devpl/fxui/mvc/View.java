package io.devpl.fxui.mvc;

import io.devpl.sdk.collection.WeakValueHashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * 所有控制器的基类，控制器是单例对象
 */
public abstract class View implements SceneGraphAccessor {

    /**
     * all view controller instances
     */
    private static final WeakValueHashMap<Class<?>, View> viewCache = new WeakValueHashMap<>();

    /**
     * the root node of this view
     */
    private Node root;

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public View() {
    }

    protected void setRoot(Node root) {
        this.root = root;
    }

    /**
     * @return return the root node of this view
     */
    public final Node getRoot() {
        return this.root;
    }

    /**
     * 加载视图，并进行初始化
     * @param clazz View impl Class
     * @param <T>   View
     * @return Root Node
     */
    public static <T> Parent load(Class<T> clazz) {
        return (Parent) loadImpl(clazz).getRoot();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findViewByClass(Class<?> viewClass) {
        return (T) loadImpl(viewClass);
    }

    /**
     * 加载指定类的根节点
     * @param clazz view impl
     * @param <T>   view impl
     * @return View实例
     */
    private static <T> View loadImpl(Class<T> clazz) {
        if (!View.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(String.format("cannot load class View [%s]", clazz));
        }
        View view = viewCache.get(clazz);
        if (view == null) {
            // Fxml类型的节点
            FxmlController fxmlInfo = clazz.getAnnotation(FxmlController.class);
            if (fxmlInfo != null) {
                String fxmlLocation = fxmlInfo.location();
                if (fxmlLocation.isEmpty()) {
                    String packageName = clazz.getPackageName();
                    fxmlLocation = packageName.replace(".", "/") + "/" + clazz.getSimpleName() + ".fxml";
                }
                URL resource = Thread.currentThread().getContextClassLoader().getResource(fxmlLocation);
                FXMLLoader fxmlLoader = new FXMLLoader(resource);
                fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                    @Override
                    public Object call(Class<?> param) {
                        Object view = viewCache.get(param);
                        if (view == null) {
                            try {
                                view = param.getConstructor().newInstance();
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return view;
                    }
                });
                try {
                    Node root = fxmlLoader.load();
                    if (fxmlInfo.label() != null && !fxmlInfo.label().isEmpty()) {
                        root.getProperties().put("title", fxmlInfo.label());
                    }
                    viewCache.put(clazz, view = fxmlLoader.getController());
                    view.setRoot(root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return view;
    }
}
