package com.example.jadxserver.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.jadxserver.model.DecompileResponse;
import com.example.jadxserver.service.JadxDecompileService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/decompile")
public class DecompileController {

    @Autowired
    private JadxDecompileService jadxDecompileService;

    @PostMapping
    public DecompileResponse decompile(@RequestParam("file") MultipartFile file) {
        try {
            return jadxDecompileService.decompile(file);
        } catch (Exception e) {
            return new DecompileResponse("error", e.getMessage(), null, null);
        }
    }
}