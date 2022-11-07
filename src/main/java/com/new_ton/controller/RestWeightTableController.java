package com.new_ton.controller;


import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping({"/api/v1"})
@RestController
public class RestWeightTableController {

    private final WeightLogTableService weightLogTableService;
    private final GetFioOperService getFioOperService;
    private final ProductTableService productTableService;
    private final PrintDischargePageService printDischargePageService;
    private final PrintTestReportService printTestReportService;
    private final GetDataForRecipePageService getDataForRecipePageService;


    @PostMapping({"/getTableData"})
    public WeightLogTableResponseDto insertWeighingLog(WeighingLogRequestDto weighingLogRequestDto) {
        return this.weightLogTableService.getWeightLogTableData(weighingLogRequestDto);
    }

    @GetMapping({"/getFioOper"})
    public List<String> getFioOper() {
        return this.getFioOperService.getFioOper();
    }

    @PostMapping({"/getProductTableData"})
    public ProductTableResponseDto getProductTableData(ProductTableRequestDto productTableRequestDto) {
        return this.productTableService.getProductTableDate(productTableRequestDto);
    }

    @PostMapping({"/printDischargePage"})
    public ResponseEntity<?> printDischargePage(@RequestParam int id) throws DocumentException, IOException {
        boolean result = this.printDischargePageService.printDischargePage(id);
        return result ? ResponseEntity.ok("") : ResponseEntity.notFound().build();
    }

    @PostMapping({"/printTestReport"})
    public ResponseEntity<?> printTestReport(@RequestParam int id) {
        boolean result = this.printTestReportService.printTestReport(id);
        return result ? ResponseEntity.ok("") : ResponseEntity.notFound().build();
    }

    @PostMapping({"/recipePageData"})
    public ResponseEntity<?> recipePageData(@RequestParam int id) {
        RecipePageDataDto recipePageDataDto = this.getDataForRecipePageService.getDataForRecipePage(id);
        return recipePageDataDto != null ? ResponseEntity.ok(recipePageDataDto) : ResponseEntity.notFound().build();
    }
}