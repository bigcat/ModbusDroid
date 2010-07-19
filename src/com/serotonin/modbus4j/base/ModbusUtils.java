package com.serotonin.modbus4j.base;

import com.serotonin.modbus4j.exception.IllegalSlaveIdException;
import com.serotonin.modbus4j.exception.ModbusIdException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

public class ModbusUtils {
    public static final int TCP_PORT = 502;
    public static final int IP_PROTOCOL_ID = 0; // Modbus protocol
    public static final int MAX_READ_BIT_COUNT = 2000;
    public static final int MAX_READ_REGISTER_COUNT = 125;
    public static final int MAX_WRITE_REGISTER_COUNT = 120;

    public static void pushByte(ByteQueue queue, int value) {
        queue.push((byte) value);
    }

    public static void pushShort(ByteQueue queue, int value) {
        queue.push((byte) (0xff & (value >> 8)));
        queue.push((byte) (0xff & value));
    }

    public static int popByte(ByteQueue queue) {
        return queue.pop();
    }

    public static int popUnsignedByte(ByteQueue queue) {
        return queue.pop() & 0xff;
    }

    public static int popShort(ByteQueue queue) {
        return toShort(queue.pop(), queue.pop());
    }

    public static int popUnsignedShort(ByteQueue queue) {
        return ((queue.pop() & 0xff) << 8) | (queue.pop() & 0xff);
    }

    public static short toShort(byte b1, byte b2) {
        return (short) ((b1 << 8) | (b2 & 0xff));
    }

    public static byte toByte(short value, boolean first) {
        if (first)
            return (byte) (0xff & (value >> 8));
        return (byte) (0xff & value);
    }

    public static void validateSlaveId(int slaveId, boolean includeBroadcast) {
        if (slaveId < (includeBroadcast ? 0 : 1) || slaveId > 240)
            throw new IllegalSlaveIdException("Invalid slave id: " + slaveId);
    }

    public static void validateBit(byte bit) {
        if (bit < 0 || bit > 15)
            throw new ModbusIdException("Invalid bit: " + bit);
    }

    public static void validateOffset(int offset) throws ModbusTransportException {
        if (offset < 0 || offset > 65535)
            throw new ModbusTransportException("Invalid offset: " + offset);
    }

    public static void validateEndOffset(int offset) throws ModbusTransportException {
        if (offset > 65535)
            throw new ModbusTransportException("Invalid end offset: " + offset);
    }

    public static void validateNumberOfBits(int bits) throws ModbusTransportException {
        if (bits < 1 || bits > MAX_READ_BIT_COUNT)
            throw new ModbusTransportException("Invalid number of bits: " + bits);
    }

    public static void validateNumberOfRegisters(int registers) throws ModbusTransportException {
        if (registers < 1 || registers > MAX_READ_REGISTER_COUNT)
            throw new ModbusTransportException("Invalid number of registers: " + registers);
    }
}
