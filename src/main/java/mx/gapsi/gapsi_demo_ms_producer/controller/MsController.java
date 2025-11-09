package mx.gapsi.gapsi_demo_ms_producer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import mx.gapsi.commons.model.Base;
import mx.gapsi.commons.dto.CustomDto;
import mx.gapsi.commons.utils.InitObject;
import mx.gapsi.gapsi_demo_ms_producer.service.MsService;


@RestController
@RequestMapping("/api/ms-gapsi/")
public class MsController {

    @Autowired
    private MsService msService;

    @GetMapping
    public ResponseEntity<CustomDto> findAll(@RequestParam(name="sortPaginator", defaultValue = "", required = true) String sortPaginator) {
        Base base = new Base();
        try {
            InitObject.toBase(base, sortPaginator);
            msService.findAll(base);
            base.getCustomDto().setCode(HttpStatus.OK.value());
            base.getCustomDto().setDescription("Successfully");
            return new ResponseEntity<CustomDto>(base.getCustomDto(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            base.getCustomDto().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            base.getCustomDto().setDescription(e.getMessage());
            return new ResponseEntity<CustomDto>(base.getCustomDto(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
