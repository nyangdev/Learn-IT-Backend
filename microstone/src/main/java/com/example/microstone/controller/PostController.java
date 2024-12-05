//package com.example.microstone.controller;
//
//import com.example.microstone.domain.Enum.Category;
//import com.example.microstone.domain.Enum.GroupType;
//import com.example.microstone.domain.Post;
//import com.example.microstone.domain.User;
//import com.example.microstone.dto.paging.PageResponseDTO;
//import com.example.microstone.dto.post.*;
//import com.example.microstone.repository.UserRepository;
//import com.example.microstone.service.TokenService;
//import com.example.microstone.service.post.PostService;
//import com.example.microstone.repository.PostRepository;
//import com.example.microstone.util.EnumCastingUtil;
//import lombok.extern.log4j.Log4j2;
//import net.minidev.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//@Log4j2
//@RestController
//public class PostController {
//
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private PostRepository postRepository;
//
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private EnumCastingUtil enumCastingUtil;
//
//    // 일반 게시판 포스트 리스트 요청
//    @PostMapping("/posts/paging")
//    public ResponseEntity<?> paging(Pageable pageable, @RequestBody PostPageRequestDTO postPageRequestDTO) {
//        Integer page = postPageRequestDTO.getPage();
//
//        String s_category = postPageRequestDTO.getCategory();
//        Long group_id = postPageRequestDTO.getGroup_id();
//        Category category = enumCastingUtil.castingCategory(s_category);
//        String s_group_type = postPageRequestDTO.getType();
//
//        if (page < 1)
//            page = 1;
//
//        Page<PostResponseDTO> postPage;
//
//        if(s_group_type.equalsIgnoreCase("study_group")){
//            postPage = postService.studygroupPaging(pageable, page, category,group_id);
//        }
//        else{
//            postPage = postService.publicPaging(pageable, page,category);
//        }
//
//        PageResponseDTO pages = new PageResponseDTO(postPage);
//
//        return ResponseEntity.ok(pages);
//    }
//
//    // 스터디그룹 게시판 포스트 리스트 요청
//    //1개로 합침
////    @GetMapping("/posts/paging/studygroup/{page}")
////    public ResponseEntity<?> studygroupPaging(Pageable pageable, @PathVariable("page") Integer page) {
////        if (page == null)
////            page = 1;
////
////        Page<PostResponseDTO> postPage = postService.studygroupPaging(pageable, page);
////        PageResponseDTO pages = new PageResponseDTO(postPage);
////
////        return ResponseEntity.ok(pages);
////    }
//    // 선택 게시글 조회
//    @GetMapping("/api/posts/{id}")
//    public ResponseEntity<?> getPost(@PathVariable("id") Long post_id, @RequestHeader("Authorization") String token) {
//        Long uid = tokenService.getUidFromToken(token);
//        PostResponseDTO postResponseDTO = postService.getPost(post_id,uid);
//        return ResponseEntity.ok(postResponseDTO);
//    }
//
//    // 게시글 생성
//    @PostMapping("/api/posts")
//    public ResponseEntity<?> createPost(@RequestHeader("Authorization") String token, @RequestBody PostCreateRequestDTO postCreateRequestDTO) {
//
//        if(postCreateRequestDTO.getTitle().isEmpty() || postCreateRequestDTO.getContent().isEmpty() || postCreateRequestDTO.getContent().isEmpty() || postCreateRequestDTO.getContent().isBlank()){return ResponseEntity.badRequest().build();}
//
//        Long uid = tokenService.getUidFromToken(token);
//
//        User user = userRepository.findById(uid) .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Long postId = postService.registerPost(postCreateRequestDTO, user);
//
//        Map<String, String> response = new HashMap<>();
//
//
//        if(postId != null){
//            response.put("response", "200");
//            response.put("post_id", postId.toString());
//            return ResponseEntity.ok(response);
//        }
//        response.put("response", "400");
//        return ResponseEntity.badRequest().build();
//    }
//
//    //게시글 수정
//    @RequestMapping(value = "/api/posts/update" , method = RequestMethod.POST)
//    public ResponseEntity<?> createPost(@RequestBody PostUpdateDto postUpdateDto) {
//        postService.updatePost(postUpdateDto);
//        JSONObject postResponse = new JSONObject();
//        postResponse.put("result", "success");
//        postResponse.put("post_id", postUpdateDto.getPost_id());
//
//        return ResponseEntity.ok(postResponse);
//    }
//
//    //게시글 삭제(Soft Delete) Hard Delete는 scheduler로 처리
//    @RequestMapping(value = "/api/posts/delete", method = RequestMethod.POST)
//    public ResponseEntity<?> deletePost(@RequestParam("post_id") Long post_id) {
//        postService.deletePost(post_id);
//        JSONObject postResponse = new JSONObject();
//        postResponse.put("result", "success");
//        postResponse.put("post_id", post_id);
//        postResponse.put("delete_at", postRepository.findById(post_id).get().getDeleted_at());
//        return ResponseEntity.ok(postResponse);
//    }
//
//    // 스터디그룹 내 게시판에서 검색
//    @GetMapping("/studygroup/{group_id}/search")
//    public ResponseEntity<List<Post>> searchPostsInStudyGroup(@PathVariable Long group_id, @RequestParam("keyword") String keyword,
//                                                              @RequestParam("category") Category category) {
//        List<Post> result = postService.searchPostsInStudyGroup(group_id, keyword, category);
//        return ResponseEntity.ok(result);
//    }
//
//
//    // 전체 게시판에서 검색 (스터디그룹 게시글 제외)
//    @GetMapping("/general/search")
//    public ResponseEntity<List<Post>> searchPostsInGeneral(@RequestParam("keyword") String keyword,
//                                                           @RequestParam("category") Category category) {
//        List<Post> result = postService.searchPostsInGeneral(keyword, category);
//        return ResponseEntity.ok(result);
//    }
//
//    @PostMapping("/api/posts/recommend")
//    public ResponseEntity<?> recommendPost(@RequestHeader("Authorization")String token, @RequestBody PostRecommendRequestDTO requestDTO){
//        Long uid = tokenService.getUidFromToken(token);
//        int response = postService.recommendPost(requestDTO, uid);
//        if (response == 200){
//            Map<String, Object> map = postService.getNumOfRecommend(requestDTO.getPost_id());
//            return ResponseEntity.ok(map);
//        }else if(response == 202){
//            return ResponseEntity.accepted().build();
//        }
//        return ResponseEntity.badRequest().build();
//
//    }
//
//    @GetMapping("/api/posts/getrecommend/{post_id}")
//    public ResponseEntity<?> getRecommend(@RequestHeader("Authorization")String token, @PathVariable("post_id") Long post_id){
//        Long uid = tokenService.getUidFromToken(token);
//        Map<String, Object> map = postService.getNumOfRecommend(post_id);
//        return ResponseEntity.ok(map);
//    }
//
//
//}
