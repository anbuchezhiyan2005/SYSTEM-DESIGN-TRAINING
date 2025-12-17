package DAY6.WhatsApp.models;

import DAY6.WhatsApp.enums.CallStatus;
import DAY6.WhatsApp.enums.CallType;
import java.time.LocalDateTime;

public class Call {
    private String callID;
    private User caller;
    private User receiver;
    private CallType callType;
    private CallStatus callStatus;
    private LocalDateTime callStartTime;
    private LocalDateTime callEndTime;
    private long callDuration;

    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }

    public User getCaller() {
        return caller;
    }

    public void setCaller(User caller) {
        this.caller = caller;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

    public CallStatus getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(CallStatus callStatus) {
        this.callStatus = callStatus;
    }

    public LocalDateTime getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(LocalDateTime callStartTime) {
        this.callStartTime = callStartTime;
    }

    public LocalDateTime getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(LocalDateTime callEndTime) {
        this.callEndTime = callEndTime;
    }

    public long getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(long callDuration) {
        this.callDuration = callDuration;
    }

    public void startCall() {
        this.callStartTime = LocalDateTime.now();
        this.callStatus = CallStatus.ONGOING;
    }

    public void endCall() {
        this.callEndTime = LocalDateTime.now();
        if (callStartTime != null) {
            this.callDuration = calculateDuration();
        }
        this.callStatus = CallStatus.COMPLETED;
    }

    public void markAsMissed() {
        this.callStatus = CallStatus.MISSED;
    }

    public void markAsRejected() {
        this.callStatus = CallStatus.REJECTED;
    }

    private long calculateDuration() {
        if (callStartTime != null && callEndTime != null) {
            return java.time.temporal.ChronoUnit.SECONDS.between(callStartTime, callEndTime);
        }
        return 0;
    }
}
