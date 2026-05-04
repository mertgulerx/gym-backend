package com.ytu.gymbackend.dto.response;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class SubscriptionResponse {
    private Long id;

    private Long customerId;

    private String lastSubscriptionStartDate;

    // If canceled or suspended. Doesn't have to exist.
    @Nullable
    private String endDate;

    private String status;
}
