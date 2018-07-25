package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        // ファイル sample.fxml を読み込む
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        // FXMLをSceneへ設定, 画面描画
        primaryStage.setTitle("GenMap");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}