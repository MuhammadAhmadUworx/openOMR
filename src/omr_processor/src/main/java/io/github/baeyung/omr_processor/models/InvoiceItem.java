package io.github.baeyung.omr_processor.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceItem {

    private String productName;
    private Integer quantity;
    private String packing;

    private BigDecimal rate;
    private BigDecimal grossAmount;

    private BigDecimal discountPercent;
    private BigDecimal discountRs;
    private BigDecimal salesTax;

    private BigDecimal netAmount;
}
