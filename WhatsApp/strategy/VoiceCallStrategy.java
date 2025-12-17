package DAY6.WhatsApp.strategy;

import DAY6.WhatsApp.models.Call;

public class VoiceCallStrategy implements CallStrategy {
    
    @Override
    public void initiateCall(Call call) {
        System.out.println("Initiating voice call between " + 
            call.getCaller().getUserName() + " and " + 
            call.getReceiver().getUserName());
    }
    
    @Override
    public void establishConnection(Call call) {
        System.out.println("Establishing audio connection...");
        System.out.println("Audio codec: Opus");
        System.out.println("Audio bitrate: 64 kbps");
        System.out.println("Voice call connection established successfully");
    }
    
    @Override
    public void handleCallQuality(Call call) {
        System.out.println("Monitoring audio quality...");
        System.out.println("Audio quality: Good");
        System.out.println("Latency: 50ms");
        System.out.println("Packet loss: 0.5%");
    }
    
    @Override
    public String getCallTypeDescription() {
        return "Voice Call - Audio only communication";
    }
}
