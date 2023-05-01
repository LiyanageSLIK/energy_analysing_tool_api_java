package com.greenbill.greenbill.service;

import com.greenbill.greenbill.entity.SubscriptionPlanEntity;
import com.greenbill.greenbill.enumeration.*;
import com.greenbill.greenbill.repository.SubscriptionPlanRepository;
import com.greenbill.greenbill.repository.TariffRepository;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UploadHandlerService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private TariffRepository tariffRepository;

    public void uploadSubscriptionPlan(MultipartFile file) {

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
                        subscriptionPlan.setStatus(Status.valueOf(row[7]));

                        return subscriptionPlan;
                    })
                    .collect(Collectors.toList());

            subscriptionPlanRepository.saveAll(subscriptionPlanList);

        } catch (CsvException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
