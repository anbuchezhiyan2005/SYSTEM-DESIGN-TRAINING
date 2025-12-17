package DAY6.WhatsApp.strategy;

import DAY6.WhatsApp.models.Call;

public interface CallStrategy {
    void initiateCall(Call call);
    void establishConnection(Call call);
    void handleCallQuality(Call call);
    String getCallTypeDescription();
}
