//package com.example.microstone.controller;
//
//import com.example.microstone.dto.Report.*;
//import com.example.microstone.dto.paging.PageResponseDTO;
//import com.example.microstone.repository.ReplyRepository;
//import com.example.microstone.service.ReportService;
//import com.example.microstone.util.EnumCastingUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.token.TokenService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/reports")
//@RequiredArgsConstructor
//public class ReportController {
//    @Autowired
//    private final ReportService reportService;
//
//    @Autowired
//    private TokenService tokenService;
//    @Autowired
//    private EnumCastingUtil enumCastingUtil;
//    @Autowired
//    private ReplyRepository replyRepository;
//
//    // post 신고하기
//    @PostMapping("/posts")//@RequestParam(name = "reporter_id") Long reporter_id,
//    public ResponseEntity<ReportPostResponseDTO> createPostReport(@RequestHeader("Authorization") String token,
//                                                                  @RequestBody ReportPostRequestDTO reportPostRequestDTO) {
//
//        Long reporter_id = tokenService.getUidFromToken(token);
//        ReportPostResponseDTO responseDTO = reportService.createPostReport(reporter_id, reportPostRequestDTO);
//
//        return ResponseEntity.ok(responseDTO);
//    }
//
//    // 신고_댓글_매핑
//    @PostMapping("/replies")//@RequestParam(name = "reporter_id") Long reporter_id,
//    public ResponseEntity<ReportReplyResponseDTO> createReplyReport(@RequestHeader("Authorization") String token,
//                                                                    @RequestBody ReportReplyRequestDTO reportReplyRequestDTO) {
//        Long reporter_id = tokenService.getUidFromToken(token);
//
//        ReportReplyResponseDTO responseDTO = reportService.createReplyReport(reporter_id, reportReplyRequestDTO);
//
//        return ResponseEntity.ok(responseDTO);
//    }
//
//    // 신고 상태 변경
//    @PutMapping("/status/{report_id}")
//    public ResponseEntity<Map<String, String>> updateReportStatus(@RequestHeader("Authorization")String token,
//                                                                  @PathVariable("report_id") Long report_id)
////                                                                  @RequestParam(name = "newStatus") ReportStatus newStatus,
////                                                                  @RequestParam(name = "admin_id") Long admin_id
//    {
//
//        Long admin_id = tokenService.getUidFromToken(token);
//
//        // 정상 실행시
//        try {
////            reportService.updateReportStatus(report_id, newStatus, admin_id);
//            reportService.updateReportStatus(report_id, admin_id);
//
//            Map<String, String> response = new HashMap<>();
//            response.put("result", "success");
//
//            return new ResponseEntity<>(response, HttpStatus.OK);
//
//        } catch (IllegalArgumentException e) {
//
//            Map<String, String> response = new HashMap<>();
//            response.put("result", "error");
//
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            Map<String, String> response = new HashMap<>();
//            response.put("result", "error");
//
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PostMapping("/paging")
//    public ResponseEntity<?> pagingReportList(@RequestBody ReportPagingRequestDTO pagingRequestDTO){
//        PageResponseDTO pages = reportService.reportPaging(pagingRequestDTO);
//        return ResponseEntity.ok(pages);
//    }
//}