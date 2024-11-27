package com.gustavo.dummyaxon.query.rent.application.controllers;

import com.gustavo.dummyaxon.query.rent.application.dtos.viewmodels.RentViewModel;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/rents")
public class RentRestController {

    private final QueryGateway queryGateway;

    public RentRestController(final QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public ResponseEntity<List<RentViewModel>> listAll() {
        return ResponseEntity.ok(this.queryGateway.query("listAll", null, ResponseTypes.multipleInstancesOf(RentViewModel.class)).join());
    }
}
