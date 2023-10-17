package io.devpl.fxui.components.table;

public class TablePaneOption {

    private final Class<?> modelClass;
    private boolean enablePagination;
    private boolean enableToolBar;

    TablePaneOption(Class<?> modelClass) {
        this.modelClass = modelClass;
    }

    public static <T> TablePaneOption model(Class<T> modelClass) {
        return new TablePaneOption(modelClass);
    }

    public TablePaneOption enablePagination(boolean pageable) {
        this.enablePagination = pageable;
        return this;
    }

    public TablePaneOption enableToolbar(boolean enableToolBar) {
        this.enableToolBar = enableToolBar;
        return this;
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public boolean isPaginationEnabled() {
        return enablePagination;
    }

    public boolean isToolBarEnabled() {
        return enableToolBar;
    }
}
