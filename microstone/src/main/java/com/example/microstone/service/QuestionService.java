package com.example.microstone.service;

import com.example.microstone.domain.PreprocessingPdf;
import com.example.microstone.domain.Question;
import com.example.microstone.domain.User;
import com.example.microstone.dto.question.PdfNameUpdateDTO;
import com.example.microstone.dto.question.QuestionContentDTO;
import com.example.microstone.domain.Result;
import com.example.microstone.dto.paging.PageResponseDTO;
import com.example.microstone.dto.question.PdfResponseDTO;
import com.example.microstone.dto.question.QuestionResponseDTO;
import com.example.microstone.repository.PreprocessingPdfRepository;
import com.example.microstone.repository.QuestionRepository;
import com.example.microstone.repository.ResultRepository;
import com.example.microstone.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PreprocessingPdfRepository pdfRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ResultRepository resultRepository;

    @Transactional
    public com.example.microstone.domain.Question makeQuestion(Long uid, Long pdfId) {
        // API 요청을 위한 webclient 연결
        WebClient webClient = WebClient.create("http://114.201.56.70:5000");

        // PDF에서 UUID 가져오기
        String uuid = pdfRepository.findById(pdfId)
                .orElseThrow(() -> new RuntimeException("PDF not found"))
                .getTaskId();

        Map<String,Object> params = new HashMap<>();
        params.put("uuid",uuid);

        // API로 POST 요청
        QuestionContentDTO responseDTO = webClient.post()
                .uri("/create-quiz-questions")
                .contentType(MediaType.APPLICATION_JSON) // Content-Type 설정
                .bodyValue(params) // 문자열 UUID를 JSON으로 보내기
                .retrieve()
                .bodyToMono(QuestionContentDTO.class) // QuestionDTO로 응답 받기
                .doOnNext(response -> {
                    System.out.println("Response: " + response);
                })
                .doOnError(error -> {
                    System.err.println("Error: " + error.getMessage());
                })
                .block(); // 결과를 동기적으로 기다림

        // DTO를 Entity로 변환하는 로직 (매핑)
        Question entity = mapDtoToEntity(responseDTO);

        // 사용자 설정
        entity.setUser(userRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("User not found")));

        //pdf연결
        entity.setPdfId(pdfRepository.findById(pdfId).orElseThrow(()->new RuntimeException("pdf not found")));

        addNumberOfQuestions(pdfId);

        // DB에 저장
        return questionRepository.save(entity); // 저장 후 Entity 반환
    }

    @Transactional
    public void addNumberOfQuestions(Long pdfId) {
        PreprocessingPdf pdf = pdfRepository.findById(pdfId).get();
        pdf.setNumberOfQuestions(pdf.getNumberOfQuestions() + 1);
        pdfRepository.save(pdf);
    }

    @Transactional
    public void substractNumberOfQuestions(Long pdfId) {
        PreprocessingPdf pdf = pdfRepository.findById(pdfId).get();
        pdf.setNumberOfQuestions(pdf.getNumberOfQuestions() - 1);
        pdfRepository.save(pdf);
    }


    @Transactional
    public Page<PdfResponseDTO> pagingPdf(Pageable pageable, int page, Long uid){
        int pageLimit = 10; // 페이지당 최대 포스트 수

        if (page < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal to 1");
        }

        User user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("Fetching PDFs for UID: {}", uid);
        Page<PreprocessingPdf> pdfPage = pdfRepository.findAllpdfs(PageRequest.of(page - 1, pageLimit), user);

        if (pdfPage.isEmpty()) {
            log.warn("No PDFs found for UID: {}", uid);
            return null;
        } else {
            log.info("Found {} PDFs for UID: {}", pdfPage.getTotalElements(), uid);
        }

        Page<PdfResponseDTO> pdfResponseDtos = pdfPage.map(pdf -> new PdfResponseDTO(pdf));

        return pdfResponseDtos;
    }

    @Transactional
    public Page<QuestionResponseDTO> pagingQuestion(Pageable pageable, int page, Long uid, Long pdfId){
        int pageLimit = 10; // 페이지당 최대 포스트 수

        if (page < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal to 1");
        }

        PreprocessingPdf pdf = pdfRepository.findById(pdfId).orElseThrow(()->new IllegalArgumentException("pdf not found"));

        User user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("Fetching Questions for UID: {}", uid);
        Page<com.example.microstone.domain.Question> questionPage = questionRepository.findAllQuestions(PageRequest.of(page - 1, pageLimit), user,pdf);

        if (questionPage.isEmpty()) {
            log.warn("No Questions found for UID: {}", uid);
        } else {
            log.info("Found {} Questions for UID: {}", questionPage.getTotalElements(), uid);
        }

        Page<QuestionResponseDTO> questionPageResponseDTOS = questionPage.map(question -> new QuestionResponseDTO(question));

        return questionPageResponseDTOS;
    }

    @Transactional
    public com.example.microstone.domain.Question processQuestionGeneration(QuestionContentDTO dto) {

        // DTO를 Entity로 변환하는 로직 (매핑)
        com.example.microstone.domain.Question entity = mapDtoToEntity(dto);

        // DB에 저장
        questionRepository.save(entity);
        // 저장 후 DTO 반환
        return entity;
    }

    // DTO -> Entity 변환
    private com.example.microstone.domain.Question mapDtoToEntity(QuestionContentDTO dto) {
        com.example.microstone.domain.Question entity = new com.example.microstone.domain.Question();
        Result result = new Result();


        result.setCorrect_answer(dto.getResult().getCorrect_answer());
        result.setExplanation(dto.getResult().getExplanation());
        result.setOptions(dto.getResult().getOptions());
        result.setQuestion(dto.getResult().getQuestion());


        entity.setMessage(dto.getMessage());
        entity.setState(dto.getState());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setDeletedAt(LocalDateTime.now().plusDays(30));
        entity.setReminder(false);
        if (result.getCorrect_answer() == null || result.getQuestion() == null) {
            throw new IllegalArgumentException("Result의 필드가 null입니다.");
        }else{
            entity.setResult(result);
        }
        entity.setTaskId(dto.getTaskId());

        return entity;
    }

    @Transactional
    public Page<QuestionResponseDTO> pagingReminderQuestion(Pageable pageable, int page, Long uid, Long pdfId){
        int pageLimit = 10; // 페이지당 최대 포스트 수

        if (page < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal to 1");
        }

        PreprocessingPdf pdf = pdfRepository.findById(pdfId).orElseThrow(()->new IllegalArgumentException("pdf not found"));

        User user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("Fetching Questions for UID: {}", uid);
        Page<com.example.microstone.domain.Question> questionPage = questionRepository.findAllReminderQuestions(PageRequest.of(page - 1, pageLimit), user,pdf);

        if (questionPage.isEmpty()) {
            log.warn("No Questions found for UID: {}", uid);
        } else {
            log.info("Found {} Questions for UID: {}", questionPage.getTotalElements(), uid);
        }

        Page<QuestionResponseDTO> questionPageResponseDTOS = questionPage.map(question -> new QuestionResponseDTO(question));

        return questionPageResponseDTOS;
    }

    @Transactional
    public PageResponseDTO pagingReminderPdf(Pageable pageable, int page, Long uid){
        int pageLimit = 10; // 페이지당 최대 포스트 수

        if (page < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal to 1");
        }

        User user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("Fetching PDFs for UID: {}", uid);
        Page<PreprocessingPdf> pdfPage = pdfRepository.findAllpdfs(PageRequest.of(page - 1, pageLimit), user);

        if (pdfPage.isEmpty()) {
            log.warn("No PDFs found for UID: {}", uid);
            return null;
        } else {
            log.info("Found {} PDFs for UID: {}", pdfPage.getTotalElements(), uid);
        }

        Page<PdfResponseDTO> pdfResponseDtos = pdfPage.map(pdf -> new PdfResponseDTO(pdf));
        PageResponseDTO pages = new PageResponseDTO(pdfResponseDtos);
        List<Map<String,Object>> content = new ArrayList<>();
        for(int i = 0; i < pages.getCurrentElements();i++) {
            Map<String,Object> map = new HashMap<>();
            PdfResponseDTO pdf = (PdfResponseDTO)pages.getContent().get(i);
            pdf.setNumberOfQuestion(questionRepository.countReminderQuestions(userRepository.findById(uid).get(),pdfRepository.findById(pdf.getId()).get()));
            map.put("id",pdf.getId());
            map.put("status",pdf.getStatus());
            map.put("fileName",pdf.getFileName());
            map.put("taskId",pdf.getTaskId());
            map.put("numberOfQuestion",questionRepository.countReminderQuestions(userRepository.findById(uid).get(),pdfRepository.findById(pdf.getId()).get()));
            content.add(map);
        }
        pages.setContent(content);
        return pages;
    }

    @Transactional
    public void updatePdfName(PdfNameUpdateDTO nameUpdateDto){
        PreprocessingPdf pdf = pdfRepository.findById(nameUpdateDto.getPdfId()).get();
        pdf.setFileName(nameUpdateDto.getPdfName());
        pdfRepository.save(pdf);
    }
}
