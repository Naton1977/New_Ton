package com.new_ton.service.tasks;

import com.itextpdf.text.DocumentException;
import com.new_ton.dao.PrintDao;
import com.new_ton.service.PrintDischargePageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PrintDocumentTaskService {

    private final PrintDischargePageService printDischargePageService;
    private final PrintDao printDao;


    @Scheduled(fixedDelay = 5000L)
    void printDocument() throws DocumentException, IOException {
        List<Integer> idList = this.printDao.getIdPrintTask();
        Iterator var2 = idList.iterator();

        while (var2.hasNext()) {
            Integer id = (Integer) var2.next();
            log.info("Start print task id_pr -" + id);
            boolean result = this.printDischargePageService.printDischargePage(id);
            if (result) {
                this.printDao.changeCode(id);
                log.info("End print task id_pr -" + id);
            }
        }
    }
}
