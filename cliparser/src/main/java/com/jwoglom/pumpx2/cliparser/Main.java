package com.jwoglom.pumpx2.cliparser;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.jwoglom.pumpx2.pump.messages.Message;
import com.jwoglom.pumpx2.pump.messages.MessageType;
import com.jwoglom.pumpx2.pump.messages.Messages;
import com.jwoglom.pumpx2.pump.messages.PacketArrayList;
import com.jwoglom.pumpx2.pump.messages.bluetooth.BTResponseParser;
import com.jwoglom.pumpx2.pump.messages.bluetooth.Characteristic;
import com.jwoglom.pumpx2.pump.messages.bluetooth.CharacteristicUUID;
import com.jwoglom.pumpx2.pump.messages.bluetooth.PumpStateSupplier;
import com.jwoglom.pumpx2.pump.messages.bluetooth.TronMessageWrapper;
import com.jwoglom.pumpx2.pump.messages.bluetooth.models.PumpResponseMessage;
import com.jwoglom.pumpx2.pump.messages.helpers.Bytes;
import com.jwoglom.pumpx2.pump.messages.response.currentStatus.TimeSinceResetResponse;
import com.jwoglom.pumpx2.shared.L;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Main {
    private static final String TAG = "CLIParser";
    static {
        L.getPrintln = System.err::println;
    }
    public static void main(String[] args) throws DecoderException, IOException {
        if (args.length == 0) {
            System.out.println("Specify a command.");
            return;
        }

        String pumpAuthenticationKey = System.getenv("PUMP_AUTHENTICATION_KEY");
        if (!Strings.isNullOrEmpty(pumpAuthenticationKey)) {
            PumpStateSupplier.authenticationKey = () -> pumpAuthenticationKey;
        }

        String pumpTimeSinceReset = System.getenv("PUMP_TIME_SINCE_RESET");
        if (!Strings.isNullOrEmpty(pumpTimeSinceReset)){
            PumpStateSupplier.pumpTimeSinceReset = () -> Long.valueOf(pumpTimeSinceReset);
        }

        String filename;
        List<String> lines;
        String[] extra = new String[0];
        if (args.length >= 2) {
            extra = Arrays.copyOfRange(args, 2, args.length);
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "opcode":
                System.out.println(parseOpcode(args[1]));
                break;
            case "parse":
                System.out.println(parseFull(args[1], extra));
                break;
            case "guesscargo":
                System.out.println(parseGuessCargo(args[1], extra));
                break;
            case "bulkopcode":
                filename = args[1];
                lines = Files.readAllLines(Path.of(filename));
                for (String line : lines) {
                    System.out.println(line+"\t"+parseOpcode(line));
                }
                break;
            case "bulkparse":
                filename = args[1];
                lines = Files.readAllLines(Path.of(filename));
                for (String line : lines) {
                    System.out.println(line+"\t"+parseFull(line));
                }
                break;
            case "read":
            case "write":
                System.out.println(parse(args[1], extra));
                break;
        }
    }

    private static void assertTrue(String msg, boolean ok) {
        if (!ok) {
            L.w(TAG, "FAIL: " + msg);
        }
    }

    public static String parseOpcode(String rawHex) throws DecoderException {
        byte[] initialRead;
        try {
            initialRead = Hex.decodeHex(rawHex);
        } catch (Exception e) {
            return "Unable to parse: " + e;
        }
        int txId = initialRead[1];
        int opCode = initialRead[2];
        Set<Message> messages = new HashSet<>();
        String messageStr = "";
        try {
            Set<Characteristic> possibilities = Messages.findPossibleCharacteristicsForOpcode(opCode);
            if (possibilities.size() == 0) {
                return "Unknown opcode "+opCode;
            }
            Characteristic characteristic = null;
            for (Characteristic c : possibilities) {
                Message message = Messages.fromOpcode(opCode, c).newInstance();
                message.fillWithEmptyCargo();
                messages.add(message);
                if (messageStr.length() > 0) {
                    messageStr += " <OR> ";
                }
                messageStr += message.getClass().getName().replace("com.jwoglom.pumpx2.pump.messages.", "");
            }
        } catch (NullPointerException e) {
            return "Unknown opcode "+opCode;
        } catch (InstantiationException|IllegalAccessException e) {
            e.printStackTrace();
        }

        return opCode+"\t"+messageStr;
    }

    public static String parseFull(String rawHex, String ...extra) throws DecoderException {
        String ret = parseOpcode(rawHex);
        System.err.println("parse with "+rawHex+" "+ String.join(",", extra));
        try {
            Message message = parse(rawHex, extra);
            return ret+"\t"+message.toString();
        } catch (Exception e) {
            return ret+"\t"+e.toString();
        }
    }

    public static String parseGuessCargo(String rawHex, String ...extra) throws DecoderException {
        String ret = parseOpcode(rawHex);
        System.err.println("parse with "+rawHex+" "+ String.join(",", extra));
        try {
            Message message = parse(rawHex, extra);
            return guessCargo(message);
        } catch (Exception e) {
            return ret+"\t"+e.toString();
        }
    }

    public static String guessCargo(Message message) {
        byte[] cargo = message.getCargo();
        int max = message.props().size();
        String ret = "";
        for (int i=0; i<max; i++) {
            ret += (i+" "+cargo[i]+"\t");
            if (i+4 <= max) {ret += ("uint32: "+ Bytes.readUint32(cargo, i)+"\t");}
            if (i+2 <= max) {ret += ("short: "+Bytes.readShort(cargo,i)+"\t");}
            if (i+4 <= max) {ret += ("float: "+Bytes.readFloat(cargo, i)+"\t");}
            ret += "\n";
        }
        return ret;
    }

    @SuppressWarnings("deprecation")
    public static Message parse(String rawHex, String ...extra) throws DecoderException {
        byte[] initialRead = Hex.decodeHex(rawHex);
        int txId = initialRead[1];
        int opCode = initialRead[2];
        Message message = null;
        try {
            Set<Characteristic> possibilities = Messages.findPossibleCharacteristicsForOpcode(opCode);
            if (possibilities.size() > 1) {
                System.err.print("Multiple characteristics possible for opCode: "+opCode+": "+possibilities);
                if (possibilities.contains(Characteristic.CONTROL)) {
                    System.err.print("Using CONTROL");
                    possibilities = ImmutableSet.of(Characteristic.CONTROL);
                } else if (possibilities.contains(Characteristic.CURRENT_STATUS)) {
                    System.err.print("Using CURRENT_STATUS");
                    possibilities = ImmutableSet.of(Characteristic.CURRENT_STATUS);
                } else {
                    return null;
                }
            }
            if (possibilities.size() == 0) {
                System.err.print("Unknown opcode "+opCode);
                return null;
            }
            Characteristic characteristic = null;
            for (Characteristic c : possibilities) {
                characteristic = c;
            }
            message = Messages.fromOpcode(opCode, characteristic).newInstance();
            message.fillWithEmptyCargo();
        } catch (NullPointerException e) {
            System.err.print("NPE for opcode "+opCode);
            return null;
        } catch (InstantiationException|IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            return testRequest(rawHex, txId, message, extra);
        } catch (IllegalStateException e) {
            System.err.print("Needs more packets"); // for "+opCode+": "+e.getMessage());
            return null;
        }
    }

    public static Message testRequest(String rawHex, int txId, Message expected, String ...extraRawHex) throws DecoderException {
        byte[] initialRead = Hex.decodeHex(rawHex);

        UUID uuid = CharacteristicUUID.determine(expected);
        MessageType messageType = expected.type();

        TronMessageWrapper tron = new TronMessageWrapper(expected, (byte) txId);
        PacketArrayList packetArrayList = tron.buildPacketArrayList(messageType);
        PumpResponseMessage resp = BTResponseParser.parse(tron.message(), packetArrayList, initialRead, uuid);

        for (String exRawHex : extraRawHex) {
            System.err.print("extraRawHex: "+exRawHex);
            byte[] nextRead = Hex.decodeHex(exRawHex);
            resp = BTResponseParser.parse(tron.message(), packetArrayList, nextRead, uuid);
        }

        assertTrue("Response message returned from parser: " + resp, resp.message().isPresent());

        if (!resp.message().isPresent()) {
            System.err.print("Message not fully formed");

            // Hack to split messages which contain two packets into individual packets for parsing
            if (!rawHex.substring(0, 2).equals("00") && extraRawHex.length == 0) {
                String op = rawHex.substring(2, 4);
                int num = Integer.parseInt(rawHex.substring(0, 2), 16);
                List<String> messages = new ArrayList<>();
                String remainder = rawHex;
                System.err.println("rawHex: "+remainder);
                for (int i=num-1; i>=0; i--) {
                    System.err.println("i="+i+" num="+num);
                    String spl = Integer.toHexString(i) + op;
                    if (spl.length() == 3) {
                        spl = "0"+spl;
                    }
                    System.err.println("spl:"+spl);
                    String[] split = remainder.split(spl);
                    messages.add(split[0]);
                    System.err.println("messages:"+messages);
                    if (split.length > 1) {
                        remainder = spl + split[1];
                    } else {
                        remainder = "";
                    }
                    System.err.println("remainder: "+remainder);
                }
                if (!remainder.isEmpty()) {
                    messages.add(remainder);
                }
                String firstRawHex = messages.remove(0);
                String[] newExtra = messages.toArray(new String[0]);
                return testRequest(firstRawHex, txId, expected, newExtra);
            }
            return null;
        }
        Message parsedMessage = resp.message().get();
        L.w(TAG, String.format("Parsed: %s\nExpected: %s", parsedMessage, expected));
        assertTrue("classes match", expected.getClass().equals(parsedMessage.getClass()));

        int numPackets = tron.packets().size();
        byte[] mergedPackets = tron.mergeIntoSinglePacket().build();

        if (parsedMessage instanceof TimeSinceResetResponse) {
            TimeSinceResetResponse tsr = (TimeSinceResetResponse) parsedMessage;
            PumpStateSupplier.pumpTimeSinceReset = tsr::getPumpTimeSeconds;
        }

        return parsedMessage;
    }
}