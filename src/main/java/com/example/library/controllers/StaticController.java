package com.example.library.controllers;

import com.example.library.services.IStaticService;
import com.example.library.services.impl.StaticServiceImpl;
import com.example.library.utils.AlertUtil;
import com.example.library.utils.SettingUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * The StaticController class handles the initialization and interaction logic
 * for the static view in the application.
 * It implements the Initializable interface.
 */
public class StaticController implements Initializable {
  @FXML private Text txtNumberReader;
  @FXML private Text txtTotalBorrow;
  @FXML private Text txtTotalQuantityBook;
  @FXML private Text txtTotalLate;
  @FXML private Text txtNumberReturn;
  @FXML private TextArea taNumberReturn;
  @FXML private PieChart pieLate;
  private final IStaticService staticService;
  private final SettingUtils settingUtils = SettingUtils.getInstance();

  /**
   * Default constructor for the StaticController class.
   * This constructor initializes an instance of StaticServiceImpl and assigns it to
   * the staticService field.
   */
  public StaticController() {
    staticService = new StaticServiceImpl();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    txtNumberReader.setText(String.valueOf(staticService.getTotalReader()));
    txtTotalBorrow.setText(String.valueOf(staticService.getTotalBorrow()));
    txtTotalQuantityBook.setText(String.valueOf(staticService.getTotalBook()));
    txtTotalLate.setText(String.valueOf(staticService.getTotalLate()));
    txtNumberReturn.setText(String.valueOf(staticService.getTotalReturn()));

    txtNumberReturn.setVisible(false);
    taNumberReturn.setVisible(false);
    drawChart();
  }

  private void drawChart() {
    pieLate.getData().clear();
    pieLate.getData().add(new PieChart.Data("Total Borrow", staticService.getTotalBorrow()));
    pieLate.getData().add(new PieChart.Data("Total Return Late", staticService.getTotalLate()));
  }

  /**
   * Handles the click event on the "Return On Time" button.
   * This method toggles the highlight return option based on the current state.
   * A confirmation dialog is presented to the user before changing the setting.
   *
   * @param mouseEvent The mouse event triggered by clicking the "Return On Time" button.
   */
  public void onClickReturnOnTime(MouseEvent mouseEvent) {
    if (settingUtils.isHighlightReturn()) {
      if (AlertUtil.showConfirmation("Bạn có muốn tắt tuỳ chọn này không?")) {
        settingUtils.setHighlightReturn(false);
      }
    } else if (AlertUtil.showConfirmation("Bạn có muốn bật tuỳ chọn này không?")) {
      settingUtils.setHighlightReturn(true);
    }
  }

  /**
   * Handles the click event on the "Return Late" button.
   * This method toggles the highlight late return option based on the current state.
   * A confirmation dialog is presented to the user before changing the setting.
   *
   * @param mouseEvent The mouse event triggered by clicking the "Return Late" button.
   */
  public void onClickReturnLate(MouseEvent mouseEvent) {
    if (settingUtils.isHighlightLate()) {
      if (AlertUtil.showConfirmation("Bạn có muốn tắt tuỳ chọn này không?")) {
        settingUtils.setHighlightLate(false);
      }
    } else if (AlertUtil.showConfirmation("Bạn có muốn bật tuỳ chọn này không?")) {
      settingUtils.setHighlightLate(true);
    }
  }
}
