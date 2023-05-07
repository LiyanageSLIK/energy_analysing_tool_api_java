package com.greenbill.greenbill.service;

import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.entity.TariffEntity;
import com.greenbill.greenbill.enumeration.*;
import com.greenbill.greenbill.repository.TariffRepository;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UploadHandlerService {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private TariffRepository tariffRepository;

    public void uploadSubscriptionPlansAsCSV(MultipartFile file) {

        try {
            List<SubscriptionPlanEntity> subscriptionPlanList = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                    .withSkipLines(1) // Skip header row
                    .build()
                    .readAll()
                    .stream()
                    .map(row -> {
                        SubscriptionPlanEntity subscriptionPlan = new SubscriptionPlanEntity();
                        subscriptionPlan.setName(SubscriptionPlanName.valueOf(row[0]));
                        subscriptionPlan.setRate(Float.valueOf(row[1]));
                        subscriptionPlan.setCurrencyCode(CurrencyCode.valueOf(row[2]));
                        subscriptionPlan.setCycle(Cycle.valueOf(row[3]));
                        subscriptionPlan.setPlanType(PlanType.valueOf(row[4]));
                        subscriptionPlan.setMaxNumProject(Integer.valueOf(row[5]));
                        subscriptionPlan.setMaxNumNode(Integer.valueOf(row[6]));
                        subscriptionPlan.setStatus(Status.ACTIVE);
                        return subscriptionPlan;
                    })
                    .collect(Collectors.toList());

            subscriptionService.AddNewSubscriptionPlanList(subscriptionPlanList);

        } catch (CsvException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void uploadTariffAsCSV(MultipartFile file) {

        try {
            String batchId=new Date().toString();
            List<TariffEntity> tariffEntityList = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                    .withSkipLines(1) // Skip header row
                    .build()
                    .readAll()
                    .stream()
                    .map(row -> {
                        TariffEntity tariffEntity = new TariffEntity();
                        tariffEntity.setBatchId(batchId);
                        try {
                            tariffEntity.setEffectiveFrom(String.valueOf(row[0]));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        tariffEntity.setCategory(ProjectType.valueOf(row[1]));
                        tariffEntity.setCountry(String.valueOf(row[2]));
                        tariffEntity.setCurrencyCode(CurrencyCode.valueOf(row[3]));
                        tariffEntity.setLimitedFrom(Double.valueOf(row[4]));
                        tariffEntity.setLimitedTo(Double.valueOf(row[5]));
                        tariffEntity.setLowerLimit(Double.valueOf(row[6]));
                        tariffEntity.setUpperLimit(Double.valueOf(row[7]));
                        tariffEntity.setEnergyCharge(Double.valueOf(row[8]));
                        tariffEntity.setFixedCharge(Double.valueOf(row[9]));
                        tariffEntity.setLevy(Double.valueOf(row[10]));
                        tariffEntity.setStatus(Status.ACTIVE);
                        return tariffEntity;
                    })
                    .collect(Collectors.toList());
            tariffRepository.updateStatusByStatus(Status.INACTIVE,Status.ACTIVE);
            tariffRepository.saveAll(tariffEntityList);

        } catch (CsvException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
