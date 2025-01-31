import java.io.*;
import java.util.*;

public class TestCaboCha1 {

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
	    // CaboChaを起動し，入出力用のストリームを生成する
		Process process = Runtime.getRuntime().exec(new String[] {"cabocha", "-f1"});
		BufferedReader br = new BufferedReader(
			new InputStreamReader(process.getInputStream()));
		PrintWriter pw = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(process.getOutputStream())));

	    // 入力文章を１行ずつCaboChaに送って係り受け解析させる
		for (String line : textLines) {
		pw.println(line); // CaboChaへ文を送信
		pw.flush();

		String line2;
		int bunsetuId, dependId; // 現在の文節番号と，係り先文節番号
		while ((line2 = br.readLine()) != null) {  // 解析結果をCaboChaから受信
		    if (line2.equals("EOS")) {  // EOSは文の終わりを表す
			break;
			}
		    if (line2.charAt(0) == '*') { // 文節情報を表す行のとき
			String[] split = line2.split(" ");
			bunsetuId = Integer.parseInt(split[1]);
			dependId = Integer.parseInt(
				split[2].substring(0, split[2].length()-1));
			System.out.println("文節番号 " + bunsetuId +
				" → 係り先文節番号 " + dependId);
		    } else { // 形態素情報を表す行のとき
			String[] split = line2.split("[\t,]");
			String syutugen = split[0];
			String hinsi1 = split[1];
			String hinsi2 = split[2];
			String hinsi3 = split[3];
			String hinsi4 = split[4];
			String katsuyo1 = split[5];
			String katsuyo2 = split[6];
			String kihon = split[7];
			System.out.print("出現形：" + syutugen);
			System.out.print(" 基本形：" + kihon);
			System.out.println(" 品詞：" + hinsi1 + "-" + hinsi2 +
					   "-" + hinsi3 + "-" + hinsi4);
			}
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
