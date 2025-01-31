import java.io.*;
import java.util.*;

// 雑談対話システムの単純なサンプルプログラム
public class TestZatudan1 {
    // インスタンス変数
    public List<ReactionPattern> patternList; // 反応パターンリスト

    // メインメソッド
    public static void main(String[] args) {
	TestZatudan1 instance = new TestZatudan1();
    }

    // コンストラクタ：ここでは雑談対話システムのメインフローを記述する
    public TestZatudan1() {
	String output = "こんにちは！"; // システムの応答文

	setupReactionPattern(); // 反応パターンリストの初期化

	// 最初の挨拶
	System.out.println("システム：" + output);

	try {
	    // 標準入力の準備
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("ユーザ　：");
		String input;
	    while((input = br.readLine()) != null) { // ユーザによる文の入力を受け取る
		// システムの応答を生成し出力する
		output = generateResponse(input, output);
		System.out.println("システム：" + output);
		System.out.print("ユーザ　：");
	    }
	    br.close();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

    // 反応パターンリストの初期化を行う
    public void setupReactionPattern() {
	// データはファイルpattern.txtに1行1パターンの形式で格納されているとする
	// それらのデータを順次読み込み，リストに格納する
	patternList = new ArrayList<ReactionPattern>(); // リストの初期化
	try {
	    BufferedReader br = new BufferedReader(new FileReader("pattern.txt"));
	    String line;
	    while ((line = br.readLine()) != null) {
		String[] split = line.split("\t"); // タブ文字を区切りとして分割する
		ReactionPattern ptn = new ReactionPattern(); // 新しいインスタンスを作る
		ptn.keyword = split[0];
		ptn.response = split[1];
		patternList.add(ptn); // パターンリストに追加する
	    }
	    br.close();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

    // 応答文を生成する
    public String generateResponse(String input, String previousOutput) {
	// 応答候補リスト
	List<ResponseCandidate> candidateList = new ArrayList<ResponseCandidate>();

	// 複数の方法での応答候補生成を行う
	// それぞれの方法で生成した応答候補はcandidateListに順次追加していく
	generateResponseByPattern(input, candidateList);
	generateResponseByExpansion(previousOutput, candidateList);
	generateOtherResponse(candidateList);

	// スコア最大の応答候補を選択する
	String ret = "";
	double maxScore = -1.0;
	for (ResponseCandidate cdd : candidateList) {
	    if (cdd.score > maxScore) {
		ret = cdd.response;
		maxScore = cdd.score;
	    }
	}

	return ret;
    }

    // 反応パターンを利用した応答候補の生成
    void generateResponseByPattern(String input, List<ResponseCandidate> candidateList) {
	double score = 3.0;

	for (ReactionPattern ptn : patternList) {
	    if (input.contains(ptn.keyword)) { // ユーザ入力文がキーワードを含んでいたら
		ResponseCandidate cdd = new ResponseCandidate(); // 応答候補を生成する
		cdd.response = ptn.response;
		cdd.score = score;
		candidateList.add(cdd);
	    }
	}
    }

    // 1つ前の自分の発言からの話題展開による応答候補の生成
    void generateResponseByExpansion(String previousOutput, List<ResponseCandidate> candidateList) {
	double score = 2.0;

	// この例では単に繰り返すだけ
	ResponseCandidate cdd = new ResponseCandidate();
	cdd.response = "うん、" + previousOutput;
	cdd.score = score;
	candidateList.add(cdd);
    }

    // その他の応答候補の生成
    void generateOtherResponse(List<ResponseCandidate> candidateList) {
	double score = 1.0;

	// この例では単にうなるだけ
	ResponseCandidate cdd = new ResponseCandidate();
	cdd.response = "うーん。";
	cdd.score = score;
	candidateList.add(cdd);
    }

}

