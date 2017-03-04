package com.example.android.uamp.PhoneSync;

import android.net.wifi.p2p.WifiP2pDevice;
/**
 * https://android.googlesource.com/platform/development/+/master/samples/WiFiDirectServiceDiscovery/src/com/example/android/wifidirect/discovery/WiFiP2pService.java
 * A structure to hold service information.
 */
public class WiFiP2pService {
    WifiP2pDevice device;
    String instanceName = null;
    String serviceRegistrationType = null;
}
