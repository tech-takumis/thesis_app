package com.hashjosh.agripro.hpj_insurance.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.hpj_insurance.exception.FileUploadException;
import com.hashjosh.agripro.hpj_insurance.exception.InsuranceApplicationException;
import com.hashjosh.agripro.hpj_insurance.exception.InsuranceException;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceField;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Component
public class FileValidator implements FieldValidatorStrategy{
    private static final Path UPLOAD_DIR = Path.of("uploads");

    static {
        try{
            Files.createDirectories(UPLOAD_DIR);
        }catch (IOException ex){
            throw new FileUploadException("Unable to create upload directory");
        }
    }

    @Override
    public void validate(InsuranceField field, JsonNode value) {
        if(!value.isTextual()){
            throw new InsuranceApplicationException(
                    "Field '" + field.getFieldName() + "' must be a file (FILE)",
                    "Invalid file value",
                    HttpStatus.BAD_REQUEST.value()
            );
        }
    }

    public static String saveFile(JsonNode submittedValue, List<MultipartFile> files) {
        System.out.println("Saving file");
        String originalFileName = submittedValue.asText();

        MultipartFile targetFile = files.stream()
                .filter(f -> f.getOriginalFilename() != null
                        && f.getOriginalFilename().equals(originalFileName))
                .findFirst()
                .orElseThrow(() -> new InsuranceApplicationException(
                        "File '" + originalFileName + "' not found in uploaded files",
                        "Missing uploaded file",
                        HttpStatus.BAD_REQUEST.value()
                ));

        try{
            String uniqueFileName = UUID.randomUUID() + "-"+originalFileName;
            Path destination = UPLOAD_DIR.resolve(uniqueFileName);
            Files.copy(targetFile.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + uniqueFileName;
        }catch (IOException ex){
            throw new InsuranceApplicationException(
                    "File to save file "+ originalFileName + "",
                    "File save error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

}
