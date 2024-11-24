package com.example.microstone.util;

import com.example.microstone.domain.Enum.Category;
import com.example.microstone.domain.Enum.PostRecommendStatus;
import com.example.microstone.domain.Enum.ReportStatus;
import com.example.microstone.domain.Enum.ReportType;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;


@Component
@Log4j2
public class EnumCastingUtil {

    public ReportType castingReportType(String type){
        ReportType reportType = null;
        if (type.equalsIgnoreCase("POLITICAL_SLANDER")) {reportType = ReportType.POLITICAL_SLANDER;}
        else if(type.equalsIgnoreCase("INAPPROPRIATE_CONTENT")){reportType = ReportType.INAPPROPRIATE_CONTENT;}
        else if(type.equalsIgnoreCase("LEAK_IMPERSONATION_FRAUD")){reportType=ReportType.LEAK_IMPERSONATION_FRAUD;}
        else if(type.equalsIgnoreCase("ABUSE_SLURS")){reportType=ReportType.ABUSE_SLURS;}
        else if(type.equalsIgnoreCase("CLICKBAIT_SPAM")){reportType=ReportType.CLICKBAIT_SPAM;}
        else if(type.equalsIgnoreCase("COMMERCIAL_ADVERTISEMENT")){reportType=ReportType.COMMERCIAL_ADVERTISEMENT;}
        else if(type.equalsIgnoreCase("OBSCENE_MATERIAL")){reportType=ReportType.OBSCENE_MATERIAL;}
        else if(type.equalsIgnoreCase("ILLEGAL_FILMING_DISTRIBUTION")){reportType=ReportType.ILLEGAL_FILMING_DISTRIBUTION;}
        else if(type.equalsIgnoreCase("OTHER")){reportType=ReportType.OTHER;}

        return reportType;
    }

    public Category castingCategory(String s_category){
        Category category = null;
        if(s_category.equalsIgnoreCase("question_board")) {category = Category.QUESTION_BOARD;}
        else if(s_category.equalsIgnoreCase("problem_share_board")){category = Category.PROBLEM_SHARE_BOARD;}
        else if(s_category.equalsIgnoreCase("free_board")){category =  Category.FREE_BOARD;}
        else if(s_category.equalsIgnoreCase("announcement_board")){category =  Category.ANNOUNCEMENT_BOARD;}
        else if(s_category.equalsIgnoreCase("all")){category =  Category.ALL;}
        return category;
    }

    public PostRecommendStatus castingRecommendStatus(String s_recommendStatus){
        PostRecommendStatus recommendStatus = null;
        if(s_recommendStatus.equalsIgnoreCase("NONE")){recommendStatus = PostRecommendStatus.NONE;}
        else if(s_recommendStatus.equalsIgnoreCase("RECOMMEND")){recommendStatus = PostRecommendStatus.RECOMMEND;}
        else if(s_recommendStatus.equalsIgnoreCase("NOT_RECOMMEND")){recommendStatus = PostRecommendStatus.NOT_RECOMMEND;}
        return recommendStatus;
    }

    public ReportStatus castingReportStatus(String s_reportStatus){
        ReportStatus reportStatus = null;
        if(s_reportStatus.equalsIgnoreCase("received")){reportStatus = ReportStatus.RECEIVED;}
        else if(s_reportStatus.equalsIgnoreCase(("in_progress"))){reportStatus = ReportStatus.IN_PROGRESS;}
        else if(s_reportStatus.equalsIgnoreCase("resolved")){reportStatus = ReportStatus.RESOLVED;}
        else if(s_reportStatus.equalsIgnoreCase("dismissed")){reportStatus = ReportStatus.DISMISSED;}
        return reportStatus;
    }
}
