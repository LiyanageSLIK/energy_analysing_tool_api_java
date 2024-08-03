package com.greenbill.greenbill.service;


import com.greenbill.greenbill.dto.request.NodeRequestDto;
import com.greenbill.greenbill.dto.response.*;
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

import java.util.*;

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

    @Autowired
    private SolarPanelRepository solarPanelRepository;

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
        long userId = extractUserIdFromFrontEndId(nodeRequestDto.getFrontEndId());
        var project = projectRepository.getReferenceById(projectId);
        var user = userRepository.getReferenceById(userId);
        if (!validatePlayGroundNodeAccess(user.getEmail())) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Sorry You had reach your subscription limitations upgrade your plan for more benefits");
        }
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

    public GraphNodeDto getProjectGraphData(long projectId) {
        var project = projectRepository.getFirstById(projectId);
        if (project == null || project.getRoot() == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No Such Project");
        }
        return getNestedTotalUnits(project.getRoot());
    }

    private GraphNodeDto getNestedTotalUnits(NodeEntity node) {
        if (node == null || NodeType.Appliance.equals(node.getNodeType())) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "No such node found");
        }
        var children = NodeType.Root.equals(node.getNodeType()) ?
                ((RootEntity) node).getChildren() :
                ((SectionEntity) node).getChildren();

        double totalUnits = 0;
        var childGraphNodes = new ArrayList<GraphNodeDto>();
        for (var child : children) {
            if (NodeType.Appliance.equals(child.getNodeType())) {
                var appliance = (ApplianceEntity) child;
                totalUnits += appliance.getQuantity() * appliance.getHours() * appliance.getWattRate() * 0.03;
            } else {
                var childGraphNode = getNestedTotalUnits(child);
                childGraphNode.setParentName(node.getName());
                totalUnits += childGraphNode.getTotalUnits();
                childGraphNodes.add(childGraphNode);
            }
        }
        return new GraphNodeDto(node.getFrontEndId(), node.getName(), totalUnits, childGraphNodes);
    }

    @Transactional
    public SectionSummaryDto getSectionSummary(String frontEndId) {
        SectionEntity section = sectionRepository.findByFrontEndId(frontEndId);
        SectionSummaryDto sectionSummaryDto = new SectionSummaryDto(section);
        sectionSummaryDto.setChildren(section.getChildren());
        return sectionSummaryDto;
    }

    @Transactional
    public CalculatedBillDto calculateBill(long projectId) throws HttpClientErrorException {
        var projectEnergyConsumptionDetails = getProjectEnergyConsumptionDetails(projectId);
        var solarEnergyIncomeUnits = getSolarEnergyIncomeUnits(projectId);
        var solarTariffRate = getSolarTariffRate(projectId);
        var billCalculatorInputs = new BillCalculatorInputs(projectEnergyConsumptionDetails, solarEnergyIncomeUnits, solarTariffRate);
        return billCalculator(billCalculatorInputs);
    }

    private double getSolarTariffRate(long projectId) {
        Optional<ProjectEntity> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No Such Project");
        }
        ProjectEntity project = projectOptional.get();
        SolarTariffEntity solarTariff = project.getSolarTariff();
        if (solarTariff == null) {
            return 0;
        }
        return solarTariff.getSolarTariffRate();
    }

    private double getSolarEnergyIncomeUnits(long projectId) {
        List<SolarPanelEntity> solarPanels = solarPanelRepository.findByProject_Id(projectId);
        if (solarPanels == null || solarPanels.isEmpty()) {
            return 0;
        }
        return solarPanels.stream()
                .map(panel -> panel.getQuantity() * panel.getWattRate() * panel.getHours() * 30 / 1000)
                .reduce(0.0, Double::sum);
    }

    private ProjectEnergyConsumptionDetailsDto getProjectEnergyConsumptionDetails(long projectId) throws HttpClientErrorException {
        ProjectEntity project = projectRepository.getFirstById(projectId);
        RootEntity root = project.getRoot();
        if (root == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No Such Project");
        }
        var children = root.getChildren();
        if (children == null) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "It's Empty Project");
        }

        double totalUnits = 0;
        List<NodeEnergyConsumptionDetailsDto> resultsOfChildren = new ArrayList<>();
        for (var child : children) {
            var calculatedEnergyConsumptionDetail = calculateNodeEnergyConsumptionDetails(child);
            if (calculatedEnergyConsumptionDetail != null) {
                resultsOfChildren.add(calculatedEnergyConsumptionDetail);
                totalUnits = totalUnits + calculatedEnergyConsumptionDetail.getTotalUnits();
                calculatedEnergyConsumptionDetail.setUnitPercentageOfParent(totalUnits);
                percentageReSetter(calculatedEnergyConsumptionDetail, totalUnits);
            }
        }

        var result = new ProjectEnergyConsumptionDetailsDto(project);
        result.setTotalUnits(totalUnits);
        result.setChildren(resultsOfChildren);
        return result;
    }

    private void percentageReSetter(NodeEnergyConsumptionDetailsDto input, double totalUnitsOfProject) {
        var children = input.getChildren();
        input.setUnitPercentageOfProject(totalUnitsOfProject);
        if (children != null) {
            for (var child : children) {
                percentageReSetter(child, totalUnitsOfProject);
            }
        }
    }

    private NodeEnergyConsumptionDetailsDto calculateNodeEnergyConsumptionDetails(NodeEntity node) {
        if (node.getNodeType() == NodeType.Appliance && node.getStatus() == Status.ACTIVE) {
            return new NodeEnergyConsumptionDetailsDto((ApplianceEntity) node);
        }
        if (node.getNodeType() == NodeType.Section && node.getStatus() == Status.ACTIVE) {
            var result = new NodeEnergyConsumptionDetailsDto(node);
            SectionEntity section = (SectionEntity) node;
            var children = section.getChildren();
            if (children == null || children.isEmpty()) {
                return result;
            }

            double totalUnitOfSection = 0;
            List<NodeEnergyConsumptionDetailsDto> childrenOfResult = new ArrayList<>();
            for (var childNod : children) {
                var resultOfChild = calculateNodeEnergyConsumptionDetails(childNod);
                if (resultOfChild != null) {
                    totalUnitOfSection = totalUnitOfSection + resultOfChild.getTotalUnits();
                    childrenOfResult.add(resultOfChild);
                }
            }
            result.setTotalUnits(totalUnitOfSection);
            for (var child : childrenOfResult) {
                child.setUnitPercentageOfParent(totalUnitOfSection);
            }
            result.setChildren(childrenOfResult);
            return result;
        }
        return null;
    }


    public CalculatedBillDto simpleBillCalculator(double units) throws HttpClientErrorException {
        var inputs = new BillCalculatorInputs();
        inputs.setCategory(ProjectType.Domestic);
        inputs.setTotalUnits(units);
        return billCalculator(inputs);
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

    private long extractUserIdFromFrontEndId(String frontEndId) {
        String userId = frontEndId.split("_")[0];
        return Long.parseLong(userId);
    }

    private CalculatedBillDto billCalculator(BillCalculatorInputs inputs) {
        var category = inputs.getCategory();
        if (category == ProjectType.Domestic || category == ProjectType.ReligiousAndCharitable) {
            double totalUnits = inputs.getTotalUnits();
            double solarUnits = inputs.getSolarUnits();
            if (totalUnits >= solarUnits) {
                return calculatePositiveBill(inputs);
            } else {
                return calculateNegativeBill(inputs);
            }
        }
        return null;
    }

    private CalculatedBillDto calculateNegativeBill(BillCalculatorInputs inputs) {
        double effectiveUnits = inputs.getSolarUnits() - inputs.getTotalUnits();
        double totalIncome = effectiveUnits * inputs.getSolarTariffRate();
        var calculationStep = String.format("%10.0f x %4.2f =%10.2f", effectiveUnits, inputs.getSolarTariffRate(), totalIncome);

        CurrencyCode currencyCode = CurrencyCode.LKR;
        var result = new CalculatedBillDto(currencyCode);
        result.setTotalUnits(inputs.getTotalUnits());
        result.setSolarUnits(inputs.getSolarUnits());
        result.setTotalIncome(totalIncome);
        result.setCalculationSteps(Collections.singletonList(calculationStep));
        return result;
    }

    private CalculatedBillDto calculatePositiveBill(BillCalculatorInputs inputs) {
        var category = inputs.getCategory();
        double effectiveUnits = inputs.getTotalUnits() - inputs.getSolarUnits();
        double levy = 0.00;
        double billAmount = 0.00;
        double totalCharge = 0.00;
        double usageCharge = 0.00;
        double fixedCharge = 0.00;
        List<Object> calculationSteps = new ArrayList<>();

        var tariff = tariffRepository.getByLimitedFromLessThanEqualAndLimitedToGreaterThanEqualAndCategoryAndStatusOrderByLowerLimitAsc(effectiveUnits, effectiveUnits, category, Status.ACTIVE);
        CurrencyCode currencyCode = CurrencyCode.LKR;
        for (var block : tariff) {
            var lowerLimit = block.getLowerLimit();
            var upperLimit = block.getUpperLimit();
            currencyCode = block.getCurrencyCode();
            if (!(lowerLimit <= effectiveUnits && effectiveUnits <= upperLimit) && effectiveUnits > upperLimit) {
                if (lowerLimit == 0) {
                    var charge = (upperLimit - lowerLimit) * block.getEnergyCharge();
                    usageCharge += charge;
                    calculationSteps.add(String.format("%10.0f x %4.2f =%10.2f", (upperLimit - lowerLimit), block.getEnergyCharge(), (charge)));
                }
                if (lowerLimit != 0) {
                    var charge = (upperLimit - lowerLimit + 1) * block.getEnergyCharge();
                    usageCharge += charge;
                    calculationSteps.add(String.format("%10.0f x %4.2f =%10.2f", (upperLimit - lowerLimit + 1), block.getEnergyCharge(), (charge)));
                }
            }
            if (lowerLimit <= effectiveUnits && effectiveUnits <= upperLimit) {
                if (lowerLimit == 0) {
                    var charge = (effectiveUnits - lowerLimit) * block.getEnergyCharge();
                    usageCharge += charge;
                    calculationSteps.add(String.format("%10.0f x %4.2f =%10.2f", (effectiveUnits - lowerLimit), block.getEnergyCharge(), (charge)));
                }
                if (lowerLimit != 0) {
                    var charge = (effectiveUnits - lowerLimit + 1) * block.getEnergyCharge();
                    usageCharge += charge;
                    calculationSteps.add(String.format("%10.0f x %4.2f =%10.2f", (effectiveUnits - lowerLimit + 1), block.getEnergyCharge(), (charge)));
                }
                fixedCharge += block.getFixedCharge();
                totalCharge = usageCharge + fixedCharge;
                levy = totalCharge * block.getLevy();
                billAmount = totalCharge + levy;
            }
        }
        var result = new CalculatedBillDto(currencyCode);
        result.setTotalUnits(inputs.getTotalUnits());
        result.setSolarUnits(inputs.getSolarUnits());
        result.setUsageCharge(usageCharge);
        result.setFixedCharge(fixedCharge);
        result.setTotalCharge(totalCharge);
        result.setLevy(levy);
        result.setBillAmount(billAmount);
        result.setCalculationSteps(calculationSteps);
        return result;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class BillCalculatorInputs {
        private double solarUnits;
        private double solarTariffRate;
        private double totalUnits;
        private double dayUnits;
        private double peakUnits;
        private double offPeakUnits;
        private ProjectType category;

        public BillCalculatorInputs(ProjectEnergyConsumptionDetailsDto graphDetails, double solarUnits, double solarTariffRate) {
            setTotalUnits(graphDetails.getTotalUnits());
            setCategory(graphDetails.getProjectType());
            setSolarUnits(solarUnits);
            setSolarTariffRate(solarTariffRate);
        }

        public double getTotalUnits() {
            return Math.round(totalUnits);
        }
    }
}
