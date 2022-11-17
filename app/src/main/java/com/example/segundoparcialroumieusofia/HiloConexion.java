package com.example.segundoparcialroumieusofia;

import android.icu.util.Measure;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HiloConexion implements Runnable {
    Handler handler;
    public HiloConexion (Handler handler){
        this.handler=handler;
    }

    @Override
    public void run() {
        HTTPConexion http = new HTTPConexion();

        String respuesta = http.consultarUsuarios("http://192.168.100.24:3001/usuarios");

        Log.d("Sofia - volvio", respuesta);
        Message message =new Message();
        message.obj=respuesta;
        handler.sendMessage(message);
      //  Log.d("Sofia - resultado", respuesta);
    }
}
