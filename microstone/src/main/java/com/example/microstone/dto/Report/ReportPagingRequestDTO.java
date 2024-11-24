package com.example.microstone.dto.Report;

import lombok.Data;
import lombok.Getter;

@Getter
public class ReportPagingRequestDTO {
    private int page;
    private String type;
}
