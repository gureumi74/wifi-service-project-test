package org.example.domain.wifi;

import org.example.domain.location.LocationService;

import java.sql.*;
import java.util.Comparator;
import java.util.List;

public class WifiService {
    // 가까운 wifi 20개를 저장
    public List<Wifi> wifiSave(double lat, double lnt) {
        LocationService locationService = new LocationService();
        String url = locationService.sqliteDriveLoad();

        // 전체 api 정보 불러오기
        WifiApiService wifiApiService = new WifiApiService();

        List<Wifi> wifiList = wifiApiService.wifiApiGet();

        for (int i = 0; i < wifiList.size(); i++) {
            Wifi wifi = wifiList.get(i);
            wifi.setDistance(getDistance(lat, lnt, wifi.getWifiLAT(), wifi.getWifiLNT()));
            wifi.setSavedAt(new Timestamp(System.currentTimeMillis()));
        }

        wifiList.sort(Comparator.comparingDouble(Wifi::getDistance));

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        for (int i = 0; i < 20; i++) {
            // 커넥션 객체 생성
            try {
                Wifi wifi = wifiList.get(i);
                connection = DriverManager.getConnection(url);

                // sql 쿼리
                String sql = "insert into wifi_info (DISTANCE, X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, " +
                        "X_SWIFI_ADRES1, X_SWIFI_ADRES2, X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY, " +
                        "X_SWIFI_SVC_SE, X_SWIFI_CMCWR, X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, X_SWIFI_REMARS3, LAT, LNT, WORK_DTTM, SAVED_AT)" +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDouble(1, wifi.getDistance());
                preparedStatement.setString(2, wifi.getMgrNo());
                preparedStatement.setString(3, wifi.getWrdofc());
                preparedStatement.setString(4, wifi.getWifiName());
                preparedStatement.setString(5, wifi.getAddress1());
                preparedStatement.setString(6, wifi.getAddress2());
                preparedStatement.setString(7, wifi.getInstlFloor());
                preparedStatement.setString(8, wifi.getInstlTy());
                preparedStatement.setString(9, wifi.getInstlMby());
                preparedStatement.setString(10, wifi.getSvcSe());
                preparedStatement.setString(11, wifi.getCmcwr());
                preparedStatement.setString(12, wifi.getCnstcYear());
                preparedStatement.setString(13, wifi.getInoutDoor());
                preparedStatement.setString(14, wifi.getRemars());
                preparedStatement.setDouble(15, wifi.getWifiLAT());
                preparedStatement.setDouble(16, wifi.getWifiLNT());
                preparedStatement.setString(17, wifi.getWorkDttm());
                preparedStatement.setTimestamp(18, wifi.getSavedAt());

                // 쿼리 실행
                int affected = preparedStatement.executeUpdate();

                if (affected > 0) {
                    System.out.println(i + "번 데이터 저장 성공");
                } else {
                    System.out.println(i + "번 데이터 저장 실패");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null && !preparedStatement.isClosed()) {
                        preparedStatement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return wifiList;
    }

    // 두 좌표의 거리 구하는 기능
    public double getDistance(double lat, double lnt, double wifiLat, double wifiLnt) {
        final double R = 6371; // 지구 반지름

        double latDistance = Math.toRadians(wifiLat - lat);
        double lntDistance = Math.toRadians(wifiLnt - lnt);

        double distance = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(wifiLat))
                * Math.sin(lntDistance / 2) * Math.sin(lntDistance / 2);

        distance = 2 * Math.atan2(Math.sqrt(distance), Math.sqrt(1 - distance));

        return R * distance;
    }
}
