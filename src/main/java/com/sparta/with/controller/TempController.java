package com.sparta.with.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {
    @GetMapping("/check")
    public ResponseEntity check(){
        return ResponseEntity.ok().body("success");
    }
}
