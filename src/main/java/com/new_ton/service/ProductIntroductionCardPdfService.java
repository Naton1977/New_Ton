package com.new_ton.service;

import com.itextpdf.text.DocumentException;
import com.lider.domain.dto.PrintTestReportDto;

import java.io.IOException;

public interface ProductIntroductionCardPdfService {
    boolean productIntroductionCard(PrintTestReportDto printTestReportDto) throws DocumentException, IOException;
}
