package sample;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class MacAddrManage {

    // KEY:   [A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}
    // VALUE: [\\w\\s,\\.]+
    public Map<String, String> macAddrs = new TreeMap<>();

    // 参考: http://www.vor.jp/oui/oui.html

    /**
     * Macアドレスからベンダーを割り出すメソッド
     *
     */
    public String macToVendor(String targetMac){

        // 探すmacAddressを小文字に and -を:へ
        String targetMacLower = targetMac.toLowerCase().replaceAll("-", ":");

        // 探すmacAddressのVendorを抽出
        String targetMacVend = targetMacLower.split(":")[0] +":"+ targetMacLower.split(":")[1] +":"+ targetMacLower.split(":")[2];

        // あからじめ作成したベンダー一覧から探す
        for(String listedMac : macAddrs.keySet()){

            // -を: and 小文字に
            String listedMacLower = listedMac.replaceAll("-", ":").toLowerCase();

            // 一致するか判定
            // System.out.println(listedMacLower + " vs " + targetMacVend);
            if(targetMacVend.equals(listedMacLower)){
                return macAddrs.get(listedMac);
            }
        }

        return null;

    }

    public MacAddrManage(){

        BufferedReader br = null;
        try {
             br = new BufferedReader(new FileReader("/Users/tkoyama/IdeaProjects/sample01/src/vendorList2"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(;;){
            try {
                String line = br.readLine();

                // 末尾に到達
                if(line == null){
                    break;
                }

                // macアドレスとベンダー名を分割
                String lineMac = line.split("\t")[0].replaceAll(" ", ""); // 空白を除去
                String lineVendor = line.split("\t")[1];

                // 追加
                macAddrs.put(lineMac, lineVendor);
                // System.out.println(lineMac + "\t\t" + lineVendor);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
