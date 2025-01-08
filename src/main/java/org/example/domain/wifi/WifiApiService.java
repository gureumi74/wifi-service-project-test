package org.example.domain.wifi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.domain.location.LocationService;

import java.util.ArrayList;
import java.util.List;

public class WifiApiService {
    // API - 전체 wifi 정보 불러오기
    public List<Wifi> wifiApiGet() {
        LocationService locationService = new LocationService();
        String url = locationService.sqliteDriveLoad();

        // API URL 설정
        String apiKey = System.getenv("API_KEY");
        String apiUrl = "http://openapi.seoul.go.kr:8088/" + apiKey + "/json/TbPublicWifiInfo/1/1000/";

        // OkHttpClient API 호출
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        List<Wifi> wifiList = new ArrayList<>();

        try (Response response = client.newCall(request).execute()){
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();

                // 파싱
                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
                JsonObject wifiInfo = jsonObject.getAsJsonObject("TbPublicWifiInfo");
                JsonArray rows = wifiInfo.getAsJsonArray("row");

                for (int i = 0; i < rows.size(); i++) {
                    JsonObject wifiObject = rows.get(i).getAsJsonObject();
                    Wifi wifi = new Wifi();
                    wifi.setMgrNo(wifiObject.get("X_SWIFI_MGR_NO").getAsString());
                    wifi.setWrdofc(wifiObject.get("X_SWIFI_WRDOFC").getAsString());
                    wifi.setWifiName(wifiObject.get("X_SWIFI_MAIN_NM").getAsString());
                    wifi.setAddress1(wifiObject.get("X_SWIFI_ADRES1").getAsString());
                    wifi.setAddress2(wifiObject.get("X_SWIFI_ADRES2").getAsString());
                    wifi.setInstlFloor(wifiObject.get("X_SWIFI_INSTL_FLOOR").getAsString());
                    wifi.setInstlTy(wifiObject.get("X_SWIFI_INSTL_TY").getAsString());
                    wifi.setInstlMby(wifiObject.get("X_SWIFI_INSTL_MBY").getAsString());
                    wifi.setSvcSe(wifiObject.get("X_SWIFI_SVC_SE").getAsString());
                    wifi.setCmcwr(wifiObject.get("X_SWIFI_CMCWR").getAsString());
                    wifi.setCnstcYear(wifiObject.get("X_SWIFI_CNSTC_YEAR").getAsString());
                    wifi.setInoutDoor(wifiObject.get("X_SWIFI_INOUT_DOOR").getAsString());
                    wifi.setRemars(wifiObject.get("X_SWIFI_REMARS3").getAsString());
                    wifi.setWifiLAT(Double.parseDouble(wifiObject.get("LAT").getAsString()));
                    wifi.setWifiLNT(Double.parseDouble(wifiObject.get("LNT").getAsString()));
                    wifi.setWorkDttm(wifiObject.get("WORK_DTTM").getAsString());
                    wifiList.add(wifi);
                }
            } else {
                System.out.println("API 호출 실패 : " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wifiList;
    }
}
