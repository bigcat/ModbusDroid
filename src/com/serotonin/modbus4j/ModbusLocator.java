package com.serotonin.modbus4j;

import java.math.BigInteger;

import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.base.SlaveAndRange;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.IllegalDataTypeException;
import com.serotonin.modbus4j.exception.ModbusIdException;
import com.serotonin.modbus4j.exception.ModbusTransportException;

public class ModbusLocator {
    protected SlaveAndRange slaveAndRange;
    protected int offset;
    protected int dataType;
    protected byte bit = -1;
    
    public ModbusLocator(SlaveAndRange slaveAndRange, int offset, int dataType) {
        this(slaveAndRange, offset, dataType, (byte)-1);
    }
    
    public ModbusLocator(int slaveId, int range, int offset, int dataType) {
        this(new SlaveAndRange(slaveId, range), offset, dataType, (byte)-1);
    }
    
    public ModbusLocator(int slaveId, int range, int offset, byte bit) {
        this(new SlaveAndRange(slaveId, range), offset, DataType.BINARY, bit);
        if (range != RegisterRange.HOLDING_REGISTER && range != RegisterRange.INPUT_REGISTER)
            throw new ModbusIdException("Bit requests can only be made from holding registers and input registers");
    }
    
    public ModbusLocator(int slaveId, int range, int offset, int dataType, byte bit) {
        this(new SlaveAndRange(slaveId, range), offset, dataType, bit);
    }
    
    private ModbusLocator(SlaveAndRange slaveAndRange, int offset, int dataType, byte bit) {
        this.slaveAndRange = slaveAndRange;
        this.offset = offset;
        this.dataType = dataType;
        this.bit = bit;
        validate();
    }
    
    
    protected void validate() {
        try {
            ModbusUtils.validateOffset(offset);
            ModbusUtils.validateEndOffset(offset + DataType.getRegisterCount(dataType) - 1);
        }
        catch (ModbusTransportException e) {
            throw new ModbusIdException(e);
        }
        
        int range = slaveAndRange.getRange();
        if (dataType != DataType.BINARY && (range == RegisterRange.COIL_STATUS || range == RegisterRange.INPUT_STATUS))
            throw new IllegalDataTypeException("Only binary values can be read from Coil and Input ranges");
        
        if ((range == RegisterRange.HOLDING_REGISTER || range == RegisterRange.INPUT_REGISTER) &&
                dataType == DataType.BINARY)
            ModbusUtils.validateBit(bit);
    }
    
    
    public int getDataType() {
        return dataType;
    }
    public int getOffset() {
        return offset;
    }
    public SlaveAndRange getSlaveAndRange() {
        return slaveAndRange;
    }
    public int getEndOffset() {
        return offset + DataType.getRegisterCount(dataType) - 1;
    }
    public int getLength() {
        return DataType.getRegisterCount(dataType);
    }
    public byte getBit() {
        return bit;
    }
    
    /**
     * Converts data from the byte array into a java value according to this locator's data type.
     * @param data
     * @param requestOffset
     * @return the converted data
     */
    public Object bytesToValue(byte[] data, int requestOffset) {
        // Determined the offset normalized to the response data.
        int nOffset = offset - requestOffset;
        
        // If this is a coil or input, convert to boolean.
        if (slaveAndRange.getRange() == RegisterRange.COIL_STATUS || 
                slaveAndRange.getRange() == RegisterRange.INPUT_STATUS)
            return new Boolean((((data[nOffset/8] & 0xff) >> (nOffset%8)) & 0x1) == 1);
        
        // For the rest of the types, we double the normalized offset to account for short to byte.
        nOffset *= 2;

        // We could still be asking for a binary if it's a bit in a register.
        if (dataType == DataType.BINARY)
            return new Boolean((((data[nOffset + 1 - bit/8] & 0xff) >> (bit%8)) & 0x1) == 1);
        
        // Handle the numeric types.
        // 2 bytes
        if (dataType == DataType.TWO_BYTE_INT_UNSIGNED)
            return new Integer(((data[nOffset] & 0xff) << 8) | (data[nOffset+1] & 0xff));
        
        if (dataType == DataType.TWO_BYTE_INT_SIGNED)
            return new Short((short)(((data[nOffset] & 0xff) << 8) | (data[nOffset+1] & 0xff)));
        
        if (dataType == DataType.TWO_BYTE_BCD) {
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<2; i++) {
                sb.append(bcdNibbleToInt(data[nOffset+i], true));
                sb.append(bcdNibbleToInt(data[nOffset+i], false));
            }
            return Short.parseShort(sb.toString());
        }
        
        // 4 bytes
        if (dataType == DataType.FOUR_BYTE_INT_UNSIGNED)
            return new Long(
                    ((long)((data[nOffset] & 0xff)) << 24) | ((long)((data[nOffset+1] & 0xff)) << 16) | 
                    ((long)((data[nOffset+2] & 0xff)) << 8) | ((data[nOffset+3] & 0xff)));
        
