package app.netbooks.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.annotations.AuhenticatedOnly;
import app.netbooks.backend.authentication.AuthenticatedUser;
import app.netbooks.backend.dtos.response.PlanEditionResponse;
import app.netbooks.backend.models.PlanEdition;
import app.netbooks.backend.services.PlansEditionsService;

@RestController
@RequestMapping("/plans/editions")
public class PlansEditionsController {
    @Autowired
    private PlansEditionsService plansEditionService;

    @AuhenticatedOnly
    @GetMapping("/me")
    public ResponseEntity<PlanEditionResponse> findAvailableById(
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        PlanEdition edition = plansEditionService.findBySubscriber(
            user.getUser().getUuid()
        );

        PlanEditionResponse response = new PlanEditionResponse(edition);
        return ResponseEntity.ok().body(response);
    };
};
