package com.example.jadxserver.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.jadxserver.model.DecompileResponse;
import com.example.jadxserver.utils.ZipUtils;

import java.io.*;
import java.util.UUID;

@Service
public class JadxDecompileService {

    public DecompileResponse decompile(MultipartFile file) throws IOException, InterruptedException {
        File tempFile = null;
        File outputDir = null;
        File zipFile = null;
        try {
            String tempFileName = UUID.randomUUID().toString();
            tempFile = new File(System.getProperty("java.io.tmpdir"), tempFileName + "_" + file.getOriginalFilename());
            file.transferTo(tempFile);
            outputDir = new File(System.getProperty("java.io.tmpdir"), "jadx_out_" + tempFileName);
            outputDir.mkdirs();
            zipFile = new File(System.getProperty("java.io.tmpdir"),  tempFileName + ".zip");

            String jadxPath = "/usr/bin/jadx"; // Replace with the actual path to jadx
            ProcessBuilder processBuilder = new ProcessBuilder(jadxPath, "-d", outputDir.getAbsolutePath(), tempFile.getAbsolutePath());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Capture output from jadx
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                ZipUtils.zipDirectory(outputDir, zipFile);
              // Create a zip out of the outputDir and return the URL and remove the source code
                return new DecompileResponse("success", "Decompilation successful.", null, zipFile.getName());
            } else {
                return new DecompileResponse("error", "Jadx exited with error code: " + exitCode + ". Output: " + output.toString(), null, null);
            }

        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
            if (outputDir != null && outputDir.exists()) {
                deleteDirectory(outputDir);
            }
            if(zipFile != null && zipFile.exists()){
                //zipFile.delete(); //Keep zip file, can be downloaded
            }

        }
    }

    // Helper function to delete a directory recursively
    private void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}