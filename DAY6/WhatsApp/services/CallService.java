package DAY6.WhatsApp.services;

import DAY6.WhatsApp.models.Call;
import DAY6.WhatsApp.models.User;
import DAY6.WhatsApp.enums.CallStatus;
import DAY6.WhatsApp.enums.CallType;
import DAY6.WhatsApp.repositories.CallRepository;
import DAY6.WhatsApp.repositories.CallRepositoryImpl;
import DAY6.WhatsApp.strategy.CallStrategy;
import DAY6.WhatsApp.strategy.VoiceCallStrategy;
import DAY6.WhatsApp.strategy.VideoCallStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CallService {
    private static CallService instance;
    private final CallRepository callRepository;
    private final UserService userService;
    private final Map<CallType, CallStrategy> callStrategies;
    
    private CallService(CallRepository callRepository, UserService userService) {
        this.callRepository = callRepository;
        this.userService = userService;
        this.callStrategies = new HashMap<>();
        this.callStrategies.put(CallType.VOICE, new VoiceCallStrategy());
        this.callStrategies.put(CallType.VIDEO, new VideoCallStrategy());
    }
    
    public static CallService getInstance() {
        if (instance == null) {
            instance = new CallService(CallRepositoryImpl.getInstance(), UserService.getInstance());
        }
        return instance;
    }
    
    public Call initiateCall(String callerId, String receiverId, CallType callType) {
        if (callerId == null || receiverId == null || callType == null) {
            return null;
        }
        if (callerId.equals(receiverId)) {
            return null;
        }
        
        User caller = userService.getUserById(callerId);
        User receiver = userService.getUserById(receiverId);
        
        Call call = new Call();
        call.setCallID(UUID.randomUUID().toString());
        call.setCaller(caller);
        call.setReceiver(receiver);
        call.setCallType(callType);
        call.setCallStatus(CallStatus.ONGOING);
        
        CallStrategy strategy = callStrategies.get(callType);
        if (strategy != null) {
            strategy.initiateCall(call);
        }
        
        return callRepository.save(call);
    }
    
    public Call getCallById(String callId) {
        return callRepository.findById(callId);
    }
    
    public List<Call> getCallHistoryForUser(String userId) {
        return callRepository.findByUserId(userId);
    }
    
    public List<Call> getCallHistoryBetweenUsers(String userId1, String userId2) {
        return callRepository.findByCallerAndReceiver(userId1, userId2);
    }
    
    public boolean answerCall(String callId) {
        Call call = callRepository.findById(callId);
        if (call == null) return false;
        if (call.getCallStatus() != CallStatus.ONGOING) return false;
        
        call.startCall();
        
        CallStrategy strategy = callStrategies.get(call.getCallType());
        if (strategy != null) {
            strategy.establishConnection(call);
        }
        
        callRepository.update(call);
        return true;
    }
    
    public boolean endCall(String callId) {
        Call call = callRepository.findById(callId);
        if (call == null) return false;
        
        call.endCall();
        callRepository.update(call);
        return true;
    }
    
    public boolean rejectCall(String callId) {
        Call call = callRepository.findById(callId);
        if (call == null) return false;
        if (call.getCallStatus() != CallStatus.ONGOING) return false;
        
        call.markAsRejected();
        callRepository.update(call);
        return true;
    }
    
    public boolean markCallAsMissed(String callId) {
        Call call = callRepository.findById(callId);
        if (call == null) return false;
        
        call.markAsMissed();
        callRepository.update(call);
        return true;
    }
    
    public CallStatus getCallStatus(String callId) {
        Call call = callRepository.findById(callId);
        return call != null ? call.getCallStatus() : null;
    }
    
    public long getCallDuration(String callId) {
        Call call = callRepository.findById(callId);
        return call != null ? call.getCallDuration() : 0;
    }
    
    public boolean deleteCall(String callId) {
        Call call = callRepository.findById(callId);
        if (call == null) return false;
        
        callRepository.delete(callId);
        return true;
    }
    
    public void monitorCallQuality(String callId) {
        Call call = callRepository.findById(callId);
        if (call == null) return;
        
        CallStrategy strategy = callStrategies.get(call.getCallType());
        if (strategy != null) {
            strategy.handleCallQuality(call);
        }
    }
    
    public String getCallDescription(CallType callType) {
        CallStrategy strategy = callStrategies.get(callType);
        return strategy != null ? strategy.getCallTypeDescription() : "Unknown call type";
    }
}
