package io.github.geniusay;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 14:14
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Menu.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("登号器");
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("程序已关闭");
            System.exit(0);
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
