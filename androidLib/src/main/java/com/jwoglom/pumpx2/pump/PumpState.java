package com.jwoglom.pumpx2.pump;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import com.jwoglom.pumpx2.pump.messages.Message;
import com.jwoglom.pumpx2.pump.messages.bluetooth.PumpStateSupplier;
import com.jwoglom.pumpx2.pump.messages.models.ApiVersion;

import java.util.LinkedList;
import java.util.Queue;

public class PumpState {
    // TODO: Refactor this class to be less hacky.

    static {
        PumpStateSupplier.authenticationKey = PumpState::getAuthenticationKey;
        PumpStateSupplier.pumpTimeSinceReset = PumpState::getPumpTimeSinceReset;
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences("PumpState", Context.MODE_PRIVATE);
    }

    // The pairing code is also called the authentication key
    private static final String PAIRING_CODE_PREF = "pairingCode";
    public static String savedAuthenticationKey = null;
    public static void setPairingCode(Context context, String pairingCode) {
        prefs(context).edit().putString(PAIRING_CODE_PREF, pairingCode).apply();
        savedAuthenticationKey = pairingCode;
    }

    public static String getPairingCode(Context context) {
        return prefs(context).getString(PAIRING_CODE_PREF, null);
    }

    public static String getAuthenticationKey() {
        return savedAuthenticationKey;
    }

    public static long timeSinceReset = 0;

    // This is used during packet generation for signed messages,
    // and is filled by calling TimeSinceResetRequest
    public static Long pumpTimeSinceReset = null;
    // This is the time at which pumpTimeSinceReset was fetched
    public static Long selfTimeSinceReset = null;

    public static Long getPumpTimeSinceReset() {
        return pumpTimeSinceReset;
    }


    /**
     * @return the pumpTimeSinceReset we would expect the pump to have right now
     * given when it was last fetched
     */
    public static Long getAdjustedPumpTimeSinceReset() {
        return pumpTimeSinceReset + (System.currentTimeMillis() - selfTimeSinceReset)/1000;
    }

    public static void setPumpTimeSinceReset(long time) {
        pumpTimeSinceReset = time;
        selfTimeSinceReset = System.currentTimeMillis();
    }

    public static int failedPumpConnectionAttempts = 0;


    // The most recent Bluetooth MAC of the connected pump
    private static final String SAVED_BLUETOOTH_MAC_PREF = "savedBluetoothMAC";
    public static void setSavedBluetoothMAC(Context context, String bluetoothMAC) {
        prefs(context).edit().putString(SAVED_BLUETOOTH_MAC_PREF, bluetoothMAC).apply();
    }

    public static String getSavedBluetoothMAC(Context context) {
        return prefs(context).getString(SAVED_BLUETOOTH_MAC_PREF, null);
    }


    // The (major, minor) pump version returned from ApiVersionResponse
    private static final String PUMP_MAJOR_API_VERSION_PREF = "pumpMajorApiVersion";
    private static final String PUMP_MINOR_API_VERSION_PREF = "pumpMinorApiVersion";
    public static void setPumpAPIVersion(Context context, ApiVersion apiVersion) {
        prefs(context).edit()
                .putInt(PUMP_MAJOR_API_VERSION_PREF, apiVersion.getMajor())
                .putInt(PUMP_MINOR_API_VERSION_PREF, apiVersion.getMinor())
                .apply();
    }

    public static ApiVersion getPumpAPIVersion(Context context) {
        int major = prefs(context).getInt(PUMP_MAJOR_API_VERSION_PREF, 0);
        int minor = prefs(context).getInt(PUMP_MINOR_API_VERSION_PREF, 0);
        return new ApiVersion(major, minor);
    }

    // The state of recent messages sent to the pump paired with the transaction id.
    private static Queue<Pair<Message, Byte>> requestMessages = new LinkedList<>();
    public static synchronized void pushRequestMessage(Message m, byte txId) {
        requestMessages.add(Pair.create(m, txId));
    }

    public static synchronized Pair<Message, Byte> popRequestMessage() {
        return requestMessages.poll();
    }
}
