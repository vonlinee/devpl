package io.devpl.sdk;

import java.util.function.Supplier;

/**
 * DataObject工厂
 */
public final class DataClass {

    private static final DataClass INSTANCE = new DataClass(() -> new MapDataObject(5));
    private Supplier<DataObject> supplier;

    public DataClass(Supplier<DataObject> supplier) {
        this.supplier = supplier;
    }

    public static DataObject newObject() {
        return INSTANCE.supplier.get();
    }

    public static void setFactory(Supplier<DataObject> supplier) {
        INSTANCE.supplier = supplier;
    }
}
