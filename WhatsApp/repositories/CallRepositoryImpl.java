package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.Call;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CallRepositoryImpl implements CallRepository {
    private Map<String, Call> callStore;
    private Map<String, List<String>> userCallsIndex;
    private static CallRepositoryImpl instance;
    
    private CallRepositoryImpl() {
        this.callStore = new HashMap<>();
        this.userCallsIndex = new HashMap<>();
    }
    
    public static CallRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new CallRepositoryImpl();
        }
        return instance;
    }
    
    @Override
    public Call save(Call call) {
        if (call == null || call.getCallID() == null) {
            return null;
        }
        if (call.getCaller() == null || call.getReceiver() == null) {
            return null;
        }
        
        callStore.put(call.getCallID(), call);
        
        String callerId = call.getCaller().getUserID();
        String receiverId = call.getReceiver().getUserID();
        
        userCallsIndex.putIfAbsent(callerId, new ArrayList<>());
        userCallsIndex.get(callerId).add(call.getCallID());
        
        userCallsIndex.putIfAbsent(receiverId, new ArrayList<>());
        userCallsIndex.get(receiverId).add(call.getCallID());
        
        return call;
    }
    
    @Override
    public Call findById(String callId) {
        return callStore.get(callId);
    }
    
    @Override
    public List<Call> findByUserId(String userId) {
        List<Call> userCalls = new ArrayList<>();
        List<String> callIds = userCallsIndex.getOrDefault(userId, new ArrayList<>());
        for (String callId : callIds) {
            Call call = callStore.get(callId);
            if (call != null) {
                userCalls.add(call);
            }
        }
        return userCalls;
    }
    
    @Override
    public List<Call> findByCallerAndReceiver(String callerId, String receiverId) {
        List<Call> allCalls = findByUserId(callerId);
        return allCalls.stream()
            .filter(call -> (call.getCaller().getUserID().equals(callerId) && 
                           call.getReceiver().getUserID().equals(receiverId)) ||
                          (call.getCaller().getUserID().equals(receiverId) && 
                           call.getReceiver().getUserID().equals(callerId)))
            .collect(Collectors.toList());
    }
    
    @Override
    public Call update(Call call) {
        if (call == null || call.getCallID() == null) {
            return null;
        }
        if (!callStore.containsKey(call.getCallID())) {
            return null;
        }
        callStore.put(call.getCallID(), call);
        return call;
    }
    
    @Override
    public void delete(String callId) {
        Call call = callStore.get(callId);
        if (call != null) {
            callStore.remove(callId);
            
            String callerId = call.getCaller().getUserID();
            String receiverId = call.getReceiver().getUserID();
            
            List<String> callerCalls = userCallsIndex.get(callerId);
            if (callerCalls != null) {
                callerCalls.remove(callId);
            }
            
            List<String> receiverCalls = userCallsIndex.get(receiverId);
            if (receiverCalls != null) {
                receiverCalls.remove(callId);
            }
        }
    }
}
