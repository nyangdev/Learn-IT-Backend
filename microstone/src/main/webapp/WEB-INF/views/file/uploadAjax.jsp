<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>이미지 업로드</title>
    <style>
        #drop-area {
            border: 2px dashed #ccc;
            width: 300px;
            height: 200px;
            text-align: center;
            padding: 10px;
            margin: 10px auto;
            transition: background-color 0.3s ease-in-out;
        }
        #drop-area:hover {
            background-color: #eee;
        }
        #image-preview {
            margin: 10px auto;
            max-width: 100%;
            max-height: 200px;
            display: none;
        }
    </style>
</head>
<body>
<div class="container">
    <div id="drop-area">
        <p>이미지를 드래그 앤 드롭 하거나 클릭하여 업로드하세요.</p>
        <input type="file" id="file-input" accept="image/*" style="display: none;">
    </div>
    <img id="image-preview" src="" alt="업로드된 이미지">
</div>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"><</script>

<script>
    const dropArea = document.getElementById("drop-area");
    const fileInput = document.getElementById("file-input");
    const imagePreview = document.getElementById("image-preview");

    // 드래그 앤 드롭 이벤트 처리
    dropArea.addEventListener("dragover", (e) => {
        e.preventDefault();
        dropArea.style.backgroundColor = "#eee";
    });

    dropArea.addEventListener("dragleave", () => {
        dropArea.style.backgroundColor = "#fff";
    });

    dropArea.addEventListener("drop", (e) => {
        e.preventDefault();
        dropArea.style.backgroundColor = "#fff";
        const file = e.dataTransfer.files[0];
        if (file && file.type.startsWith("image")) {
            displayImage(file);
            upload(file);
        }
    });

    // 파일 입력 필드 변경 이벤트 처리
    fileInput.addEventListener("change", () => {
        const file = fileInput.files[0];
        if (file && file.type.startsWith("image")) {
            displayImage(file);
            upload(file);
        }
    });

    // 클릭 이벤트 처리
    dropArea.addEventListener("click", () => {
        fileInput.click();
    });

    // 이미지 표시 함수
    function displayImage(file) {
        const reader = new FileReader();
        reader.onload = () => {
            imagePreview.src = reader.result;
            imagePreview.style.display = "block";
        };
        reader.readAsDataURL(file);

    }

    function upload(file){
        let formData = new FormData(); /* Ajava 방식의 파일 업로드의 핵심 객체  */
        formData.append("file",file);
        $.ajax({
            type:"post",
            url:"http://localhost:8080/upload/uploadAjax",
            data : formData,
            dataType: "text",
            processData: false, // 파일 전송시 자동으로 쿼리 스트링 형식으로 전송되지 않도록 막는 처리
            contentType: false, /* multipart/ form-data 로 처리되는 것과 같음*/
            success: function(data, status,req) {
                console.log("data: "+ data ); // 업로드된 파일 이름
                console.log("status: "+ status ); // 성공, 실패 여부
                console.log("req: "+ req.status);// 요청 코드값
            }
        });
    }
</script>
</body>
</html>