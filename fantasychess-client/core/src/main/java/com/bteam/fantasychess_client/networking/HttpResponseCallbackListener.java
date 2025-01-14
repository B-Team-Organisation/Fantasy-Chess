package com.bteam.fantasychess_client.networking;

import com.badlogic.gdx.Net;
import com.bteam.fantasychess_client.Main;

import java.util.function.Consumer;
import java.util.logging.Level;

public class HttpResponseCallbackListener implements Net.HttpResponseListener {
    Consumer<Net.HttpResponse> responseConsumer;
    Consumer<Throwable> onFailedConsumer;
    Runnable onCancelledConsumer;

    public HttpResponseCallbackListener(Consumer<Net.HttpResponse> responseConsumer,
                                        Consumer<Throwable> onFailedConsumer,
                                        Runnable onCancelledConsumer) {
        this.responseConsumer = responseConsumer;
        this.onFailedConsumer = onFailedConsumer;
        this.onCancelledConsumer = onCancelledConsumer;
    }

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {
        responseConsumer.accept(httpResponse);
    }

    @Override
    public void failed(Throwable t) {
        Main.getLogger().log(Level.SEVERE, "Registration failed");
        if (onFailedConsumer != null) onFailedConsumer.accept(t);
    }

    @Override
    public void cancelled() {
        Main.getLogger().log(Level.SEVERE, "Registration cancelled");
        if (onCancelledConsumer != null) onCancelledConsumer.run();
    }
}
