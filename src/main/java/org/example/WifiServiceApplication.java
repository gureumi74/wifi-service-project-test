package org.example;

import org.example.domain.location.Location;
import org.example.domain.location.LocationService;
import org.example.domain.wifi.WifiService;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class WifiServiceApplication {
    public static void main(String[] args) {
        WifiService wifiService = new WifiService();
        // 일단 lat, lnt 정보를 수동으로 입력해서 test
        // 추후 기능 추가
        wifiService.wifiSave(37.537431, 126.836736);
    }
}