package org.ikernits.vaadin.shared;

import com.vaadin.ui.UI;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class VaadinSharedModel<VPC extends VaadinPropertyContainer> {
    private static final Logger log = Logger.getLogger(VaadinSharedModel.class);

    private static class UpdateReceiver<VPC extends VaadinPropertyContainer> {
        final UI ui;
        final Runnable updateHandler;
        final VPC propertyContainer;

        public UpdateReceiver(UI ui, Runnable updateHandler, VPC propertyContainer) {
            this.ui = ui;
            this.updateHandler = updateHandler;
            this.propertyContainer = propertyContainer;
        }

        public UI getUi() {
            return ui;
        }

        public void update() {
            ui.access(() -> {
                ui.getSession().lock();
                propertyContainer.update();
                updateHandler.run();
                ui.push();
                ui.getSession().unlock();
            });
        }
    }

    final Map<UI, UpdateReceiver> receivers = new ConcurrentHashMap<>();
    final Supplier<VPC> propertyContainerSupplier;

    public VaadinSharedModel(Supplier<VPC> propertyContainerSupplier) {
        this.propertyContainerSupplier = propertyContainerSupplier;
    }

    public VPC registerUpdateHandler(UI ui, Runnable updateHandler) {
        VPC propertyModel = propertyContainerSupplier.get();
        receivers.put(ui, new UpdateReceiver<>(ui, updateHandler, propertyModel));
        propertyModel.initialize();
        return propertyModel;
    }

    public boolean unregisterUpdateHandler(UI ui) {
        return receivers.remove(ui) != null;
    }

    public void update() {
        for (UpdateReceiver receiver : receivers.values()) {
            try {
                receiver.update();
            } catch (Exception ex) {
                log.error("Update receiver error, receiver unregistered", ex);
                receivers.remove(receiver.getUi());
            }
        }
    }
}
