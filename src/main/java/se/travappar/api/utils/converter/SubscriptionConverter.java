package se.travappar.api.utils.converter;

import se.travappar.api.model.Subscription;
import se.travappar.api.model.dto.SubscriptionDTO;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionConverter implements Converter<List<Subscription>, SubscriptionDTO> {
    @Override
    public SubscriptionDTO convertToDTO(List<Subscription> subscriptionList) {
        List<Long> trackIdList = new ArrayList<>();
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        for (Subscription subscription : subscriptionList) {
            subscriptionDTO.setDeviceId(subscription.getDeviceId());
            trackIdList.add(subscription.getTrackId());
        }
        subscriptionDTO.setTrackIdList(trackIdList);
        return subscriptionDTO;
    }

    @Override
    public List<Subscription> convertToEntity(SubscriptionDTO subscriptionDTO) {
        List<Subscription> subscriptionList = new ArrayList<>();
        String deviceId = subscriptionDTO.getDeviceId();
        for (Long trackId : subscriptionDTO.getTrackIdList()) {
            Subscription subscription = new Subscription();
            subscription.setTrackId(trackId);
            subscription.setDeviceId(deviceId);
            subscriptionList.add(subscription);
        }
        return subscriptionList;
    }
}
