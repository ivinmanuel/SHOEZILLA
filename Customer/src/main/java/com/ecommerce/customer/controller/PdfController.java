package com.ecommerce.customer.controller;

import com.ecommerce.library.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayInputStream;

@Controller
public class PdfController {
    private PdfService pdfService;
@Autowired
    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }
    @GetMapping("/report")
    public ResponseEntity<InputStreamResource> pdfCreator(){
        ByteArrayInputStream pdf=pdfService.createPdf();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-dispositionn","inline;file=lcwd.pdf");
        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }
}
