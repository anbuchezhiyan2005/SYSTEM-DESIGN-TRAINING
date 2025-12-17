package DAY6.WhatsApp.repositories;

import DAY6.WhatsApp.models.Call;
import java.util.List;

public interface CallRepository {
    Call save(Call call);
    Call findById(String callId);
    List<Call> findByUserId(String userId);
    List<Call> findByCallerAndReceiver(String callerId, String receiverId);
    Call update(Call call);
    void delete(String callId);
}
