package com.example.microstone.util;

import net.minidev.json.JSONObject;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadFileUtils {
    private static final Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);


    public static JSONObject uploadFile(String uploadPath, String originalName, byte[] fileData) throws Exception {
        String finalUploadPath;
        Map<String, String> fileInfo = new HashMap<String, String>();

        //UUID 발급
        UUID uid = UUID.randomUUID();
        String savedName = uid.toString() + "_" + originalName;

        //업로드 할 디렉토리 생성
        //String savedPath = calcPath(uploadPath);
        //일자 디렉토리 사용 안함
        String savedPath = uploadPath;

        //이미지 여부 확인 후 생성
        //File target = new File(uploadPath+savedPath,savedName);

        //임시 디렉토리에 업로드된 파일을 지정된 디렉토리로 복사
        //FileCopyUtils.copy(fileData, target);

        //파일 확장자 검사(a.jpg, aaa.bbb.ccc.jpg)
        String formatName = originalName.substring(originalName.lastIndexOf('.')+1);
        String uploadFileName = null;

        //이미지 파일인경우: 썸네일 생성
        if (MediaUtils.getMediaType(formatName) != null){
            //썸네일 생성
            makeTypeDir(uploadPath+savedPath);
            File target = new File(uploadPath+savedPath+"\\Image",savedName);
            FileCopyUtils.copy(fileData, target);

            finalUploadPath = target.getAbsolutePath();

            //썸네일 생성
            Map<String, String> ThumnailInfo = makeThumbnail(uploadPath, savedPath+"\\Image", savedName);

            //DB 생성용 데이터 저장
            fileInfo.put("uploadFileName", savedName);
            fileInfo.put("finalUploadPath", finalUploadPath);
            fileInfo.put("thumbnailName", ThumnailInfo.get("thumbnailName"));
            fileInfo.put("thumbnailPath", ThumnailInfo.get("thumbnailPath"));
            fileInfo.put("fileType","Image");
            fileInfo.put("response","200");


        }else{
            Map<String, String> response = new HashMap<String, String>();
            response.put("response","415");
            JSONObject responseObj = new JSONObject(response);
            return responseObj;
        }

        //DB 저장용 데이터 리턴용

        JSONObject jsonObject = new JSONObject(fileInfo);


        return jsonObject;
    }

    public static void makeTypeDir(String uploadPath){
            File dirPath = new File( uploadPath+"\\Image");
            if(!dirPath.exists()){dirPath.mkdir();}
    }

    //이미지 파일이 아닌 파일처리 메서드
    public static String noimg(String uploadPath, String path, String filename) throws Exception {
        //File.separator : 이름 구분자
        //윈도우의 경우 upload\\text.txt, 리눅스는 upload/text.txt로 쓰는데 '\', '/'를 OS에 맞춰 자동으로 처리한다
        String iconName = uploadPath + path +File.separator + filename;

        return iconName.substring(uploadPath.length()).replace(File.separatorChar, '/');
    }

    public static Map<String, String> makeThumbnail(String uploadPath, String savedPath, String fileName) throws Exception{
        //원본 파일을 읽기 위한 버퍼
        BufferedImage sourceImg = ImageIO.read(new File(uploadPath+savedPath, fileName));

        //100픽셀 단위의 썸네일 생성
        BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, 100);

        //썸네일 이름
        makeThumbnailDir(uploadPath + savedPath);
        String thumbnailName = uploadPath + savedPath + "\\Thumbnail" + File.separator + "s_" + fileName;
        File newFile = new File(thumbnailName);
        String formatName = fileName.substring(fileName.lastIndexOf('.')+1);

        //썸네일 생성
        ImageIO.write(destImg, formatName.toUpperCase(), newFile);

        Map<String, String> ThumnailInfo = new HashMap<>();
        ThumnailInfo.put("thumbnailName", "s_" + fileName);
        ThumnailInfo.put("thumbnailPath", uploadPath + savedPath + "\\Thumbnail" + fileName);

        //썸네일 이름 리턴
        return ThumnailInfo;
    }

    //날짜 처리(년 폴더 \ 월 폴더 \ 일 폴더 생성)
//    public void String calcPath(String uploadPath) {
        Calendar cal = Calendar.getInstance();
//        String yearPath = File.separator + cal.get(Calendar.YEAR);
//        String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH)+1);
//        String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

//        makeDir(uploadPath, yearPath, monthPath, datePath);
//        logger.info(datePath);

//        return datePath;

//    }

    //디렉토리 생성
    private static void makeDir(String uploadPath, String... paths) {
        //String... 은 가변 사이즈 매개변수(배열의 요소가 몇 개든 상관 없이 처리

        //디렉토리 존재하면 skip
        if(new File(paths[paths.length-1]).exists()){return;}
        for (String path : paths) {
            File dirPath = new File(uploadPath + path);
            if(!dirPath.exists()){dirPath.mkdir();}
        }

    }

    private static  void makeThumbnailDir(String uploadPath){
        File dirPath = new File( uploadPath+"\\Thumbnail");
        if(!dirPath.exists()){dirPath.mkdir();}

    }
}