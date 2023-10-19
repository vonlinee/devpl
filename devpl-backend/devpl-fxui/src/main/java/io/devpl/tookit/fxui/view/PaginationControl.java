package io.devpl.tookit.fxui.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

/**
 * 分页控制
 */
public class PaginationControl extends HBox {

    // pagination buttons
    private final Button btnFirstPage;
    private final Button btnLastPage;
    private final Button btnNextPage;
    private final Button btnPrevPage;
    private final ComboBox<Integer> cmbPage;

    private final IntegerProperty currentPage = new SimpleIntegerProperty();

    public PaginationControl(DataTable<?> table) {
        this.setAlignment(Pos.CENTER);
        btnFirstPage = new Button();
        btnFirstPage.setGraphic(IconMap.fontIcon(FontAwesomeSolid.ANGLE_DOUBLE_LEFT));
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

        btnPrevPage = new Button();
        btnPrevPage.setGraphic(IconMap.fontIcon(FontAwesomeSolid.ANGLE_LEFT));
        btnPrevPage.setOnAction(paginationHandler);
        btnPrevPage.setFocusTraversable(false);

        btnNextPage = new Button();
        btnNextPage.setGraphic(IconMap.fontIcon(FontAwesomeSolid.ANGLE_RIGHT));
        btnNextPage.setOnAction(paginationHandler);
        btnNextPage.setFocusTraversable(false);

        btnLastPage = new Button();
        btnLastPage.setGraphic(IconMap.fontIcon(FontAwesomeSolid.ANGLE_DOUBLE_RIGHT));
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

        currentPage.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Event.fireEvent(table, PagingEvent.pageChange(newValue.intValue(), table.getPageSize()));
            }
        });

        cmbPage.valueProperty().addListener((observable, oldValue, newValue) -> currentPage.set(newValue));

        cmbPage.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Integer> call(ListView<Integer> param) {
                ComboBoxListCell<Integer> cell = new ComboBoxListCell<>() {
                    @Override
                    public void startEdit() {
                        super.startEdit();
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        table.maxPageNumProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updatePages(newValue.intValue());
            }
        });

        this.getChildren().addAll(btnFirstPage, btnPrevPage, cmbPage, btnNextPage, btnLastPage);
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

    public void updatePages(int maxPageNum) {
        cmbPage.getItems().clear();
        for (int i = 1; i <= maxPageNum; i++) {
            cmbPage.getItems().add(i);
        }
        cmbPage.getSelectionModel().selectFirst();
    }

    public void selectPage(int pageNum) {
        if (cmbPage.getItems().contains(pageNum)) {
            currentPage.set(pageNum);
        }
    }
}
