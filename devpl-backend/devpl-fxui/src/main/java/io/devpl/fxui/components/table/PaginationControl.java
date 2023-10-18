package io.devpl.fxui.components.table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * 分页控制组件
 *
 * @see javafx.scene.control.Pagination
 */
public class PaginationControl extends HBox {

    // pagination buttons
    private final Button btnFirstPage;
    private final Button btnLastPage;
    private final Button btnNextPage;
    private final Button btnPrevPage;
    private final ComboBox<Integer> cmbPage;

    /**
     * 当前页
     */
    private final ObjectProperty<Integer> currentPageNum = new SimpleObjectProperty<>();

    /**
     * 当前每页显示数据条数
     */
    private final IntegerProperty currentPageSize = new SimpleIntegerProperty(10);

    private final IntegerProperty maxPageNum = new SimpleIntegerProperty();

    public PaginationControl() {
        this.setAlignment(Pos.CENTER);
        btnFirstPage = new Button("<<");
        // btnFirstPage.setGraphic(new Button("First"));
        final EventHandler<ActionEvent> paginationHandler = new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() == btnFirstPage) {
                    cmbPage.getSelectionModel().selectFirst();
                } else if (event.getSource() == btnPrevPage) {
                    cmbPage.getSelectionModel().selectPrevious();
                } else if (event.getSource() == btnNextPage) {
                    cmbPage.getSelectionModel().selectNext();
                } else if (event.getSource() == btnLastPage) {
                    cmbPage.getSelectionModel().selectLast();
                } else if (event.getSource() == cmbPage) {

                }
            }
        };
        btnFirstPage.setOnAction(paginationHandler);
        btnFirstPage.setFocusTraversable(false);
        btnFirstPage.getStyleClass().addAll("pill-button", "pill-button-left");

        btnPrevPage = new Button("<");
        // btnPrevPage.setGraphic(new Button("Prev"));
        btnPrevPage.setOnAction(paginationHandler);
        btnPrevPage.setFocusTraversable(false);

        btnNextPage = new Button(">");
        // btnNextPage.setGraphic(new Button("Next"));
        btnNextPage.setOnAction(paginationHandler);
        btnNextPage.setFocusTraversable(false);

        btnLastPage = new Button(">>");
        // btnLastPage.setGraphic(new Button("Last"));
        btnLastPage.setOnAction(paginationHandler);
        btnLastPage.setFocusTraversable(false);

        cmbPage = new ComboBox<>();
        cmbPage.setEditable(true);
        cmbPage.setOnAction(paginationHandler);
        cmbPage.setFocusTraversable(false);
        cmbPage.setPrefWidth(105.0);

        cmbPage.setConverter(new StringConverter<>() {
            @Override
            public String toString(Integer object) {
                return String.valueOf(object);
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (Exception exception) {
                    return cmbPage.getValue();
                }
            }
        });

        // 页码更新时更新表格数据
        currentPageNum.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && currentPageChangeHandler != null) {
                PagingEvent pagingEvent = PagingEvent.pageChange(newValue, getCurrentPageSize());
                currentPageChangeHandler.handle(pagingEvent);
            }
        });

        // 更新当前页码
        cmbPage.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && newValue != null) {
                setCurrentPageNum(newValue);
            }
        });

        cmbPage.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Integer> call(ListView<Integer> param) {
                ComboBoxListCell<Integer> cell = new ComboBoxListCell<>() {
                    @Override
                    public void startEdit() {
                        super.startEdit();
                    }

                    @Override
                    public void commitEdit(Integer newValue) {
                        super.commitEdit(newValue);
                        System.out.println(newValue);
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        this.getChildren().addAll(btnFirstPage, btnPrevPage, cmbPage, btnNextPage, btnLastPage);
    }

    EventHandler<PagingEvent> currentPageChangeHandler;

    public void setOnCurrentPageChanged(EventHandler<PagingEvent> eventHandler) {
        this.currentPageChangeHandler = eventHandler;
    }

    private void toggleButtons(int startIndex, boolean moreRows) {
        boolean firstPage = startIndex == 0;
        btnFirstPage.setDisable(firstPage);
        btnPrevPage.setDisable(firstPage);
        btnNextPage.setDisable(!moreRows);
        btnLastPage.setDisable(!moreRows);
    }

    public final void refreshPageNums(long maxPageNum) {
        cmbPage.setDisable(maxPageNum == 0);
        cmbPage.getItems().clear();
        for (int i = 1; i <= maxPageNum; i++) {
            cmbPage.getItems().add(i);
        }
    }

    public void updatePageNums(int pageSize, long total) {
        int maxPageNum = calculateMaxPageNum(pageSize, total);
        if (maxPageNum == getMaxPageNum()) {
            return;
        }
        updatePageNums(maxPageNum);
    }

    /**
     * 更新页码
     *
     * @param maxPageNum 最大页码
     */
    public void updatePageNums(int maxPageNum) {
        cmbPage.getItems().clear();
        for (int i = 1; i <= maxPageNum; i++) {
            cmbPage.getItems().add(i);
        }
        this.maxPageNum.set(maxPageNum);
    }

    /**
     * 计算最大页数
     *
     * @param pageSize      每页记录条数
     * @param totalRowCount 总记录数
     * @return 最大页数
     */
    int calculateMaxPageNum(int pageSize, long totalRowCount) {
        long pages = totalRowCount / (long) pageSize;
        return pages == 0 ? 1 : (int) pages + 1;
    }

    public final void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum.set(currentPageNum);
        if (cmbPage.getValue() == null) {
            cmbPage.setValue(currentPageNum);
        }
    }

    public final int getCurrentPageNum() {
        return currentPageNum.get();
    }

    public final int getCurrentPageSize() {
        return currentPageSize.get();
    }

    public final int getMaxPageNum() {
        return maxPageNum.get();
    }
}
