package com.example.android.uamp.PhoneSync;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by loyd on 2/21/2017.
 */

public class PhoneSync {

    WifiP2pManager mManager;
    WifiP2pManager.Channel channel;
    private static final char[] SERVER_PORT = {7070};
    final HashMap<String, String> buddies = new HashMap<String, String>();


    private void startRegistration() {
        //  Create a string map containing information about your service.
        Map record = new HashMap();
        record.put("listenport", String.valueOf(SERVER_PORT));
        record.put("buddyname", "John Doe" + (int) (Math.random() * 1000));
        record.put("available", "visible");

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record);

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        mManager.addLocalService(channel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                // Unless you want to update the UI or add logging statements.
            }

            @Override
            public void onFailure(int arg0) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
            }
        });
    }

    WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
        @Override
        /* Callback includes:
         * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
         * record: TXT record dta as a map of key/value pairs.
         * device: The device running the advertised service.
         */

        public void onDnsSdTxtRecordAvailable(
                String fullDomain, Map record, WifiP2pDevice device) {
            Log.d(TAG, "DnsSdTxtRecord available -" + record.toString());
            buddies.put(device.deviceAddress, (String) record.get("buddyname"));
        }
    };

    WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
        @Override
        public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                            WifiP2pDevice resourceType) {

            // Update the device name with the human-friendly version from
            // the DnsTxtRecord, assuming one arrived.
            resourceType.deviceName = buddies
                    .containsKey(resourceType.deviceAddress) ? buddies
                    .get(resourceType.deviceAddress) : resourceType.deviceName;

            // Add to the custom adapter defined specifically for showing
            // wifi devices.
//            WiFiDirectServicesList fragment = (WiFiDirectServicesList) getFragmentManager().findFragmentById(R.id.frag_peerlist);
//            WiFiDirectServicesList.WiFiDevicesAdapter adapter = ((WiFiDirectServicesList.WiFiDevicesAdapter) fragment
//                    .getListAdapter());
//
//            adapter.add(resourceType);
//            adapter.notifyDataSetChanged();
            Log.d(TAG, "onBonjourServiceAvailable " + instanceName);
        }
    };

//    mManager.setDnsSdResponseListeners(channel, servListener, txtListener);
//
//    serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
//    mManager.addServiceRequest(channel,
//    serviceRequest,
//            new ActionListener() {
//        @Override
//        public void onSuccess() {
//            // Success!
//        }
//
//        @Override
//        public void onFailure(int code) {
//            // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
//        }
//    });
//
//    mManager.discoverServices(channel, new ActionListener() {
//
//        @Override
//        public void onSuccess() {
//            // Success!
//        }
//
//        @Override
//        public void onFailure (int code) {
//            // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
//            if (code == WifiP2pManager.P2P_UNSUPPORTED) {
//                Log.d(TAG, "P2P isn't supported on this device.");
////                else if(...)
////                ...
//            }
//        });


//}
}