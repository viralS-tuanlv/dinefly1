package com.dineplan.dinefly.core.print;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class BTPOSPrinter
{

    private final String address;

    BluetoothAdapter mBluetoothAdapter;
    BTConnector.BluetoothSocketWrapper mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    final static byte LF = 10;

    public BTPOSPrinter(String address) throws IOException
    {
        this.address = address.toUpperCase();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        openBT();
    }

    private void openBT() throws IOException
    {
        boolean success = true;
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        Set<BluetoothDevice> allDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice d : allDevices)
        {
            String address  = d.getAddress().toUpperCase();
            String localAddress = address.toUpperCase();
            if (address.equals(localAddress))
            {
                mmDevice = mBluetoothAdapter.getRemoteDevice(address);
                break;
            }
        }
        if (mmDevice != null)
        {
            BTConnector connector = new BTConnector(mmDevice, true, mBluetoothAdapter, Arrays.asList(uuid));
            mmSocket = connector.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            beginListenForData();
        }
    }

    void beginListenForData() throws IOException
    {
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while (!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();

                        if (bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);

                            for (int i = 0; i < bytesAvailable; i++)
                            {
                                byte b = packetBytes[i];
                                if (b == LF)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    // todo: handle printer reply if necessary
                                    readBufferPosition = 0;
                                } else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }

                    } catch (IOException ex)
                    {
                        ex.printStackTrace();
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    public void sendData(final List<byte[]> escposdata, int va) throws IOException
    {
        for (byte[] line : escposdata)
        {
            mmOutputStream.write(line);
        }
    }

    public void sendData(final List<String> data) throws IOException
    {
        mmOutputStream.write(ESCP.init_printer());
        for (String line : data)
        {
            mmOutputStream.write(line.getBytes());

        }

        mmOutputStream.write(ESCP.feedpapercut());
    }

    public void close() throws IOException
    {
        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        stopWorker = true;
        mmOutputStream.flush();
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
    }

}
