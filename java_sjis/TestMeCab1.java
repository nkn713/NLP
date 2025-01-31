import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class TestMeCab1 {

    public static void main(String[] args) {
	String str = "太郎は昨日図書館で本を借りました。";

	try {
	    // MeCabを起動し，入出力用のストリームを生成する
		Process process = Runtime.getRuntime().exec("mecab");
		BufferedReader br = new BufferedReader(
			new InputStreamReader(process.getInputStream()));
		PrintWriter pw = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(process.getOutputStream())));

	    pw.println(str); // MeCabへ文を送信
		pw.flush();

		String line2;
	    while ((line2 = br.readLine()) != null) { // 解析結果をMeCabから受信
		if (line2.equals("EOS")) { // EOSは文の終わりを表す
			break;
		}
		System.out.println(line2);
		}
		br.close();
		pw.close();
		process.destroy();
	} catch (IOException ex) {
		ex.printStackTrace();
	}
    }
}
