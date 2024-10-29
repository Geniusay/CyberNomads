package io.github.geniusay;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/29 11:32
 */
public class MenuController {

    @FXML
    private TextField webDriver;

    @FXML
    private TextField browser;

    public MenuController() {
    }

    @FXML
    private void handleSubmit() {

        String filePath1 = webDriver.getText();
        String filePath2 = browser.getText();
        if(StringUtils.isBlank(filePath1)||StringUtils.isBlank(filePath2)){
            // 创建提示框
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("路径不能为空!");
            alert.showAndWait();
            return;
        }
        try {
            // 加载登录页面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Register.fxml"));
            Parent loginPage = loader.load();
            LoginController loginController = loader.getController();
            loginController.set(filePath1,filePath2);
            // 创建新的场景并显示登录页面
            Stage stage = (Stage) webDriver.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
