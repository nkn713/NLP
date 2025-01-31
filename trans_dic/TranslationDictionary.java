import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class TranslationDictionary {
    private static final String API_KEY = System.getenv("DEEPL_API_KEY");
    private static final String HISTORY_FILE = "translation_history.txt";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("翻訳する日本語文を入力してください：");
                String text = scanner.nextLine();

                System.out.print("英語・中国語・フランス語・ドイツ語の中から翻訳する言語を入力してください：");
                String targetLanguage = scanner.nextLine();

                String langCode = getLanguageCode(targetLanguage);

                if (langCode != null) {
                    String translatedText = translateText(text, langCode);
                    System.out.println("翻訳結果：" + translatedText);
                    saveTranslation(text, translatedText, targetLanguage);
                } else {
                    System.out.println("対応していない言語が入力されました。");
                }
            }
        }
    }

    private static String getLanguageCode(String language) {
        switch (language) {
            case "英語":
                return "EN";
            case "中国語":
                return "ZH";
            case "フランス語":
                return "FR";
            case "ドイツ語":
                return "DE";
            default:
                return null;
        }
    }

    private static String translateText(String text, String targetLanguage) {
        try {
            String urlStr = "https://api-free.deepl.com/v2/translate";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "DeepL-Auth-Key " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String params = "text=" + URLEncoder.encode(text, StandardCharsets.UTF_8)
                    + "&target_lang=" + URLEncoder.encode(targetLanguage, StandardCharsets.UTF_8);

            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = params.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return parseTranslation(response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String parseTranslation(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"text\":\"") + 8;
        int endIndex = jsonResponse.indexOf("\"}", startIndex);
        if (startIndex != -1 && endIndex != -1) {
            return jsonResponse.substring(startIndex, endIndex);
        } else {
            return "翻訳できませんでした。";
        }
    }

    private static void saveTranslation(String originalText, String translatedText, String targetLanguage) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE, true))) {
        writer.write("日本語: " + originalText + "\n");
        writer.write(targetLanguage + ": " + translatedText + "\n");
        writer.write("------------------------------------------------------------------------------------------------------------------------------------------\n");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
