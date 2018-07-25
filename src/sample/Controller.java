package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Process;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Controller {

    @FXML
    public TextField addressBox;
    public GridPane gridPane;

    /**
     * Searchボタンを押下したときの処理
     * @param event イベント処理
     */
    @FXML
    public void submitSearch(ActionEvent event) throws IOException {

        try {
            String s = addressBox.getText();
            String[] ss = s.split("/");
            System.out.println("入力された値1: " + ss[0]);
            System.out.println("入力された値2: " + ss[1]);
        }catch (Exception e){
            // Nothing
        }

        addressBox.setText("");

        // マウスクリックした時の動作
        // mouseEntered(event);

        // ARPテーブル一覧を取得
        Map addrs = execArp();

        // GridPaneの幅と高さを取得
        final int rowSize = gridPane.getRowConstraints().size();
        final int columnSize = gridPane.getColumnConstraints().size();

        // 画像を追加
        TextField userTextField = new TextField();
        gridPane.add(userTextField, 1, 1);

    }

    @FXML
    private void mouseEntered(ActionEvent e) {
        Node source = (Node)e.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
    }

    @FXML
    public void drawShape(){

        System.out.println("drawShape");

        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        Button button = new Button("hello");
        gridPane.add(button, 2, 2);



        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(button);
        gridPane.add(hbBtn, 1, 4);

        /*
        GridPane.setRowIndex(button, 3);
        GridPane.setColumnIndex(button, 2);
        Label label = new Label();
        GridPane.setConstraints(label, 2, 0); // column=2 row=0
        */
    }

    /**
     * ARPコマンドを実行して、その結果をソートしたMapで返す
     * @return Map(Key=IPアドレス, value=Macアドレス)
     * @throws IOException
     */
    public Map<String, String> execArp() throws IOException {

        // 実行するコマンド
        final String COMMAND = "arp -a";

        // 実行結果を保持するMap
        Map<String,String> addrs = new TreeMap<>();

        // おまじない
        Runtime runtime = Runtime.getRuntime();
        Process p = runtime.exec(COMMAND);
        InputStream is = p.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        // コマンドの実行結果を出力
        while(true) {

            // 入力から1行取り出し
            String result = br.readLine();

            // null ならループから抜ける
            if (result == null) {
                break;
            }

            // コマンド実行結果のスペース分割
            String[] parameters = result.split(" ");
            // System.out.println("[RESULT] "+result);

            // IPアドレス, Macアドレスの取り出し
            String ipAddr = parameters[1].replaceAll("[\\(\\)]", "");
            String ipAddr1octed = ipAddr.split("\\.")[0];
            String macAddr = parameters[3];

            if(ipAddr1octed.equals("192") || ipAddr1octed.equals("172") || ipAddr1octed.equals("10")){
                // 正常 :: ローカルIPアドレス
            } else {
                continue;
            }

            System.out.println("[RESULT] ip="+ipAddr+",\t mac="+macAddr);

            // イメージ: addrs[ipAddrs] = macAddr;
            addrs.put(ipAddr, macAddr);

        }

        // Debug::
        /*for(String key : addrs.keySet()){
            System.out.println("ip="+key+", mac="+addrs.get(key));
        }*/

        return addrs;
    }

}
