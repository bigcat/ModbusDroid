package com.serotonin.modbus4j.test;

import com.serotonin.modbus4j.BasicProcessImage;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusSlave;
import com.serotonin.modbus4j.ProcessImageListener;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;

public class ListenerTest {
    public static void main(String[] args) throws Exception {
        BasicProcessImage processImage = getModscanProcessImage();
        processImage.addListener(new BasicProcessImageListener());

//        SerialParameters params = new SerialParameters();
//        params.setCommPortId("COM1");
//        params.setPortOwnerName("dufus");
//        params.setBaudRate(9600);
        
//        IpParameters params = new IpParameters();
//        params.setHost(host)
        
        ModbusFactory modbusFactory = new ModbusFactory();
//        ModbusListener listener = modbusFactory.createRtuListener(processImage, 31, params, false);
//        ModbusListener listener = modbusFactory.createAsciiListener(processImage, 31, params);
        ModbusSlave listener = modbusFactory.createTcpSlave(processImage, 31);
//        ModbusSlave listener = modbusFactory.createUdpSlave(processImage, 31);
        listener.start();
    }
    
    
    static class BasicProcessImageListener implements ProcessImageListener {
        public void coilWrite(int offset, boolean oldValue, boolean newValue) {
            System.out.println("Coil at "+ offset +" was set from "+ oldValue +" to "+ newValue);
        }

        public void holdingRegisterWrite(int offset, short oldValue, short newValue) {
            System.out.println("HR at "+ offset +" was set from "+ oldValue +" to "+ newValue);
        }
    }
    
    static BasicProcessImage getModscanProcessImage() {
        BasicProcessImage processImage = new BasicProcessImage();
        
        processImage.setCoil(10, true);
        processImage.setCoil(11, false);
        processImage.setCoil(12, true);
        processImage.setCoil(13, true);
        processImage.setCoil(14, false);
        
        processImage.setInput(10, false);
        processImage.setInput(11, false);
        processImage.setInput(12, true);
        processImage.setInput(13, false);
        processImage.setInput(14, true);
        
        processImage.setBinary(16, true);
        processImage.setBinary(10016, true);
        
        processImage.setHoldingRegister(10, (short)1);
        processImage.setHoldingRegister(11, (short)10);
        processImage.setHoldingRegister(12, (short)100);
        processImage.setHoldingRegister(13, (short)1000);
        processImage.setHoldingRegister(14, (short)10000);
        
        processImage.setInputRegister(10, (short)10000);
        processImage.setInputRegister(11, (short)1000);
        processImage.setInputRegister(12, (short)100);
        processImage.setInputRegister(13, (short)10);
        processImage.setInputRegister(14, (short)1);
        
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 0, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 3, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 7, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 8, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 14, true);
        
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 0, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 7, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 8, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 15, true);
        
        processImage.setRegister(RegisterRange.HOLDING_REGISTER, 16, DataType.TWO_BYTE_INT_SIGNED, new Integer(-1968));
        processImage.setRegister(RegisterRange.HOLDING_REGISTER, 17, DataType.FOUR_BYTE_INT_SIGNED, new Long(-123456789));
        processImage.setRegister(RegisterRange.HOLDING_REGISTER, 19, DataType.FOUR_BYTE_INT_SIGNED_SWAPPED, new Long(-123456789));
        processImage.setRegister(RegisterRange.HOLDING_REGISTER, 21, DataType.FOUR_BYTE_FLOAT, new Float(1968.1968));
        processImage.setRegister(RegisterRange.HOLDING_REGISTER, 23, DataType.EIGHT_BYTE_INT_SIGNED, new Long(-123456789));
        processImage.setRegister(RegisterRange.HOLDING_REGISTER, 27, DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED, new Long(-123456789));
        processImage.setRegister(RegisterRange.HOLDING_REGISTER, 31, DataType.EIGHT_BYTE_FLOAT, new Double(1968.1968));
        
        processImage.setRegister(RegisterRange.HOLDING_REGISTER, 80, DataType.TWO_BYTE_BCD, new Short((short)1234));
        processImage.setRegister(RegisterRange.HOLDING_REGISTER, 81, DataType.FOUR_BYTE_BCD, new Integer(12345678));
        
        processImage.setRegister(RegisterRange.HOLDING_REGISTER, 12288, DataType.FOUR_BYTE_FLOAT_SWAPPED, new Float(1968.1968));
        processImage.setRegister(RegisterRange.HOLDING_REGISTER, 12290, DataType.FOUR_BYTE_FLOAT_SWAPPED, new Float(-1968.1968));
        
        processImage.setRegister(RegisterRange.INPUT_REGISTER, 16, DataType.TWO_BYTE_INT_UNSIGNED, new Integer(0xfff0));
        processImage.setRegister(RegisterRange.INPUT_REGISTER, 17, DataType.FOUR_BYTE_INT_UNSIGNED, new Long(-123456789));
        processImage.setRegister(RegisterRange.INPUT_REGISTER, 19, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED, new Long(-123456789));
        processImage.setRegister(RegisterRange.INPUT_REGISTER, 21, DataType.FOUR_BYTE_FLOAT_SWAPPED, new Float(1968.1968));
        processImage.setRegister(RegisterRange.INPUT_REGISTER, 23, DataType.EIGHT_BYTE_INT_UNSIGNED, new Long(-123456789));
        processImage.setRegister(RegisterRange.INPUT_REGISTER, 27, DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED, new Long(-123456789));
        processImage.setRegister(RegisterRange.INPUT_REGISTER, 31, DataType.EIGHT_BYTE_FLOAT_SWAPPED, new Double(1968.1968));
        
        processImage.setExceptionStatus((byte)51);
        
        return processImage;
    }
}
