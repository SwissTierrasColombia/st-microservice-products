package com.ai.st.microservice.quality.modules.deliveries.application.CreateDelivery;

import com.ai.st.microservice.quality.infrastructure.persistence.entities.DeliveryMethodEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@ApiModel(value = "CreateDeliveryProductDto")
public class CreateDeliveryProductDto implements Serializable {

    @ApiModelProperty(required = true, notes = "File")
    private MultipartFile file;

    @ApiModelProperty(required = true, notes = "Data")
    private String data;

    @ApiModelProperty(required = true, notes = "Observations")
    private String observations;

    @ApiModelProperty(required = true, notes = "Medio")
    private DeliveryMethodEnum medio;

    @ApiModelProperty(required = true, notes = "Product code")
    private Long productId;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public DeliveryMethodEnum getMedio() {
        return medio;
    }

    public void setMedio(DeliveryMethodEnum medio) {
        this.medio = medio;
    }
}
