package com.example.microstone.dto.Report;

import com.example.microstone.domain.Report;
import lombok.Data;

@Data
public class ReportResponseDTO {
    private Long report_id;
    private String reporter_id;
    private String report_type;
    private String comment;
    private String status;
    private Long post_id;
    private Long reply_id;

    private String content;

    private Long post_reply_id;

    public ReportResponseDTO(Report report) {
        this.report_id = report.getReport_id();
        this.reporter_id = report.getReporter_id().getUser_id();
        this.report_type = report.getReport_type().toString();
        this.comment = report.getComment();
        this.status = report.getStatus().toString();
        if(report.getPost_id() != null) {
            this.post_id = report.getPost_id().getPost_id();
            this.content = report.getPost_id().getTitle();
        }
        if(report.getReply_id() != null) {
            this.post_reply_id = report.getReply_id().getPost_id().getPost_id();
            this.reply_id = report.getReply_id().getReply_id();
            this.content = report.getReply_id().getContent();
        }
    }


}
