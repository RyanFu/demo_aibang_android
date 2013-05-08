package com.sssta.demo_aibang_android;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;


public class Client {
    private String result = null;
    public String ReceiveFromServer() throws IOException
    {
        
        BufferedReader is = null;
        
        try {
            //向本机的4700端口发出客户请求
            Socket socket=new Socket("192.168.1.135",4700);
            //由Socket对象得到输入流，并构造相应的BufferedReader对象
            is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //由Socket输入流得到相应的字符串
            result = is.readLine();
            System.out.println("Server:"+result);
            Log.e("normal1", "success");
            Log.i("info", result);
            is.close(); //关闭Socket输入流
            socket.close(); //关闭Socket
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error:"+e);
        }
        return result;
    }
    

	
}


