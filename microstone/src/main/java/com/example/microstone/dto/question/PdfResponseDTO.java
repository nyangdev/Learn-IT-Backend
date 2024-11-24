package com.example.microstone.dto.question;


import com.example.microstone.domain.PreprocessingPdf;
import lombok.Data;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Data
public class PdfResponseDTO {


    private Long id;
    private String fileName;
    private String status;
    private String taskId;
    private int numberOfQuestion;
    public PdfResponseDTO(PreprocessingPdf pdf){
        this.id=pdf.getId();
        this.fileName=pdf.getFileName();
        this.status=pdf.getStatus();
        this.numberOfQuestion = pdf.getNumberOfQuestions();
        this.taskId=pdf.getTaskId();
    }
}
