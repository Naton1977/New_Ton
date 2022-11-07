package com.new_ton.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.new_ton.dao.MainTableDao;
import com.new_ton.dao.RawTableDao;
import com.new_ton.dao.UploadTableDao;
import com.new_ton.domain.dto.RecipePageDataDto;
import com.new_ton.domain.dto.SumWeightDto;
import com.new_ton.domain.entities.MainEntity;
import com.new_ton.domain.entities.RawEntity;
import com.new_ton.domain.entities.RecipeMainTableDto;
import com.new_ton.domain.entities.UnloadEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetDataForRecipePageServiceImpl implements GetDataForRecipePageService {
    private static final Logger log = LoggerFactory.getLogger(GetDataForRecipePageServiceImpl.class);
    private final MainTableDao mainTableDao;
    private final RawTableDao rawTableDao;
    private final UploadTableDao uploadTableDao;


    public RecipePageDataDto getDataForRecipePage(int idProd) {
        try {
            RecipePageDataDto recipePageDataDto = new RecipePageDataDto();
            Optional<MainEntity> mainEntityOptional = this.mainTableDao.findByIdpr(idProd);
            DateFormat dateMade = new SimpleDateFormat("yyyy-MM-dd");
            if (mainEntityOptional.isPresent()) {
                MainEntity mainEntity = (MainEntity)mainEntityOptional.get();
                recipePageDataDto.setAllTimeMade(mainEntity.getTimemade());
                recipePageDataDto.setNameProd(mainEntity.getNameprod());
                recipePageDataDto.setMainMass(mainEntity.getMass());
                recipePageDataDto.setDeg(mainEntity.getDeg());
                if (mainEntity.getDatemade() != null) {
                    recipePageDataDto.setDateMade(dateMade.format(mainEntity.getDatemade()));
                }
            }

            recipePageDataDto.setCurrentDate(dateMade.format(new Date()));
            List<RawEntity> rawEntityList = this.rawTableDao.findAllByIdProd(idProd);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            List<RecipeMainTableDto> recipeMainTableDtoList = new ArrayList();

            RecipeMainTableDto dto;
            for(Iterator var8 = rawEntityList.iterator(); var8.hasNext(); recipeMainTableDtoList.add(dto)) {
                RawEntity re = (RawEntity)var8.next();
                dto = new RecipeMainTableDto();
                dto.setStage(re.getStage());
                dto.setCode(re.getCode());
                dto.setNameraw(re.getNameraw());
                dto.setPercent(String.valueOf(re.getPercent()));
                dto.setMass(String.valueOf(re.getMass()));
                dto.setDevper(String.valueOf(re.getDevper()));
                dto.setDevmass(String.valueOf(re.getDevmass()));
                dto.setFactmass(String.valueOf(re.getFactmass()));
                dto.setFactmassdev(String.valueOf(re.getFactmassdev()));
                dto.setTempdep(String.valueOf(re.getTempdep()));
                dto.setWetdep(String.valueOf(re.getWetdep()));
                dto.setProdtemp(String.valueOf(re.getProdtemp()));
                if (re.getDatestart() != null) {
                    dto.setDatestart(dateFormat.format(re.getDatestart()));
                }

                if (re.getDatestop() != null) {
                    dto.setDatestop(dateFormat.format(re.getDatestop()));
                }

                dto.setTimemade(re.getTimemade());
                dto.setTimemix(re.getTimemix());
                dto.setDevturn(re.getDevturn());
                dto.setTurnmix(re.getTurnmix());
                dto.setFactturn(re.getFactturn());
                dto.setFacttimemix(re.getFacttimemix());
                dto.setEq(re.getEq());
                dto.setPastpart(re.getPastpart());
                if (re.getPastdate() != null) {
                    dto.setPastdate(dateFormat.format(re.getPastdate()));
                }
            }

            List<RecipeMainTableDto> recipeMainTableDtoListNew = recipeMainTableDtoList.stream().sorted(Comparator.comparing(RecipeMainTableDto::getStage)).collect(Collectors.toList());
            recipePageDataDto.setCollSpanStage1(recipeMainTableDtoListNew.stream().filter((rm) -> {
                return rm.getStage() == 1;
            }).count());
            recipePageDataDto.setCollSpanStage2(recipeMainTableDtoListNew.stream().filter((rm) -> {
                return rm.getStage() == 2;
            }).count());
            recipePageDataDto.setCollSpanStage3(recipeMainTableDtoListNew.stream().filter((rm) -> {
                return rm.getStage() == 3;
            }).count());
            recipePageDataDto.setRecipeMainTableDtoList(recipeMainTableDtoListNew);
            SumWeightDto sumWeightDto = new SumWeightDto();
            List<UnloadEntity> unloadEntityList = this.uploadTableDao.getUnloadEntityById(idProd);
            double sum = 0.0D;
            Iterator var12 = unloadEntityList.iterator();

            while(var12.hasNext()) {
                UnloadEntity entity = (UnloadEntity)var12.next();
                if (entity.getNumb() == 1) {
                    sumWeightDto.setBarrel1(String.valueOf(entity.getMass()));
                    sum += entity.getMass();
                }

                if (entity.getNumb() == 2) {
                    sumWeightDto.setBarrel2(String.valueOf(entity.getMass()));
                    sum += entity.getMass();
                }

                if (entity.getNumb() == 3) {
                    sumWeightDto.setBarrel3(String.valueOf(entity.getMass()));
                    sum += entity.getMass();
                }

                if (entity.getNumb() == 4) {
                    sumWeightDto.setBarrel4(String.valueOf(entity.getMass()));
                    sum += entity.getMass();
                }
            }

            sumWeightDto.setSum(String.format("%.2f", sum));
            recipePageDataDto.setSumWeightDto(sumWeightDto);

            RecipeMainTableDto recipeMainTableDto;
            Optional rawEntityOptionalStage2;
            RawEntity rawEntity;
            try {
                rawEntityOptionalStage2 = this.rawTableDao.findCode8Stage1(idProd);
                if (rawEntityOptionalStage2.isPresent()) {
                    rawEntity = (RawEntity)rawEntityOptionalStage2.get();
                    recipeMainTableDto = (RecipeMainTableDto)(new ObjectMapper()).convertValue(rawEntity, RecipeMainTableDto.class);
                    recipePageDataDto.setCode8Stage1(recipeMainTableDto);
                }
            } catch (Exception var16) {
                log.error("Error rawEntityOptionalStage1 : {}, {}", ExceptionUtils.getMessage(var16), ExceptionUtils.getMessage(var16.getCause()));
            }

            try {
                rawEntityOptionalStage2 = this.rawTableDao.findCode8Stage2(idProd);
                if (rawEntityOptionalStage2.isPresent()) {
                    rawEntity = (RawEntity)rawEntityOptionalStage2.get();
                    recipeMainTableDto = (RecipeMainTableDto)(new ObjectMapper()).convertValue(rawEntity, RecipeMainTableDto.class);
                    recipePageDataDto.setCode8Stage2(recipeMainTableDto);
                }
            } catch (Exception var15) {
                log.error("Error rawEntityOptionalStage2 : {}, {}", ExceptionUtils.getMessage(var15), ExceptionUtils.getMessage(var15.getCause()));
            }

            return recipePageDataDto;
        } catch (Exception var17) {
            log.error("Error getDataForRecipePage : {}, {}", ExceptionUtils.getMessage(var17), ExceptionUtils.getMessage(var17.getCause()));
            return new RecipePageDataDto();
        }
    }
}
