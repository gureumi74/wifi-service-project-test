package org.example;

import org.example.domain.location.Location;
import org.example.domain.location.LocationService;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class WifiServiceApplication {
    public static void main(String[] args) {
        LocationService locationService = new LocationService();
        List<Location> locationList = locationService.viewList();
    }
}