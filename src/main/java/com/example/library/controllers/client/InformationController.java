package com.example.library.controllers.client;


import com.example.library.common.Regex;
import com.example.library.models.Reader;
import com.example.library.services.IReaderService;
import com.example.library.services.impl.ReaderServiceImpl;
import com.example.library.utils.AlertUtil;
import com.example.library.utils.UserContext;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * The `InformationController` class provides functionality to manage and display reader info.
 * It includes features to initialize, update, and save reader details.
 */
public class InformationController implements Initializable {
  @FXML private Button btnUpdate;
  @FXML private TextField txtEmail;
  @FXML private TextField txtPhoneNumber;
  @FXML private DatePicker dpDob;
  @FXML private TextField txtReaderId;
  @FXML private TextField txtFullname;
  @FXML private TextField txtAddress;
  @FXML private Button btnSave;

  private final IReaderService readerService;

  /**
   * Constructs an InformationController, initializing the reader service
   * to an instance of ReaderServiceImpl.
   */
  public InformationController() {
    this.readerService = new ReaderServiceImpl();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    disableEdit();
    Reader reader = readerService.getReaderByUsername(UserContext.getInstance().getUsername());
    initInformation(reader);
  }

  private void disableEdit() {
    txtAddress.setEditable(false);
    txtEmail.setEditable(false);
    txtFullname.setEditable(false);
    txtPhoneNumber.setEditable(false);
    dpDob.setEditable(false);
    dpDob.setDisable(true);
    txtReaderId.setEditable(false);

    btnSave.setVisible(false);
  }

  private void initInformation(Reader reader) {
    txtReaderId.setText(reader.getReaderId());
    txtFullname.setText(reader.getReaderName());
    txtAddress.setText(reader.getReaderAddress());
    txtEmail.setText(reader.getReaderEmail());
    txtPhoneNumber.setText(reader.getReaderPhone());
    dpDob.setValue(reader.getReaderDOB());

    UserContext.getInstance().setReaderId(reader.getReaderId());
  }

  /**
   * Handles the action event triggered when the update button is clicked.
   * This method enables the fields for editing and makes the save button visible.
   *
   * @param actionEvent The action event triggered by clicking the update button.
   */
  public void onClickUpdate(ActionEvent actionEvent) {
    txtAddress.setEditable(true);
    txtEmail.setEditable(true);
    txtFullname.setEditable(true);
    txtPhoneNumber.setEditable(true);
    dpDob.setEditable(true);
    dpDob.setDisable(false);

    btnSave.setVisible(true);
  }

  /**
   * Handles the action event triggered by clicking the save button.
   * This method validates the email and phone number, updates the reader's information,
   * and initializes the information display. If validation fails, it shows an error alert.
   *
   * @param actionEvent The action event triggered by clicking the save button.
   */
  public void onClickSave(ActionEvent actionEvent) {
    if (!Regex.isValid("EMAIL", txtEmail.getText())) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Email is invalid");
      return;
    }

    if (!Regex.isValid("PHONE_NUMBER", txtPhoneNumber.getText())) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Phone number is invalid");
      return;
    }

    Reader reader =
        Reader.builder()
            .readerId(txtReaderId.getText())
            .readerEmail(txtEmail.getText())
            .readerName(txtFullname.getText())
            .readerAddress(txtAddress.getText())
            .readerPhone(txtPhoneNumber.getText())
            .readerDOB(dpDob.getValue())
            .username(UserContext.getInstance().getUsername())
            .build();

    try {
      readerService.updateReader(reader);
    } catch (Exception e) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, e.getMessage());
      return;
    }

    initInformation(reader);
    disableEdit();
  }
}
