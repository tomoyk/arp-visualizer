package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Controller {

    @FXML
    public GridPane gridPane;
    public Label statusLabel;

    /**
     * Searchボタンを押下したときの処理
     * @param event イベント処理
     */
    @FXML
    public void submitSearch(ActionEvent event) throws IOException {

        // メッセージを変更
        statusLabel.setText("端末の一覧を表示しています.");

        // ARPテーブル一覧を取得
        Map addrs = execArp();

        // GridPaneの幅と高さを取得
        final int rowSize = gridPane.getRowConstraints().size();
        final int columnSize = gridPane.getColumnConstraints().size();

        int counter = 0; // カウンタ
        MacAddrManage mam = new MacAddrManage();

        Set<Map.Entry<String, String>> addrsSet = addrs.entrySet();
        for(Map.Entry<String, String> addr : addrsSet){

            int y = counter % (columnSize-1);
            int x = counter / (columnSize-1);
            // System.out.println(x + " - " + y);

            // グリッド数を超える場合
            if(columnSize-1 < x || rowSize-1 < y){
                break;
            }

            String myIp = addr.getKey(); // IPアドレス
            String myMac = addr.getValue(); // Macアドレス

            // ベンダの割り出し
            String myVendor = mam.macToVendor(myMac);
            if(myVendor==null){
                myVendor = "Unknown";
            }

            // グリッドへの表示
            Label text = new Label(myIp + "\n" + myVendor); // 表示する文字列を組み立て
            text.setTextFill(Color.MAGENTA); // 文字色
            text.setStyle("-fx-font-weight: bold");
            text.setStyle("-fx-effect: dropshadow( gaussian , rgba(255,255,255,1.0) , 0,0,1,1 );");
            text.setFont(new Font("Arial", 16));

            // 画像の描画
            String filename = myVendor.replaceAll(" ", "").toLowerCase();

            Image image = new Image("images/"+filename+".png");
            ImageView ivie = new ImageView();
            ivie.setImage(image); // ImageViewに画像を張る
            ivie.setFitWidth(100); // 画像の幅
            ivie.setPreserveRatio(true); // 縦横比を保持
            ivie.setSmooth(true); // なめらかに張る

            // gridPane.setAlignment(Pos.TOP_CENTER); // 中央に配置
            gridPane.add(ivie, x, y);
            gridPane.add(text, x,y); // パネルへ追加

            counter++;
        }
    }

    /**
     * ARPコマンドを実行して、その結果をソートしたMapで返す
     * @return Map(Key=IPアドレス, value=Macアドレス)
     * @throws IOException
     */
    public Map<String, String> execArp() throws IOException {

        // 実行するコマンド
        final String COMMAND = "cat /Users/tkoyama/result"; // "arp -a";

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

            // System.out.println("[RESULT] ip="+ipAddr+",\t mac="+macAddr);

            // イメージ: addrs[ipAddrs] = macAddr;
            addrs.put(ipAddr, macAddr);

        }

        return addrs;
    }

}
