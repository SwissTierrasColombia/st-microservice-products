package com.ai.st.microservice.quality.modules.deliveries.application.CreateDelivery;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public final class CreateDeliveryRequest {

    @ApiModelProperty(required = true, notes = "Municipality ID")
    private Long municipalityId;

    @ApiModelProperty(required = true, notes = "Observations")
    private String observations;

    private List<CreateDeliveryProductDto> products;

}
