package com.besafx.app.entity.wrapper;

import com.besafx.app.entity.DrugUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RemainQuantity {
    private DrugUnit drugUnit;
    private Double quantity;
}
