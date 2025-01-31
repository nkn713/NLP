import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// 天気予報API（HTTP）のテスト

public class TestWeather {

    // コンストラクタ
    public TestWeather() {
    }

    // 天気予報APIを利用して指定した都市の天気予報を得るメソッド
    public String getForecast(String cityID) {
        String response = null; // Webサーバからの応答

        try {
            // Web APIのリクエストURLを構築する
            String url = "http://weather.tsukumijima.net/api/forecast/city/" + cityID;

            // HTTP接続を確立し，処理要求を送る
            HttpURLConnection conn = (HttpURLConnection)(new URL(url)).openConnection();
            conn.setRequestMethod("GET"); // GETメソッド
            conn.setRequestProperty("User-Agent", "");

            // Webサーバからの応答を受け取る
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            response = "";
            String line;
            while((line = br.readLine()) != null) {
                response += line;
            }
            br.close();
            conn.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println(response); // サーバからの応答全体を出力する（確認用）


        // telopキーの次の文字列を取得して返す
        String forecast = ""; // 返り値
        if (response != null && response.length() > 0) {
            int offset = response.indexOf("telop") + 7; // telopキーの次の位置
            offset = response.indexOf("\"", offset) + 1; // 天気予報文字列の先頭（文字列を囲っている " の次）の位置
            int end = response.indexOf("\"", offset); // 天気予報文字列の最後の " の位置
            forecast = response.substring(offset, end);
        }
        return forecast;
    }

    // 動作テスト用のmainメソッド
    public static void main(String[] args) {
        String cityID = "400040";
        TestWeather instance = new TestWeather();

        String forecast = instance.getForecast(cityID);
        System.out.println(cityID + "の天気：");
        System.out.println(forecast);
    }
}
