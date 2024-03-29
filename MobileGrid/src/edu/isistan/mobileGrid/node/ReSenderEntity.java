package edu.isistan.mobileGrid.node;

import edu.isistan.mobileGrid.network.Message;
import edu.isistan.mobileGrid.network.Node;
import edu.isistan.simulator.Entity;
import edu.isistan.simulator.Event;
import edu.isistan.simulator.Logger;
import edu.isistan.simulator.Simulation;

import java.util.HashMap;

public abstract class ReSenderEntity extends Entity implements Node {

    public static final int EVENT_MESSAGE_RETRY = 99;
    private static final long NO_RETRY = 0;
    private long resendInterval = NO_RETRY;
    private int amountOfReintents = (int) NO_RETRY;
    private HashMap<String, Integer> messagesReintentsCount = new HashMap<>();

    public ReSenderEntity(String name) {
        super(name);
    }

    public void setResendInterval(long resendInterval) {
        this.resendInterval = resendInterval;
    }

    public void setAmountOfReintents(int amountOfReintents) {
        this.amountOfReintents = amountOfReintents;
    }

    private String parseKey(Message message) {
        return ((String.valueOf(message.getId())) + (message.getOffset()));
    }

    @Override
    public void fail(Message message) {
        Logger.logString("Message sent failed", message.getSource().getId(), message.getId(), message.getOffset(), message.isLastMessage());
        if (resendInterval != NO_RETRY) {
            String key = parseKey(message);
            if (!messagesReintentsCount.containsKey(key)) {
                messagesReintentsCount.put(key, 0);
            }
            int currentAmountOfReintents = messagesReintentsCount.get(key);
            if (currentAmountOfReintents < amountOfReintents) {
                retry(message);
                messagesReintentsCount.replace(key, ++currentAmountOfReintents);
                //Logger.logString("Message scheduled to resend", message.getId(), message.getOffset(), message.isLastMessage(), currentAmountOfReintents);
            } else {
                messagesReintentsCount.remove(key);
                failToRetry(message);
                Logger.logString("Message sent failed to be resend", message.getSource().getId(), message.getId(), message.getOffset(), message.isLastMessage());
            }
        } else {
            failToRetry(message);
        }
    }

    protected abstract void failToRetry(Message message);

    public void retry(Message message) {
        long retryTime = Simulation.getTime() + resendInterval;
        Event retryEvent = Event.createEvent(Event.NO_SOURCE, retryTime, this.getId(), EVENT_MESSAGE_RETRY, message);
        Simulation.addEvent(retryEvent);
    }

}
