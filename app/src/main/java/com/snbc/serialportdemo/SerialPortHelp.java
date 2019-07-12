package com.snbc.serialportdemo;

import android_serialport_api.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * author: zhougaoxiong
 * date: 2019/7/11,16:56
 * projectName:SerialPortDemo
 * packageName:com.snbc.serialportdemo
 */
public class SerialPortHelp {

    private static SerialPort mSerialPort;
    public static InputStream mInputStream;
    public static OutputStream mOutputStream;

    public static void openSericalPort(String path, int baudrate, int flag) {

        if (mSerialPort == null) {

            try {
                mSerialPort = new SerialPort(new File(path), baudrate, flag);
                mInputStream = mSerialPort.getInputStream();
                mOutputStream = mSerialPort.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
