package com.profusec.firewall.packetwall.socket;

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class PacketUtil {
    public static int extractIPVersion(byte[] bytes) {
        return (bytes[0] & 0xFF) >> 4;
    }

    public static void parsePacket(byte[] packet) {
        final int ipVersion = extractIPVersion(packet);

        if (ipVersion == 4) {
            try {
                parseIPv4Packet(packet);
            } catch (UnknownHostException e) {
                Log.d("analyze", "Error while parsing the packet");
                e.printStackTrace();
            }
        } else if (ipVersion == 6) {
            parseIPv6Packet(packet);
        }
    }

    public static void parseIPv4Packet(byte[] packet) throws UnknownHostException {
        final int firstByte = packet[0] & 0xFF;
        final int version = firstByte >> 4;
        final int headerLength = firstByte & 0x0F;
        final int serviceType = packet[1] & 0xFF;
        final int totalLength = combineBytes(packet[2], packet[3]);
        final int identification = combineBytes(packet[4], packet[5]);
        final String sourceIp = InetAddress.getByAddress(Ipv4Util.extractSourceIpFromIpv4(packet)).getHostAddress();
        final String destinationIp = InetAddress.getByAddress(Ipv4Util.extractDestinationFromIpv4(packet)).getHostAddress();
        Log.d("analyze", "Source IP: " + sourceIp + " Destination IP: " + destinationIp);
    }


    private static int combineBytes(byte a, byte b) {
        return ((a & 0xFF) << 8) + (b & 0xFF);
    }

    public static void parseIPv6Packet(byte[] packet) {

    }
}