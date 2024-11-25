package com.bteam.fantasychess_client.networking;

import com.badlogic.gdx.Net;
import com.bteam.fantasychess_client.Main;

import java.util.function.Consumer;
import java.util.logging.Level;

public class HttpResponseCallbackListener implements Net.HttpResponseListener {
    Consumer<Net.HttpResponse> responseConsumer;

    public HttpResponseCallbackListener(Consumer<Net.HttpResponse> responseConsumer) {
        this.responseConsumer = responseConsumer;
    }

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {
        responseConsumer.accept(httpResponse);
    }

    @Override
    public void failed(Throwable t) {
        Main.getLogger().log(Level.SEVERE, "Registration cancelled");
    }

    @Override
    public void cancelled() {
        Main.getLogger().log(Level.SEVERE, "Registration cancelled");
    }
}
