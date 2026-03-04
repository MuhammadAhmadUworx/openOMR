package io.github.baeyung.omr_processor.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Invoice {

    private String companyName;
    private String address;
    private List<String> phones;

    private String invoiceNumber;
    private String invoiceDate;

    private String customerCode;
    private String customerName;
    private String customerCity;

    private List<InvoiceItem> items;

    private Integer totalItems;

    private BigDecimal totalGross;
    private BigDecimal totalDiscount;
    private BigDecimal totalTax;
    private BigDecimal totalNet;
}
