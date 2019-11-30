package com.electric.noinvade.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class StaticController {
    @GetMapping({"/", "/family-view", "/login"})
    public String index() {
        return "index";
    }

    @GetMapping("/assets/report.pdf")
    public ResponseEntity<StreamingResponseBody> getPage() {
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("report.pdf"));

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(inputStream::transferTo);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
