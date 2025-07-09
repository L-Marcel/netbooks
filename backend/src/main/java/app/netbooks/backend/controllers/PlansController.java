package app.netbooks.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.authentication.AuthenticatedUser;
import app.netbooks.backend.dtos.response.BenefitResponse;
import app.netbooks.backend.dtos.response.PlanResponse;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Plan;
import app.netbooks.backend.models.PlanEdition;
import app.netbooks.backend.services.PlansBenefitsService;
import app.netbooks.backend.services.PlansEditionsService;
import app.netbooks.backend.services.PlansService;

@RestController
@RequestMapping("/plans")
public class PlansController {
    @Autowired
    private PlansService plansService;

    @Autowired
    private PlansBenefitsService plansBenefitsService;

    @Autowired
    private PlansEditionsService plansEditionsService;

    @GetMapping
    public ResponseEntity<List<PlanResponse>> findAll() {
        List<Plan> plans = this.plansService.findAll();
        Map<Integer, List<Benefit>> mappedBenefits = this.plansBenefitsService.mapAllByPlan();
        Map<Integer, List<PlanEdition>> mappedEditions = this.plansEditionsService.mapAllByPlan();

        List<PlanResponse> response = PlanResponse.fromList(
            plans, 
            mappedBenefits, 
            mappedEditions
        );
        return ResponseEntity.ok().body(response);
    };

    @GetMapping("/availables")
    public ResponseEntity<List<PlanResponse>> findAllAvailable() {
        List<Plan> plans = this.plansService.findAllAvailable();
        Map<Integer, List<Benefit>> mappedBenefits = this.plansBenefitsService.mapAllAvailableByPlan();
        Map<Integer, List<PlanEdition>> mappedEditions = this.plansEditionsService.mapAllAvailableByPlan();

        List<PlanResponse> response = PlanResponse.fromList(
            plans, 
            mappedBenefits, 
            mappedEditions
        );
        return ResponseEntity.ok().body(response);
    };

    @GetMapping("/me/benefits")
    public ResponseEntity<List<BenefitResponse>> findById(
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        List<Benefit> benefits = this.plansBenefitsService.findAllBySubscriber(
            user.getUser().getUuid()
        );

        List<BenefitResponse> response = BenefitResponse.fromList(benefits);
        return ResponseEntity.ok().body(response);
    };
};
