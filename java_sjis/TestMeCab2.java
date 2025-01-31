import java.io.*;
import java.util.*;

public class TestMeCab2 {

    public static void main(String[] args) {

	// 入力ファイルはファイルinput.txtに1行1文の形式で格納されているとする
	// まず入力の文章を1行ずつ分けてリストに格納する
	List<String> textLines = new ArrayList<String>(); // リストの初期化
	try {
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		String line;
		while ((line = br.readLine()) != null) {
		textLines.add(line);
		}
		br.close();
	} catch (IOException ex) {
		ex.printStackTrace();
	}

	try {
	    // MeCabを起動し，入出力用のストリームを生成する
		Process process = Runtime.getRuntime().exec("mecab");
		BufferedReader br = new BufferedReader(
			new InputStreamReader(process.getInputStream()));
		PrintWriter pw = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(process.getOutputStream())));

	    // 入力文章を１行ずつMeCabに送って形態素解析させる
		for (String line : textLines) {
		pw.println(line); // MeCabへ文を送信
		pw.flush();

		String line2;
		while ((line2 = br.readLine()) != null) {  // 解析結果をMeCabから受信
		    if (line2.equals("EOS")) {  // EOSは文の終わりを表す
			break;
			}
		    // System.out.println(line2);
		    String[] split = line2.split("[\t,]"); // 区切り文字で分割
			String syutugen = split[0];
			String hinsi1 = split[1];
			String hinsi2 = split[2];
			String hinsi3 = split[3];
			String hinsi4 = split[4];
			String katsuyo1 = split[5];
			String katsuyo2 = split[6];
			String kihon = split[7];
			System.out.println("出現形：" + syutugen);
			System.out.println("基本形：" + kihon);
			System.out.println("品詞：" + hinsi1 + "-" + hinsi2 + "-" + hinsi3 + "-" + hinsi4);
		}
		}
		br.close();
		pw.close();
		process.destroy();
	} catch (IOException ex) {
		ex.printStackTrace();
	}
    }
}
