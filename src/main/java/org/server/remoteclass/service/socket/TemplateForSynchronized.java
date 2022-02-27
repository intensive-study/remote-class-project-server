package org.server.remoteclass.service.socket;

public class TemplateForSynchronized {

    public synchronized void executeToSynchronize(NeedToSynchronized method) {
        method.execute();
    }
}
