package com.jwoglom.pumpx2.pump.messages;

import com.google.common.base.Preconditions;
import com.jwoglom.pumpx2.pump.messages.annotations.MessageProps;
import com.jwoglom.pumpx2.pump.messages.bluetooth.Characteristic;
import com.jwoglom.pumpx2.pump.messages.request.authentication.CentralChallengeRequest;
import com.jwoglom.pumpx2.pump.messages.request.authentication.PumpChallengeRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.AlarmStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.AlertStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.ApiVersionRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.BasalIQAlertInfoRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.BasalIQSettingsRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.BasalIQStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.BasalLimitSettingsRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CGMAlertStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CGMGlucoseAlertSettingsRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CGMHardwareInfoRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CGMOORAlertSettingsRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CGMRateAlertSettingsRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CGMStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.ControlIQIOBRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.ControlIQInfoV1Request;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.ControlIQInfoV2Request;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.ControlIQSleepScheduleRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CurrentBasalStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CurrentBatteryV1Request;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CurrentBatteryV2Request;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CurrentBolusStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CurrentEGVGuiDataRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.ExtendedBolusStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.GlobalMaxBolusSettingsRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.HistoryLogRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.HistoryLogStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.HomeScreenMirrorRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.IDPSegmentRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.InsulinStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.LastBGRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.NonControlIQIOBRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.ProfileStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.PumpFeaturesV1Request;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.PumpFeaturesV2Request;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.PumpGlobalsRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.PumpSettingsRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.PumpVersionRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.ReminderStatusRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.RemindersRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.TempRateRequest;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.TimeSinceResetRequest;
import com.jwoglom.pumpx2.pump.messages.request.historyLog.NonexistentHistoryLogStreamRequest;
import com.jwoglom.pumpx2.pump.messages.response.ErrorResponse;
import com.jwoglom.pumpx2.pump.messages.response.authentication.CentralChallengeResponse;
import com.jwoglom.pumpx2.pump.messages.response.authentication.PumpChallengeResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.AlarmStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.AlertStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.ApiVersionResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.BasalIQAlertInfoResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.BasalIQSettingsResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.BasalIQStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.BasalLimitSettingsResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CGMAlertStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CGMGlucoseAlertSettingsResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CGMHardwareInfoResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CGMOORAlertSettingsResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CGMRateAlertSettingsResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CGMStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.ControlIQIOBResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.ControlIQInfoV1Response;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.ControlIQInfoV2Response;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.ControlIQSleepScheduleResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CurrentBasalStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CurrentBatteryV1Response;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CurrentBatteryV2Response;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CurrentBolusStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CurrentEGVGuiDataResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.ExtendedBolusStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.GlobalMaxBolusSettingsResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.HistoryLogResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.HistoryLogStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.HomeScreenMirrorResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.IDPSegmentResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.InsulinStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.LastBGResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.NonControlIQIOBResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.ProfileStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.PumpFeaturesV1Response;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.PumpFeaturesV2Response;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.PumpGlobalsResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.PumpSettingsResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.PumpVersionResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.ReminderStatusResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.RemindersResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.TempRateResponse;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.TimeSinceResetResponse;
import com.jwoglom.pumpx2.pump.messages.response.historyLog.HistoryLogStreamResponse;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.BolusCalcDataSnapshotRequest;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.BolusCalcDataSnapshotResponse;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.BolusPermissionChangeReasonRequest;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.BolusPermissionChangeReasonResponse;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.LastBolusStatusV2Request;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.LastBolusStatusV2Response;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.IDPSettingsRequest;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.IDPSettingsResponse;
import com.jwoglom.pumpx2.pump.messages.request.control.ChangeCartridgeRequest;
import com.jwoglom.pumpx2.pump.messages.response.control.ChangeCartridgeResponse;
import com.jwoglom.pumpx2.pump.messages.request.control.InitiateBolusRequest;
import com.jwoglom.pumpx2.pump.messages.response.control.InitiateBolusResponse;
import com.jwoglom.pumpx2.pump.messages.request.control.BolusPermissionRequest;
import com.jwoglom.pumpx2.pump.messages.response.control.BolusPermissionResponse;
import com.jwoglom.pumpx2.pump.messages.request.currentStatus.CommonSoftwareInfoRequest;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.CommonSoftwareInfoResponse;
// IMPORT_END
import com.jwoglom.pumpx2.shared.L;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public enum Messages {
    API_VERSION(ApiVersionRequest.class, ApiVersionResponse.class),
    CENTRAL_CHALLENGE(CentralChallengeRequest.class, CentralChallengeResponse.class),
    PUMP_CHALLENGE(PumpChallengeRequest.class, PumpChallengeResponse.class),
    ALARM_STATUS(AlarmStatusRequest.class, AlarmStatusResponse.class),
    ALERT_STATUS(AlertStatusRequest.class, AlertStatusResponse.class),
    CGM_HARDWARE_INFO(CGMHardwareInfoRequest.class, CGMHardwareInfoResponse.class),
    CONTROL_IQ_IOB(ControlIQIOBRequest.class, ControlIQIOBResponse.class),
    NON_CONTROL_IQ_IOB(NonControlIQIOBRequest.class, NonControlIQIOBResponse.class),
    PUMP_FEATURES(PumpFeaturesV1Request.class, PumpFeaturesV1Response.class),
    PUMP_GLOBALS(PumpGlobalsRequest.class, PumpGlobalsResponse.class),
    PUMP_SETTINGS(PumpSettingsRequest.class, PumpSettingsResponse.class),
    CGM_STATUS(CGMStatusRequest.class, CGMStatusResponse.class),
    CURRENT_BATTERY_V1(CurrentBatteryV1Request.class, CurrentBatteryV1Response.class),
    CURRENT_BATTERY_V2(CurrentBatteryV2Request.class, CurrentBatteryV2Response.class),
    INSULIN_STATUS(InsulinStatusRequest.class, InsulinStatusResponse.class),
    LAST_BG(LastBGRequest.class, LastBGResponse.class),
    CURRENT_BOLUS_STATUS(CurrentBolusStatusRequest.class, CurrentBolusStatusResponse.class),
    CURRENT_BASAL_STATUS(CurrentBasalStatusRequest.class, CurrentBasalStatusResponse.class),
    CONTROL_IQ_SLEEP_SCHEDULE(ControlIQSleepScheduleRequest.class, ControlIQSleepScheduleResponse.class),
    CONTROL_IQ_INFO_V1(ControlIQInfoV1Request.class, ControlIQInfoV1Response.class),
    CONTROL_IQ_INFO_V2(ControlIQInfoV2Request.class, ControlIQInfoV2Response.class),
    CGM_ALERT_STATUS(CGMAlertStatusRequest.class, CGMAlertStatusResponse.class),
    CGM_GLUCOSE_ALERT_SETTINGS(CGMGlucoseAlertSettingsRequest.class, CGMGlucoseAlertSettingsResponse.class),
    CGMOOR_ALERT_SETTINGS(CGMOORAlertSettingsRequest.class, CGMOORAlertSettingsResponse.class),
    CGM_RATE_ALERT_SETTINGS(CGMRateAlertSettingsRequest.class, CGMRateAlertSettingsResponse.class),
    CURRENT_EGV_GUI_DATA(CurrentEGVGuiDataRequest.class, CurrentEGVGuiDataResponse.class),
    GLOBAL_MAX_BOLUS_SETTINGS(GlobalMaxBolusSettingsRequest.class, GlobalMaxBolusSettingsResponse.class),
    BASAL_LIMIT_SETTINGS(BasalLimitSettingsRequest.class, BasalLimitSettingsResponse.class),
    PUMP_VERSION(PumpVersionRequest.class, PumpVersionResponse.class),
    PUMP_SUPPORTED_FEATURES(PumpFeaturesV2Request.class, PumpFeaturesV2Response.class),
    REMINDER_STATUS(ReminderStatusRequest.class, ReminderStatusResponse.class),
    REMINDERS(RemindersRequest.class, RemindersResponse.class),
    TEMP_RATE(TempRateRequest.class, TempRateResponse.class),
    TIME_SINCE_RESET(TimeSinceResetRequest.class, TimeSinceResetResponse.class),
    HOME_SCREEN_MIRROR(HomeScreenMirrorRequest.class, HomeScreenMirrorResponse.class),
    PROFILE_STATUS(ProfileStatusRequest.class, ProfileStatusResponse.class),
    IDP_SEGMENT(IDPSegmentRequest.class, IDPSegmentResponse.class),
    EXTENDED_BOLUS_STATUS(ExtendedBolusStatusRequest.class, ExtendedBolusStatusResponse.class),
    BASAL_IQ_ALERT_INFO(BasalIQAlertInfoRequest.class, BasalIQAlertInfoResponse.class),
    BASAL_IQ_SETTINGS(BasalIQSettingsRequest.class, BasalIQSettingsResponse.class),
    BASAL_IQ_STATUS(BasalIQStatusRequest.class, BasalIQStatusResponse.class),
    HISTORY_LOG(HistoryLogRequest.class, HistoryLogResponse.class),
    HISTORY_LOG_STREAM(NonexistentHistoryLogStreamRequest.class, HistoryLogStreamResponse.class),
    HISTORY_LOG_STATUS(HistoryLogStatusRequest.class, HistoryLogStatusResponse.class),
    BOLUS_CALC_DATA_SNAPSHOT(BolusCalcDataSnapshotRequest.class, BolusCalcDataSnapshotResponse.class),
    BOLUS_PERMISSION_CHANGE_REASON(BolusPermissionChangeReasonRequest.class, BolusPermissionChangeReasonResponse.class),
    LAST_BOLUS_STATUS_V2(LastBolusStatusV2Request.class, LastBolusStatusV2Response.class),
    IDP_SETTINGS(IDPSettingsRequest.class, IDPSettingsResponse.class),
    CHANGE_CARTRIDGE(ChangeCartridgeRequest.class, ChangeCartridgeResponse.class),
    INITIATE_BOLUS(InitiateBolusRequest.class, InitiateBolusResponse.class),
    BOLUS_PERMISSION(BolusPermissionRequest.class, BolusPermissionResponse.class),
    COMMON_SOFTWARE_INFO(CommonSoftwareInfoRequest.class, CommonSoftwareInfoResponse.class),
    // MESSAGES_END
    ;

    private static final String TAG = "X2-Messages";

    public static Map<Pair<Characteristic, Integer>, Class<? extends Message>> OPCODES = new HashMap<>();
    public static Map<Integer, Messages> REQUESTS = new HashMap<>();
    public static Map<Integer, Messages> RESPONSES = new HashMap<>();

    static {
        for (Messages m : Messages.values()) {
            OPCODES.put(Pair.of(m.requestProps().characteristic(), m.requestOpCode), m.requestClass);
            REQUESTS.put(m.requestOpCode, m);

            OPCODES.put(Pair.of(m.responseProps().characteristic(), m.responseOpCode), m.responseClass);
            RESPONSES.put(m.responseOpCode, m);

            Preconditions.checkArgument(m.requestProps().minApi().equals(m.responseProps().minApi()), "the minApi should match for " + m);
        }

        for (Characteristic c : Characteristic.values()) {
            int errorOpcode = new ErrorResponse().opCode();
            if (!OPCODES.containsKey(Pair.of(c, errorOpcode))) {
                OPCODES.put(Pair.of(c, errorOpcode), ErrorResponse.class);
            }
        }
    }

    public static Message parse(byte[] data, int opCode, Characteristic characteristic) {
        try {
            Class<? extends Message> clazz = OPCODES.get(Pair.of(characteristic, opCode));
            if (clazz == null) {
                L.w(TAG, "Unable to find message for opCode: " + opCode +" for " + characteristic + " with data: " + Hex.encodeHexString(data));
                return null;
            }
            Message msg = clazz.newInstance();
            msg.parse(data);
            return msg;
        } catch (Exception e) {
            L.w(TAG, "Unable to invoke parse of data: " + Hex.encodeHexString(data) + " opCode: " + opCode + " " + characteristic);
            e.printStackTrace();
            return null;
        }
    }

    public static Set<Characteristic> findPossibleCharacteristicsForOpcode(int opCode) {
        Set<Characteristic> items = new HashSet<>();
        for (Pair<Characteristic, Integer> entry : OPCODES.keySet()) {
            if (entry.getRight().equals(opCode)) {
                items.add(entry.getLeft());
            }
        }

        return items;
    }

    public static Class<? extends Message> fromOpcode(int opCode, Characteristic characteristic) {
        return OPCODES.get(Pair.of(characteristic, opCode));
    }

    private final int requestOpCode;
    private final Class<? extends Message> requestClass;
    private final int responseOpCode;
    private final Class<? extends Message> responseClass;
    Messages(Class<? extends Message> requestClass, Class<? extends Message> responseClass) {
        this.requestClass = requestClass;
        this.requestOpCode = requestProps().opCode();
        this.responseClass = responseClass;
        this.responseOpCode = responseProps().opCode();
    }

    public int requestOpCode() {
        return requestOpCode;
    }
    public Class<? extends Message> requestClass() {
        return requestClass;
    }
    public MessageProps requestProps() {
        return requestClass.getAnnotation(MessageProps.class);
    }

    public int responseOpCode() {
        return responseOpCode;
    }
    public Class<? extends Message> responseClass() {
        return responseClass;
    }
    public MessageProps responseProps() {
        return responseClass.getAnnotation(MessageProps.class);
    }

    public Message request() {
        try {
            return requestClass.newInstance();
        } catch (IllegalAccessException | InstantiationException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Message response() {
        try {
            return responseClass.newInstance();
        } catch (IllegalAccessException | InstantiationException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }
}