        if (dataType == DataType.FOUR_BYTE_INT_SIGNED)
            return new Integer(
                    ((data[nOffset] & 0xff) << 24) | ((data[nOffset+1] & 0xff) << 16) | 
                    ((data[nOffset+2] & 0xff) << 8) | (data[nOffset+3] & 0xff));
        
        if (dataType == DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED)
            return new Long(
                    ((long)((data[nOffset+2] & 0xff)) << 24) | ((long)((data[nOffset+3] & 0xff)) << 16) | 
                    ((long)((data[nOffset] & 0xff)) << 8) | ((data[nOffset+1] & 0xff)));
        
        if (dataType == DataType.FOUR_BYTE_INT_SIGNED_SWAPPED)
            return new Integer(
                    ((data[nOffset+2] & 0xff) << 24) | ((data[nOffset+3] & 0xff) << 16) | 
                    ((data[nOffset] & 0xff) << 8) | (data[nOffset+1] & 0xff));
        
        if (dataType == DataType.FOUR_BYTE_FLOAT)
            return Float.intBitsToFloat(
                    ((data[nOffset] & 0xff) << 24) | ((data[nOffset+1] & 0xff) << 16) | 
                    ((data[nOffset+2] & 0xff) << 8) | (data[nOffset+3] & 0xff));
        
        if (dataType == DataType.FOUR_BYTE_FLOAT_SWAPPED)
            return Float.intBitsToFloat(
                    ((data[nOffset+2] & 0xff) << 24) | ((data[nOffset+3] & 0xff) << 16) | 
                    ((data[nOffset] & 0xff) << 8) | (data[nOffset+1] & 0xff));
        
