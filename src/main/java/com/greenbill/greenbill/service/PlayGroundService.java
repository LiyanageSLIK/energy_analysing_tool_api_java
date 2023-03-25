package com.greenbill.greenbill.service;


import com.greenbill.greenbill.dto.request.NodeRequestDto;
import com.greenbill.greenbill.dto.response.CalculatedBillDto;
import com.greenbill.greenbill.dto.response.NodeGraphDetails;
import com.greenbill.greenbill.dto.response.ProjectGraphDetails;
import com.greenbill.greenbill.dto.response.SectionSummaryDto;
import com.greenbill.greenbill.entity.*;
import com.greenbill.greenbill.enumeration.CurrencyCode;
import com.greenbill.greenbill.enumeration.NodeType;
import com.greenbill.greenbill.enumeration.ProjectType;
import com.greenbill.greenbill.enumeration.Status;
import com.greenbill.greenbill.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PlayGroundService {
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RootRepository rootRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private ApplianceRepository applianceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TariffRepository tariffRepository;


    @Transactional
    public void addNode(NodeRequestDto nodeRequestDto, String userEmail) throws Exception {
        if (!validatePlayGroundNodeAccess(userEmail)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry You had reach your subscription limitations upgrade your plan for more benefits");
        }
        NodeType nodeType = nodeRequestDto.getNodeType();
        long projectId = extractProjectIdFromFrontEndId(nodeRequestDto.getFrontEndId());
        var root = rootRepository.findByProject_Id(projectId);
        var project = projectRepository.getReferenceById(projectId);
        if (nodeType == NodeType.Section) {
            SectionEntity savedSection = new SectionEntity();
            if (nodeRequestDto.getParentFrontEndId().equals("root")) {
                SectionEntity sectionToSave = new SectionEntity(nodeRequestDto);
                project.setLastUpdated(new Date());
                root.setProject(project);
                sectionToSave.setParent(root);
                savedSection = sectionRepository.save(sectionToSave);
            } else {
                String parentFrontEndId = nodeRequestDto.getParentFrontEndId();
                SectionEntity parentSection = sectionRepository.findByFrontEndId(parentFrontEndId);
                if (parentSection == null) {
                    throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cant map Nod:Parent Nod not Found ");
                }
                SectionEntity sectionToSave = new SectionEntity(nodeRequestDto);
                sectionToSave.setParent(parentSection);
                project.setLastUpdated(new Date());
                projectRepository.save(project);
                savedSection = sectionRepository.save(sectionToSave);
            }
        }
        if (nodeType == NodeType.Appliance) {
            ApplianceEntity savedAppliance = new ApplianceEntity();
            String parentFrontEndId = nodeRequestDto.getParentFrontEndId();
            SectionEntity parentSection = sectionRepository.findByFrontEndId(parentFrontEndId);
            if (parentSection == null || parentFrontEndId.equals("root")) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cant map Nod:Parent Nod not Found ");
            }
            ApplianceEntity applianceToSave = new ApplianceEntity(nodeRequestDto);
            applianceToSave.setParent(parentSection);
            project.setLastUpdated(new Date());
            projectRepository.save(project);
            savedAppliance = applianceRepository.save(applianceToSave);
        }
    }

    @Transactional
    public void updateNode(NodeRequestDto nodeRequestDto) throws Exception {
        NodeType nodeType = nodeRequestDto.getNodeType();
        long projectId = extractProjectIdFromFrontEndId(nodeRequestDto.getFrontEndId());
        var project = projectRepository.getReferenceById(projectId);
        if (nodeType == NodeType.Section) {
            String frontEndId = nodeRequestDto.getFrontEndId();
            var thisSection = sectionRepository.findByFrontEndId(frontEndId);
            if (thisSection == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "SectionDto not found");
            } else {
                thisSection.update(nodeRequestDto);
                project.setLastUpdated(new Date());
                projectRepository.save(project);
                thisSection = sectionRepository.save(thisSection);
            }
        }
        if (nodeType == NodeType.Appliance) {
            String frontEndId = nodeRequestDto.getFrontEndId();
            var thisAppliance = applianceRepository.findByFrontEndId(frontEndId);
            if (thisAppliance == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ApplianceDto not found");
            } else {
                thisAppliance.update(nodeRequestDto);
                project.setLastUpdated(new Date());
                projectRepository.save(project);
                thisAppliance = applianceRepository.save(thisAppliance);
            }
        }
    }

    public void deleteNode(String frontEndNodId) {
        SectionEntity section = sectionRepository.findByFrontEndId(frontEndNodId);
        ApplianceEntity appliance = applianceRepository.findByFrontEndId(frontEndNodId);
        if (section != null) {
            sectionRepository.delete(section);
        }
        if (appliance != null) {
            applianceRepository.delete(appliance);
        }
    }

    @Transactional
    public boolean validatePlayGroundNodeAccess(String email) {
        SubscriptionPlanEntity activePlan = subscriptionPlanRepository.getBySubscriptions_User_Email(email);
        int maxNodAllow = activePlan.getMaxNumNode();
        int currentNodCount = countNodeCountByUserEmail(email);
        return (currentNodCount < maxNodAllow);
    }

    @Transactional
    public NodeGraphDetails getSectionGraphsDetails(String frontEndSectionId) throws HttpClientErrorException {
        SectionEntity section = sectionRepository.findByFrontEndId(frontEndSectionId);
        if (section == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No Such Section");
        }
        return calculateNodeGraphDetails(section);
    }

    @Transactional
    public SectionSummaryDto getSectionSummary(String frontEndId) throws Exception {
        SectionEntity section = sectionRepository.findByFrontEndId(frontEndId);
        SectionSummaryDto sectionSummaryDto = new SectionSummaryDto(section);
        sectionSummaryDto.setChildren(section.getChildren());
        return sectionSummaryDto;
    }

    @Transactional
    public ProjectGraphDetails getProjectGraphsDetails(long projectId) throws HttpClientErrorException {
        ProjectEntity project = projectRepository.getFirstById(projectId);
        RootEntity root = project.getRoot();
        double totalUnits = 0;
        if (root == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No Such Project");
        }
        var children = root.getChildren();
        if (root == null) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "It's Empty Project");
        }
        List<NodeGraphDetails> resultsOfChildren = new ArrayList<>();
        for (var child : children) {
            var calculatedGraphDetails = calculateNodeGraphDetails(child);
            resultsOfChildren.add(calculatedGraphDetails);
            totalUnits = totalUnits + calculatedGraphDetails.getTotalUnits();
        }
        List<NodeGraphDetails> completedChildNodResults = new ArrayList<>();
        for (var child : resultsOfChildren) {
            child.setUnitPercentageOfParent(totalUnits);
            completedChildNodResults.add(child);
        }
        var result = new ProjectGraphDetails(project);
        result.setTotalUnits(totalUnits);
        result.setChildren(completedChildNodResults);
        return result;
    }

    @Transactional
    public CalculatedBillDto calculateBill(long projectId) throws HttpClientErrorException {
        var projectGraphDetails = getProjectGraphsDetails(projectId);
        var billCalculatorInputs = new BillCalculatorInputs(projectGraphDetails);
        return billCalculator(billCalculatorInputs);
    }


    private int countNodeCountByUserEmail(String email) {
        var rootList = rootRepository.findByProject_Subscription_User_Email(email);
        int counter = 0;
        if (rootList == null) {
            return counter;
        }
        for (var root : rootList) {
            var childSectionList = root.getChildren();
            if (childSectionList == null) {
                return counter;
            }
            for (var section : childSectionList) {
                counter = counter + countChildNode((SectionEntity) section);
            }
        }
        return counter;
    }

    private int countChildNode(SectionEntity sectionEntity) {
        int counter = 1;
        var children = sectionEntity.getChildren();
        for (var node : children) {
            if (node.getNodeType() == NodeType.Appliance) {
                counter = counter + 1;
            }
            if (node.getNodeType() == NodeType.Section) {
                counter = counter + countChildNode((SectionEntity) node);
            }
        }
        return counter;
    }

    private long extractProjectIdFromFrontEndId(String frontEndId) {
        String projectId = frontEndId.split("_")[1];
        return Long.parseLong(projectId);
    }

    private NodeGraphDetails calculateNodeGraphDetails(NodeEntity node) {
        if (node.getNodeType() == NodeType.Appliance && node.getStatus() == Status.ACTIVE) {
            var result = new NodeGraphDetails((ApplianceEntity) node);
            return result;
        }
        if (node.getNodeType() == NodeType.Section && node.getStatus() == Status.ACTIVE) {
            var result = new NodeGraphDetails(node);
            List<NodeGraphDetails> childrenOfResult = new ArrayList<>();
            double totalUnitOfSection = 0;
            SectionEntity section = (SectionEntity) node;
            var children = section.getChildren();
            if (children == null) {
                return result;
            }
            for (var childNod : children) {
                var resultOfChild = calculateNodeGraphDetails(childNod);
                totalUnitOfSection = totalUnitOfSection + resultOfChild.getTotalUnits();
                childrenOfResult.add(resultOfChild);
            }
            result.setTotalUnits(totalUnitOfSection);
            List<NodeGraphDetails> completedChildNodResults = new ArrayList<>();
            for (var child : childrenOfResult) {
                child.setUnitPercentageOfParent(totalUnitOfSection);
                completedChildNodResults.add(child);
            }
            result.setChildren(completedChildNodResults);
            return result;
        }
        return null;
    }


    private CalculatedBillDto billCalculator(BillCalculatorInputs inputs) {
        if (inputs.getCategory() == ProjectType.Domestic || inputs.getCategory() == ProjectType.ReligiousAndCharitable) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            var category = inputs.getCategory();
            var totalUnits = inputs.getTotalUnits();
            double levy = 0.00;
            double billAmount = 0.00;
            double totalCharge = 0.00;
            double usageCharge = 0.00;
            double fixedCharge = 0.00;
            List<Object> calculationSteps = new ArrayList<>();
            var tariff = tariffRepository.getByLimitedFromLessThanEqualAndLimitedToGreaterThanEqualAndCategoryOrderByLowerLimitAsc(totalUnits, totalUnits, category);
//            calculationSteps.add(new String("Calculation:"));
            CurrencyCode currencyCode = CurrencyCode.LKR;
            for (var block : tariff) {
                var lowerLimit = block.getLowerLimit();
                var upperLimit = block.getUpperLimit();
                currencyCode = block.getCurrencyCode();
                if (!(lowerLimit <= totalUnits && totalUnits <= upperLimit) && totalUnits > upperLimit) {
                    if (lowerLimit == 0) {
                        var charge = (upperLimit - lowerLimit) * block.getEnergyCharge();
                        usageCharge += charge;
//                        calculationSteps.add(String.format("%10.0f x %4.2f =%10.2f", (upperLimit - lowerLimit), block.getEnergyCharge(), decimalFormat.format(charge)));
                        calculationSteps.add(String.format("%10.0f x %4.2f =%10.2f", (upperLimit - lowerLimit), block.getEnergyCharge(), (charge)));
                    }
                    if (lowerLimit != 0) {
                        var charge = (upperLimit - lowerLimit + 1) * block.getEnergyCharge();
                        usageCharge += charge;
                        calculationSteps.add(String.format("%10.0f x %4.2f =%10.2f", (upperLimit - lowerLimit + 1), block.getEnergyCharge(), (charge)));
                    }
                }
                if (lowerLimit <= totalUnits && totalUnits <= upperLimit) {
                    if (lowerLimit == 0) {
                        var charge = (totalUnits - lowerLimit) * block.getEnergyCharge();
                        usageCharge += charge;
                        calculationSteps.add(String.format("%10.0f x %4.2f =%10.2f", (totalUnits - lowerLimit), block.getEnergyCharge(), (charge)));
                    }
                    if (lowerLimit != 0) {
                        var charge = (totalUnits - lowerLimit + 1) * block.getEnergyCharge();
                        usageCharge += charge;
                        calculationSteps.add(String.format("%10.0f x %4.2f =%10.2f", (totalUnits - lowerLimit + 1), block.getEnergyCharge(), (charge)));
                    }
                    fixedCharge += block.getFixedCharge();
                    totalCharge = usageCharge + fixedCharge;
                    levy = totalCharge * block.getLevy();
                    billAmount = totalCharge + levy;
                }
            }
            var result = new CalculatedBillDto(currencyCode);
            result.setTotalUnits(totalUnits);
            result.setUsageCharge(usageCharge);
            result.setFixedCharge(fixedCharge);
            result.setTotalCharge(totalCharge);
            result.setLevy(levy);
            result.setBillAmount(billAmount);
            result.setCalculationSteps(calculationSteps);
            return result;
        }
        return null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class BillCalculatorOutputs {

        private double usageCharge;
        private double fixedCharge;
        private double totalCharge;
        private double levy;
        private double billAmount;
        private List<String> calculationSteps;


    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class BillCalculatorInputs {

        private double totalUnits;
        private double dayUnits;
        private double peakUnits;
        private double offPeakUnits;
        private ProjectType category;

        public BillCalculatorInputs(ProjectGraphDetails graphDetails) {
            setTotalUnits(graphDetails.getTotalUnits());
            setCategory(graphDetails.getProjectType());
        }

        public double getTotalUnits() {
            return Math.round(totalUnits);
        }
    }
}
