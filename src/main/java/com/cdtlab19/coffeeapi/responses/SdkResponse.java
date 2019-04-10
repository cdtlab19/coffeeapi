package com.cdtlab19.coffeeapi.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SdkResponse extends Response {
    private String status;
    private String messsage;
    private String eventPayload;
    private String ProposalResponse;
}