        if (dataType == DataType.FOUR_BYTE_BCD) {
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<4; i++) {
                sb.append(bcdNibbleToInt(data[nOffset+i], true));
                sb.append(bcdNibbleToInt(data[nOffset+i], false));
            }
            return Integer.parseInt(sb.toString());
        }
        
        // 8 bytes
        if (dataType == DataType.EIGHT_BYTE_INT_UNSIGNED) {
            byte[] b9 = new byte[9];
            System.arraycopy(data, nOffset, b9, 1, 8);
            return new BigInteger(b9);
        }
            
        if (dataType == DataType.EIGHT_BYTE_INT_SIGNED)
            return new Long(
                    ((long)((data[nOffset] & 0xff)) << 56) | ((long)((data[nOffset+1] & 0xff)) << 48) | 
                    ((long)((data[nOffset+2] & 0xff)) << 40) | ((long)((data[nOffset+3] & 0xff)) << 32) |
                    ((long)((data[nOffset+4] & 0xff)) << 24) | ((long)((data[nOffset+5] & 0xff)) << 16) | 
                    ((long)((data[nOffset+6] & 0xff)) << 8) | ((data[nOffset+7] & 0xff)));
        
        if (dataType == DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED) {
            byte[] b9 = new byte[9];
            b9[1] = data[nOffset+6];
            b9[2] = data[nOffset+7];
            b9[3] = data[nOffset+4];
            b9[4] = data[nOffset+5];
            b9[5] = data[nOffset+2];
            b9[6] = data[nOffset+3];
            b9[7] = data[nOffset];
            b9[8] = data[nOffset+1];
            return new BigInteger(b9);
        }
            
        if (dataType == DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED)
            return new Long(
                    ((long)((data[nOffset+6] & 0xff)) << 56) | ((long)((data[nOffset+7] & 0xff)) << 48) | 
                    ((long)((data[nOffset+4] & 0xff)) << 40) | ((long)((data[nOffset+5] & 0xff)) << 32) |
                    ((long)((data[nOffset+2] & 0xff)) << 24) | ((long)((data[nOffset+3] & 0xff)) << 16) | 
                    ((long)((data[nOffset] & 0xff)) << 8) | ((data[nOffset+1] & 0xff)));
            
        if (dataType == DataType.EIGHT_BYTE_FLOAT)
            return Double.longBitsToDouble(
                    ((long)((data[nOffset] & 0xff)) << 56) | ((long)((data[nOffset+1] & 0xff)) << 48) | 
                    ((long)((data[nOffset+2] & 0xff)) << 40) | ((long)((data[nOffset+3] & 0xff)) << 32) | 
                    ((long)((data[nOffset+4] & 0xff)) << 24) | ((long)((data[nOffset+5] & 0xff)) << 16) | 
                    ((long)((data[nOffset+6] & 0xff)) << 8) | ((data[nOffset+7] & 0xff)));
        
        if (dataType == DataType.EIGHT_BYTE_FLOAT_SWAPPED)
            return Double.longBitsToDouble(
                    ((long)((data[nOffset+6] & 0xff)) << 56) | ((long)((data[nOffset+7] & 0xff)) << 48) | 
                    ((long)((data[nOffset+4] & 0xff)) << 40) | ((long)((data[nOffset+5] & 0xff)) << 32) | 
                    ((long)((data[nOffset+2] & 0xff)) << 24) | ((long)((data[nOffset+3] & 0xff)) << 16) | 
                    ((long)((data[nOffset] & 0xff)) << 8) | ((data[nOffset+1] & 0xff)));
        
        throw new RuntimeException("Unsupported data type: "+ dataType);
    }
    
    private int bcdNibbleToInt(byte b, boolean high) {
        int n;
        if (high)
            n = (b >> 4) & 0xf;
        else
            n = b & 0xf;
        if (n > 9)
            n = 0;
        return n;
    }
    
    public short[] valueToShorts(Number value) {
        return valueToShorts(value, dataType);
    }
    
    /**
     * Converts data from a java value into the byte array according to this locator's data type. This method does
     * not handle the binary type.
     * 
     * @param value
     * @param dataType
     * @return the converted data
     */
    public static short[] valueToShorts(Number value, int dataType) {
        // 2 bytes
        if (dataType == DataType.TWO_BYTE_INT_UNSIGNED || dataType == DataType.TWO_BYTE_INT_SIGNED)
            return new short[] {value.shortValue()};
        
        if (dataType == DataType.TWO_BYTE_BCD) {
            short s = value.shortValue();
            return new short[] { (short)(
                    (((s / 1000) % 10) << 12) |
                    (((s / 100) % 10) << 8) |
                    (((s / 10) % 10) << 4) |
                    (s % 10))
            };
        }
        
        // 4 bytes
        if (dataType == DataType.FOUR_BYTE_INT_UNSIGNED || dataType == DataType.FOUR_BYTE_INT_SIGNED) {
            int i = value.intValue();
            return new short[] {(short)(i >> 16), (short)i};
        }
        
        if (dataType == DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED || dataType == DataType.FOUR_BYTE_INT_SIGNED_SWAPPED) {
            int i = value.intValue();
            return new short[] {(short)i, (short)(i >> 16)};
        }
        
        if (dataType == DataType.FOUR_BYTE_FLOAT) {
            int i = Float.floatToIntBits(value.floatValue());
            return new short[] {(short)(i >> 16), (short)i};
        }
        
        if (dataType == DataType.FOUR_BYTE_FLOAT_SWAPPED) {
            int i = Float.floatToIntBits(value.floatValue());
            return new short[] {(short)i, (short)(i >> 16)};
        }
        
        if (dataType == DataType.FOUR_BYTE_BCD) {
            int i = value.intValue();
            return new short[] {
                    (short)((((i / 10000000) % 10) << 12) |
                    (((i / 1000000) % 10) << 8) |
                    (((i / 100000) % 10) << 4) |
                    ((i / 10000) % 10)),
                    (short)((((i / 1000) % 10) << 12) |
                    (((i / 100) % 10) << 8) |
                    (((i / 10) % 10) << 4) |
                    (i % 10))
            };
        }
        
        // 8 bytes
        if (dataType == DataType.EIGHT_BYTE_INT_UNSIGNED || dataType == DataType.EIGHT_BYTE_INT_SIGNED) {
            long l = Float.floatToIntBits(value.floatValue());
            return new short[] {(short)(l >> 48), (short)(l >> 32), (short)(l >> 16), (short)l};
        }
            
        if (dataType == DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED || 
                dataType == DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED) {
            long l = Float.floatToIntBits(value.floatValue());
            return new short[] {(short)l, (short)(l >> 16), (short)(l >> 32), (short)(l >> 48)};
        }
        
        if (dataType == DataType.EIGHT_BYTE_FLOAT) {
            long l = Double.doubleToLongBits(value.doubleValue());
            return new short[] {(short)(l >> 48), (short)(l >> 32), (short)(l >> 16), (short)l};
        }
        
        if (dataType == DataType.EIGHT_BYTE_FLOAT_SWAPPED) {
            long l = Double.doubleToLongBits(value.doubleValue());
            return new short[] {(short)l, (short)(l >> 16), (short)(l >> 32), (short)(l >> 48)};
        }
        
        throw new RuntimeException("Unsupported data type: "+ dataType);
    }
    
    public static void main(String[] args) {
        ModbusLocator ml = new ModbusLocator(0, 0, 0, DataType.FOUR_BYTE_BCD);
        int i = 12345678;
        short[] sa = ml.valueToShorts(new Integer(i));
        for (int x=0; x<sa.length; x++)
            System.out.println(sa[x]);
        
    }
}
