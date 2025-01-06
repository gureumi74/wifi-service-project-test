import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestGet {
    public static void main(String[] args) {
        // APU URL 설정
        String apiKey = System.getenv("API_KEY");
        String apiUrl = "http://openapi.seoul.go.kr:8088/" + apiKey + "/json/TbPublicWifiInfo/1/5/";

        // OkHttpClient API 호출
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        try (Response response = client.newCall(request).execute()){
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();

                // 파싱
                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
                JsonObject wifiInfo = jsonObject.getAsJsonObject("TbPublicWifiInfo");
                JsonArray rows = wifiInfo.getAsJsonArray("row");

                // 공통 정보
                JsonObject result = wifiInfo.getAsJsonObject("RESULT");
                String resultCode = result.get("CODE").getAsString();
                String resultMessage = result.get("MESSAGE").getAsString();
                int listTotalCount = wifiInfo.get("list_total_count").getAsInt();

                // 데이터 확인
                System.out.println("[  서울시 공공 와이파이 정보  ]");
                System.out.println("총 데이터 건수 : " + listTotalCount);
                System.out.println("요청 결과 코드 : " + resultCode);
                System.out.println("요청 결과 메시지 : " + resultMessage);
                System.out.println();

                for (int i = 0; i < rows.size(); i++) {
                    JsonObject wifi = rows.get(i).getAsJsonObject();
                    System.out.println((i + 1) + " 와이파이 이름 : " + wifi.get("X_SWIFI_MAIN_NM").getAsString());
                    System.out.println("관리번호 : " + wifi.get("X_SWIFI_MGR_NO").getAsString());
                    System.out.println("자치구 : " + wifi.get("X_SWIFI_WRDOFC").getAsString());
                    System.out.println("도로명주소 : " + wifi.get("X_SWIFI_ADRES1").getAsString());
                    System.out.println("상세주소: " + wifi.get("X_SWIFI_ADRES2").getAsString());
                    System.out.println("설치위치 (층) : " + wifi.get("X_SWIFI_INSTL_FLOOR").getAsString());
                    System.out.println("설치유형 : " + wifi.get("X_SWIFI_INSTL_TY").getAsString());
                    System.out.println("설치년도 : " + wifi.get("X_SWIFI_CNSTC_YEAR").getAsString());
                    System.out.println();
                }
            } else {
                System.out.println("API 호출 실패 : " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
