package com.serotonin.modbus4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.modbus4j.base.RangeAndOffset;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.IllegalDataAddressException;
import com.serotonin.modbus4j.exception.ModbusIdException;

public class BasicProcessImage implements ProcessImage {
    private final Map<Integer, Boolean> coils = new HashMap<Integer, Boolean>();
    private final Map<Integer, Boolean> inputs = new HashMap<Integer, Boolean>();
    private final Map<Integer, Short> holdingRegisters = new HashMap<Integer, Short>();
    private final Map<Integer, Short> inputRegisters = new HashMap<Integer, Short>();
    private final List<ProcessImageListener> writeListeners = new ArrayList<ProcessImageListener>();
    private byte exceptionStatus;
    
    public synchronized void addListener(ProcessImageListener l) {
        writeListeners.add(l);
    }
    
    public synchronized void removeListener(ProcessImageListener l) {
        writeListeners.remove(l);
    }
    
    //
    ///
    /// Additional convenience methods.
    ///
    //
    public void setExceptionStatus(byte exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
    }
    
    //
    // Binaries
    public void setBinary(int registerId, boolean value) {
        RangeAndOffset rao = new RangeAndOffset(registerId);
        
        if (rao.getRange() == RegisterRange.COIL_STATUS)
            setCoil(rao.getOffset(), value);
        else if (rao.getRange() == RegisterRange.INPUT_STATUS)
            setInput(rao.getOffset(), value);
        else
            throw new ModbusIdException("Invalid registerId to set binary: "+ registerId);
    }
    
    //
    // Numerics
    public synchronized void setRegister(int range, int offset, int dataType, Number value) {
        // Write the value.
        if (range == RegisterRange.HOLDING_REGISTER)
            setHoldingRegister(offset, dataType, value);
        else if (range == RegisterRange.INPUT_REGISTER)
            setInputRegister(offset, dataType, value);
        else
            throw new ModbusIdException("Invalid range to set register: "+ range);
    }
    
    public synchronized void setHoldingRegister(int offset, int dataType, Number value) {
        validateOffset(offset);
        short[] registers = ModbusLocator.valueToShorts(value, dataType);
        for (int i=0; i<registers.length; i++)
            setHoldingRegister(offset + i, registers[i]);
    }
    
    public synchronized void setInputRegister(int offset, int dataType, Number value) {
        validateOffset(offset);
        short[] registers = ModbusLocator.valueToShorts(value, dataType);
        for (int i=0; i<registers.length; i++)
            setInputRegister(offset + i, registers[i]);
    }
    
    //
    // Bits
    public synchronized void setBit(int range, int offset, int bit, boolean value) {
        if (range == RegisterRange.HOLDING_REGISTER)
            setHoldingRegisterBit(offset, bit, value);
        else if (range == RegisterRange.INPUT_REGISTER)
            setInputRegisterBit(offset, bit, value);
        else
            throw new ModbusIdException("Invalid range to set register: "+ range);
    }
    
    public synchronized void setHoldingRegisterBit(int offset, int bit, boolean value) {
        validateBit(bit);
        short s;
        try {
            s = getHoldingRegister(offset);
        }
        catch (IllegalDataAddressException e) {
            s = 0;
        }
        setHoldingRegister(offset, setBit(s, bit, value));
    }

    public synchronized void setInputRegisterBit(int offset, int bit, boolean value) {
        validateBit(bit);
        short s;
        try {
            s = getInputRegister(offset);
        }
        catch (IllegalDataAddressException e) {
            s = 0;
        }
        setInputRegister(offset, setBit(s, bit, value));
    }

    public boolean getBit(int range, int offset, int bit) throws IllegalDataAddressException {
        if (range == RegisterRange.HOLDING_REGISTER)
            return getHoldingRegisterBit(offset, bit);
        if (range == RegisterRange.INPUT_REGISTER)
            return getInputRegisterBit(offset, bit);
        throw new ModbusIdException("Invalid range to get register: "+ range);
    }
    
    public boolean getHoldingRegisterBit(int offset, int bit) throws IllegalDataAddressException {
        validateBit(bit);
        return getBit(getHoldingRegister(offset), bit);
    }

    public boolean getInputRegisterBit(int offset, int bit) throws IllegalDataAddressException {
        validateBit(bit);
        return getBit(getInputRegister(offset), bit);
    }

    
    
    
    
    //
    ///
    /// ProcessImage interface
    ///
    //
    
    //
    // Coils
    public synchronized boolean getCoil(int offset) throws IllegalDataAddressException {
        return getBoolean(offset, coils);
    }

    public synchronized void setCoil(int offset, boolean value) {
        validateOffset(offset);
        coils.put(offset, value);
    }
    
    public synchronized void writeCoil(int offset, boolean value) throws IllegalDataAddressException {
        boolean old = getBoolean(offset, coils);
        setCoil(offset, value);
        
        for (ProcessImageListener l : writeListeners)
            l.coilWrite(offset, old, value);
    }

    //
    // Inputs
    public synchronized boolean getInput(int offset) throws IllegalDataAddressException {
        return getBoolean(offset, inputs);
    }

    public synchronized void setInput(int offset, boolean value) {
        validateOffset(offset);
        inputs.put(offset, value);
    }
    
    //
    // Holding registers
    public synchronized short getHoldingRegister(int offset) throws IllegalDataAddressException {
        return getShort(offset, holdingRegisters);
    }

    public synchronized void setHoldingRegister(int offset, short value) {
        validateOffset(offset);
        holdingRegisters.put(offset, value);
    }

    public synchronized void writeHoldingRegister(int offset, short value) throws IllegalDataAddressException {
        short old = getShort(offset, holdingRegisters);
        setHoldingRegister(offset, value);
        
        for (ProcessImageListener l : writeListeners)
            l.holdingRegisterWrite(offset, old, value);
    }
    
    //
    // Input registers
    public synchronized short getInputRegister(int offset) throws IllegalDataAddressException {
        return getShort(offset, inputRegisters);
    }

    public synchronized void setInputRegister(int offset, short value) {
        validateOffset(offset);
        inputRegisters.put(offset, value);
    }

    
    //
    // Exception status
    public byte getExceptionStatus() {
        return exceptionStatus;
    }

    
    //
    // Report slave id
    public byte[] getReportSlaveIdData() {
        return new byte[0];
    }

    
    
    //
    ///
    /// Private
    ///
    //
    private short getShort(int offset, Map<Integer, Short> map) throws IllegalDataAddressException {
        Short value = map.get(offset);
        if (value == null)
            throw new IllegalDataAddressException();
        return value.shortValue();
    }
    
    private boolean getBoolean(int offset, Map<Integer, Boolean> map) throws IllegalDataAddressException {
        Boolean value = map.get(offset);
        if (value == null)
            throw new IllegalDataAddressException();
        return value.booleanValue();
    }
    
    private void validateOffset(int offset) {
        if (offset < 0 || offset > 65535)
            throw new ModbusIdException("Invalid offset: "+ offset);
    }
    
    private void validateBit(int bit) {
        if (bit < 0 || bit > 15)
            throw new ModbusIdException("Invalid bit: "+ bit);
    }
    
    private short setBit(short s, int bit, boolean value) {
        return (short)(s | ((value ? 1 : 0) << bit));
    }
    
    private boolean getBit(short s, int bit) {
        return ((s >> bit) & 0x1) == 1;
    }
}
