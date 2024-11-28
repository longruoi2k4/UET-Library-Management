package com.example.library;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * A class that extends {@link Application} and sets up the primary stage and scene for
 * the application.
 * It provides methods to change the root FXML of the scene and to create a new modal stage
 * with a different FXML.
 */
public class App extends Application {
  private static Scene scene;
  private static Stage stage;

  @Override
  public void start(Stage primaryStage) throws IOException {
    stage = primaryStage;
    scene = new Scene(loadFXML("LoginFrm"), 931, 606);

    primaryStage.initStyle(StageStyle.TRANSPARENT);

    scene.setFill(Color.TRANSPARENT);
    primaryStage.setScene(scene);

    primaryStage.setResizable(false);
    primaryStage.show();
    centerStage(primaryStage);
  }

  /**
   * Sets the root FXML for the primary stage and updates the stage dimensions to
   * match the preferred width and height of the new root. It also centers the stage
   * on the screen.
   *
   * @param fxml The name of the FXML file (without the .fxml extension) to load
   *             and set as the new root.
   * @throws IOException if an input-output exception occurs while loading the FXML file.
   */
  public static void setRoot(String fxml) throws IOException {
    Parent root = loadFXML(fxml);
    scene.setRoot(root);
    stage.setWidth(root.prefWidth(-1));
    stage.setHeight(root.prefHeight(-1));

    centerStage(stage);
  }

  /**
   * Opens a new modal window with a scene loaded from the specified FXML file.
   * The parent window is disabled until the modal window is closed.
   *
   * @param fxml The name of the FXML file to load the scene from.
   * @param title The title of the new modal window.
   * @param resizable Indicates whether the new modal window should be resizable.
   * @throws IOException If the FXML file cannot be loaded.
   */
  public static void setRootPop(String fxml, String title, boolean resizable) throws IOException {
    Stage newStage = new Stage();
    Scene newScene = new Scene(loadFXML(fxml));

    // Thiết lập stage mới
    newStage.setResizable(resizable);
    newStage.setScene(newScene);
    newStage.setTitle(title);

    // Làm cho stage mới là modal
    newStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
    newStage.initOwner(stage);

    // Hiệu ứng làm tối cửa sổ cũ
    stage.getScene().getRoot().setDisable(true); // Vô hiệu hóa cửa sổ chính

    newStage.setOnHidden(
        e -> {
          // Kích hoạt lại cửa sổ chính khi cửa sổ mới đóng
          stage.getScene().getRoot().setDisable(false);
        });

    // Hiển thị cửa sổ mới
    newStage.showAndWait();
    centerStage(newStage);
  }

  private static void centerStage(Stage stage) {
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    double x = (screenBounds.getWidth() - stage.getWidth()) / 2;
    double y = (screenBounds.getHeight() - stage.getHeight()) / 2;

    stage.setX(x);
    stage.setY(y);
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  public static void main(String[] args) {
    launch();
  }
}
