package DAY6.WhatsApp.strategy;

import DAY6.WhatsApp.models.Call;

public class VideoCallStrategy implements CallStrategy {
    
    @Override
    public void initiateCall(Call call) {
        System.out.println("Initiating video call between " + 
            call.getCaller().getUserName() + " and " + 
            call.getReceiver().getUserName());
    }
    
    @Override
    public void establishConnection(Call call) {
        System.out.println("Establishing video connection...");
        System.out.println("Video codec: H.264");
        System.out.println("Video resolution: 720p");
        System.out.println("Video bitrate: 1.5 Mbps");
        System.out.println("Audio codec: Opus");
        System.out.println("Audio bitrate: 64 kbps");
        System.out.println("Video call connection established successfully");
    }
    
    @Override
    public void handleCallQuality(Call call) {
        System.out.println("Monitoring video and audio quality...");
        System.out.println("Video quality: HD");
        System.out.println("Frame rate: 30 fps");
        System.out.println("Audio quality: Good");
        System.out.println("Latency: 75ms");
        System.out.println("Packet loss: 1.2%");
    }
    
    @Override
    public String getCallTypeDescription() {
        return "Video Call - Audio and video communication with HD quality";
    }
}
