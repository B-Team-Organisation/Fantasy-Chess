package com.bteam.fantasychess_client.networking;

import com.badlogic.gdx.Net;
import com.bteam.fantasychess_client.Main;

import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * A callback listener for handling HTTP responses, failures, and cancellations.
 *
 * @author Marc
 */
public class HttpResponseCallbackListener implements Net.HttpResponseListener {
    Consumer<Net.HttpResponse> responseConsumer;
    Consumer<Throwable> onFailedConsumer;
    Runnable onCancelledConsumer;

    /**
     * Constructs an {@code HttpResponseCallbackListener} with the provided consumers for handling responses.
     *
     * @param responseConsumer    The consumer to handle successful HTTP responses.
     * @param onFailedConsumer    The consumer to handle failures.
     * @param onCancelledConsumer The runnable to execute when the request is cancelled.
     */
    public HttpResponseCallbackListener(Consumer<Net.HttpResponse> responseConsumer,
                                        Consumer<Throwable> onFailedConsumer,
                                        Runnable onCancelledConsumer) {
        this.responseConsumer = responseConsumer;
        this.onFailedConsumer = onFailedConsumer;
        this.onCancelledConsumer = onCancelledConsumer;
    }

    /**
     * Handles a successful HTTP response by passing it to the response consumer.
     *
     * @param httpResponse The HTTP response received.
     */

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {
        responseConsumer.accept(httpResponse);
    }

    /**
     * Handles a failed HTTP request, logs the failure, and invokes the failure consumer if available.
     *
     * @param t The throwable representing the failure cause.
     */
    @Override
    public void failed(Throwable t) {
        Main.getLogger().log(Level.SEVERE, "Registration failed");
        if (onFailedConsumer != null) onFailedConsumer.accept(t);
    }

    /**
     * Handles a cancelled HTTP request, logs the cancellation, and invokes the cancellation consumer if available.
     */
    @Override
    public void cancelled() {
        Main.getLogger().log(Level.SEVERE, "Registration cancelled");
        if (onCancelledConsumer != null) onCancelledConsumer.run();
    }
}
