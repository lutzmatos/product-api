package br.com.sys.productapi.modules.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalvesProductResultResponse {
    private List<String> salesIds;

}
