package com.example.jadxserver.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.jadxserver.model.DecompileResponse;

import java.io.*;
import java.util.UUID;

@Service
public class JadxDecompileService {

    public DecompileResponse decompile(MultipartFile file) throws IOException, InterruptedException {
        File tempFile = null;
        File outputDir = null;
        try {
            String tempFileName = UUID.randomUUID().toString();
            tempFile = new File(System.getProperty("java.io.tmpdir"), tempFileName + "_" + file.getOriginalFilename());
            file.transferTo(tempFile);
            outputDir = new File(System.getProperty("java.io.tmpdir"), "jadx_out_" + tempFileName);
            outputDir.mkdirs();

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
              // Create a zip out of the outputDir and return the URL and remove the source code
                return new DecompileResponse("success", "Decompilation successful.", null, "download_url_if_zipped");
            } else {
                return new DecompileResponse("error", "Jadx exited with error code: " + exitCode + ". Output: " + output.toString(), null, null);
            }

        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
            if (outputDir != null) {
              // Clean up the output directory after it can be downloaded as zip
              //  deleteDirectory(outputDir);
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