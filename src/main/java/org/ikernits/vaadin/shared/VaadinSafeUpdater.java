package org.ikernits.vaadin.shared;

import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class VaadinSafeUpdater implements Runnable {
    private static final Logger log = Logger.getLogger(VaadinSafeUpdater.class);
    private final ExecutorService executorService;

    public VaadinSafeUpdater(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private static class UpdateReceiver {
        final UI ui;
        final Runnable runnable;

        public UpdateReceiver(UI ui, Runnable runnable) {
            this.ui = ui;
            this.runnable = runnable;
        }

        public void update() {
            if (ui.getPushConnection().isConnected()) {
                ui.accessSynchronously(runnable::run);
            }
        }
    }

    final Map<Runnable, UpdateReceiver> receivers = new ConcurrentHashMap<>();

    public void registerHandler(UI ui, Runnable runnable) {
        receivers.put(runnable, new UpdateReceiver(ui, runnable));
    }

    public boolean unregisterHandler(Runnable runnable) {
        return receivers.remove(runnable) != null;
    }

    public void updateSynchronously() {
        for (UpdateReceiver receiver : receivers.values()) {
            try {
                receiver.update();
            } catch (UIDetachedException ex) {
                log.warn("UI is detached, receiver unregistered", ex);
                receivers.remove(receiver.runnable);
            } catch (Exception ex) {
                if (ex.getMessage().contains("Push failed")) {
                    log.error("Push failed exception", ex);
                } else {
                    log.error("Update receiver error, receiver unregistered", ex);
                    receivers.remove(receiver.runnable);
                }
            }
        }
    }

    public void run() {
        executorService.submit(this::updateSynchronously);
    }
}
