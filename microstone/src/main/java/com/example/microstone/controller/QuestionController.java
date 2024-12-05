//package com.example.microstone.controller;
//
//import com.example.microstone.domain.PreprocessingPdf;
//import com.example.microstone.domain.Question;
//import com.example.microstone.dto.question.ChangeStatusDTO;
//import com.example.microstone.dto.question.PdfNameUpdateDTO;
//import com.example.microstone.dto.question.PreprocessingPdfDTO;
//import com.example.microstone.dto.question.PdfResponseDTO;
//import com.example.microstone.dto.paging.PageResponseDTO;
//import com.example.microstone.dto.question.QuestionResponseDTO;
//import com.example.microstone.dto.question.QuestionListRequestDTO;
//import com.example.microstone.repository.PreprocessingPdfRepository;
//import com.example.microstone.repository.QuestionRepository;
//import com.example.microstone.repository.UserRepository;
//import com.example.microstone.service.QuestionService;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.token.TokenService;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@Log4j2
//public class QuestionController {
//
//    @Autowired
//    PreprocessingPdfRepository pdfRepository;
//
//    @Autowired
//    QuestionRepository questionRepository;
//
//    @Autowired
//    QuestionService questionService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    TokenService tokenService;
//
//    @PostMapping("/api/question/preprocessing")
//    @ResponseBody
//    public ResponseEntity<?> preprocessQuestion(@RequestHeader("Authorization")String token,  @RequestBody PreprocessingPdfDTO preprocessingPdfDTO) {
//        Long uid = tokenService.getUidFromToken(token);
//        PreprocessingPdf preprocessingPdf = PreprocessingPdf.builder()
//                .createAt(LocalDateTime.now())
//                .taskId(preprocessingPdfDTO.getTask_id())
//                .uid(userRepository.findById(uid).get())
//                .status(preprocessingPdfDTO.getStatus())
//                .fileName(preprocessingPdfDTO.getFile_name())
//                .build();
//        pdfRepository.save(preprocessingPdf);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @PostMapping("/api/question/changeStatus")
//    @ResponseBody
//    public ResponseEntity<?> changeStatus(@RequestBody ChangeStatusDTO changeStatusDTO) {
//        PreprocessingPdf pdf = pdfRepository.findByTaskId(changeStatusDTO.getTask_id());
//        pdf.setStatus(changeStatusDTO.getStatus());
//        pdfRepository.save(pdf);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @GetMapping("/api/question/getStatus/{pdfId}")
//    public ResponseEntity<?> getStatus(@PathVariable(name="pdfId") Long pdfId) {
//        PreprocessingPdf pdf = pdfRepository.findById(pdfId).get();
//        Map<String,Object> response = new HashMap<>();
//        response.put("response",200);
//        response.put("status",pdf.getStatus());
//        return ResponseEntity.ok(response);
//
//    }
//
//
//    @GetMapping("/api/question/pdfList/{page}")
//    public ResponseEntity<?> getPdfList(Pageable pageable, @PathVariable("page") Integer page, @RequestHeader("Authorization") String token) {
//        if (page == null)
//            page = 1;
//
//        Long uid = tokenService.getUidFromToken(token);
//        Page<PdfResponseDTO> pdfs = questionService.pagingPdf(pageable, page, uid);
//        if(pdfs == null){
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        PageResponseDTO pages = new PageResponseDTO(pdfs);
//
//        return ResponseEntity.ok(pages);
//    }
//
//    @GetMapping("/api/question/reminderPdfList/{page}")
//    public ResponseEntity<?> getReminderPdfList(Pageable pageable, @PathVariable("page") Integer page, @RequestHeader("Authorization") String token) {
//        if (page == null)
//            page = 1;
//
//        Long uid = tokenService.getUidFromToken(token);
//        Page<PdfResponseDTO> pdfs = questionService.pagingPdf(pageable, page, uid);
//        if(pdfs == null){
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        PageResponseDTO pages = new PageResponseDTO(pdfs);
//
//        return ResponseEntity.ok(pages);
//    }
//
//    @PostMapping("/api/question/removePdf")
//    public ResponseEntity<?> removePdf(@RequestParam("pdf_id")Long pdfId){
//        if(pdfRepository.existsById(pdfId)){
//            pdfRepository.deleteById(pdfId);
//            return ResponseEntity.ok(HttpStatus.OK);
//        }
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//
//    @PostMapping("/api/question/questionList")
//    @ResponseBody
//    public ResponseEntity<?> getQuestionList(Pageable pageable, @RequestHeader("Authorization") String token, @RequestBody QuestionListRequestDTO questionRequestDto){
//        int page = questionRequestDto.getPage();
//        Long pdfId = questionRequestDto.getPdf_id();
//        if (page < 1)
//            page = 1;
//
//        Long uid = tokenService.getUidFromToken(token);
//        Page<QuestionResponseDTO> questions = questionService.pagingQuestion(pageable, page, uid,pdfId);
//        PageResponseDTO pages = new PageResponseDTO(questions);
//        return ResponseEntity.ok(pages);
//        }
//
//    //UID 추출
//    @GetMapping("/api/question/makeQuestion/{pdfId}")
//    public ResponseEntity<?> makeQuestion(@PathVariable("pdfId") Long pdfId, @RequestHeader("Authorization") String token) {
//        Long uid = tokenService.getUidFromToken(token);
//        questionService.makeQuestion(uid, pdfId);
//        return ResponseEntity.ok(HttpStatus.OK);
//
//    }
//
//    @GetMapping("/api/question/question/{id}")
//    public ResponseEntity<?> getQuestion(@PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
//        Long uid = tokenService.getUidFromToken(token);
//        return ResponseEntity.ok(questionRepository.findById(id).orElse(null).getResult());
//    }
//
//    @PostMapping("/api/question/addReminder")
//    public ResponseEntity<?> addReminder(@RequestParam("question_id")Long id) {
//        Question question = questionRepository.findById(id).get();
//        if (!question.isReminder()){
//            question.setReminder(true);
//            questionRepository.save(question);
//            return ResponseEntity.ok(HttpStatus.OK);
//        }
//        return ResponseEntity.accepted().build();
//    }
//
//    @PostMapping("/api/question/removeReminder")
//    public ResponseEntity<?> removeReminder(@RequestParam("question_id")Long id) {
//        Question question = questionRepository.findById(id).get();
//        if (question.isReminder()){
//            question.setReminder(false);
//            questionRepository.save(question);
//            return ResponseEntity.ok(HttpStatus.OK);
//        }
//        return ResponseEntity.accepted().build();
//    }
//
//    @PostMapping("/api/question/reminderList")
//    public ResponseEntity<?> getReminderList(Pageable pageable,@RequestHeader("Authorization") String token,@RequestParam("page") int page) {
//        Long uid = tokenService.getUidFromToken(token);
//        if (page < 1)
//            page = 1;
//
//        PageResponseDTO Page = questionService.pagingReminderPdf(pageable, page, uid);
//        return ResponseEntity.ok(Page);
//    }
//
//    @PostMapping("/api/question/reminderQuestionList")
//    @ResponseBody
//    public ResponseEntity<?> getReminderQuestionList(Pageable pageable, @RequestHeader("Authorization") String token, @RequestBody QuestionListRequestDTO questionRequestDto) {
//        int page = questionRequestDto.getPage();
//        Long pdfId = questionRequestDto.getPdf_id();
//        if (page < 1)
//            page = 1;
//
//        Long uid = tokenService.getUidFromToken(token);
//        Page<QuestionResponseDTO> questions = questionService.pagingReminderQuestion(pageable, page, uid, pdfId);
//        PageResponseDTO pages = new PageResponseDTO(questions);
//        return ResponseEntity.ok(pages);
//    }
//    @PostMapping("/api/question/updatePdfName")
//    public ResponseEntity<?> updatePdfName(@RequestBody PdfNameUpdateDTO nameUpdateDto, @RequestHeader("Authorization") String token) {
//        if(pdfRepository.existsById(nameUpdateDto.getPdfId())){
//            questionService.updatePdfName(nameUpdateDto);
//            return ResponseEntity.ok(HttpStatus.OK);
//        }
//        return ResponseEntity.accepted().build();
//    }
//}
