package com.jishop.option.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record General(
        List<OptionInfo> options
) {
    public static General getOptions(List<Map<String, Object>> productOptions) {
        List<OptionInfo> optionsList = new ArrayList<>();

        for (Map<String, Object> option : productOptions) {
            OptionInfo optionInfo = new OptionInfo(
                    (Long) option.get("saleProductId"),
                    (String) option.get("optionValue"),
                    option.get("optionExtra")
            );

            optionsList.add(optionInfo);
        }

        return new General(optionsList);
    }
}
