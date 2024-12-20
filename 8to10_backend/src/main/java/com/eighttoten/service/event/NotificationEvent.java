package com.eighttoten.service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.eighttoten.domain.notification.NotificationType;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class NotificationEvent {
    String clientEmail;
    Long targetEntityId;
    Long relatedEntityId;
    String message;
    NotificationType notificationType;
}
