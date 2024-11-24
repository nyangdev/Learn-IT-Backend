package com.example.microstone.service.post;

import com.example.microstone.domain.*;
import com.example.microstone.domain.Enum.Category;
import com.example.microstone.domain.Enum.GroupType;
import com.example.microstone.domain.Enum.PostRecommendStatus;
import com.example.microstone.domain.Id.PostRecommendId;
import com.example.microstone.dto.post.*;
import com.example.microstone.repository.*;
import com.example.microstone.util.ChangeRecommendStatusUtil;
import com.example.microstone.util.EnumCastingUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService{

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudyGroupRepository studyGroupRepository;
    @Autowired
    private EnumCastingUtil enumCastingUtil;
    @Autowired
    private PostRecommendRepository postRecommendRepository;
    @Autowired
    ChangeRecommendStatusUtil changeRecommendStatusUtil;

    @Transactional
    public Page<PostResponseDTO> publicPaging(Pageable pageable, int page,Category category) {
        //page = pageable.getPageNumber(); //현재 페이지 번호를 가져옵니다.
        int pageLimit = 10; // 페이지당 최대 포스트 수
        Page<Post> postPage = null;

        if(category == Category.ALL){
            postPage = postRepository.findAllPublicPosts(PageRequest.of(page-1, pageLimit));
        }else{
            postPage = postRepository.findCategoryAllPublicPosts(PageRequest.of(page-1, pageLimit),category);
        }

        // 각 Post를 PostResponseDto로 변환합니다.
        Page<PostResponseDTO> postResponseDtos = postPage.map(
                post -> new PostResponseDTO(post));

        return postResponseDtos;
    }

    @Transactional
    public Page<PostResponseDTO> studygroupPaging(Pageable pageable, int page,Category category,Long group_id) {
        //page = pageable.getPageNumber(); //현재 페이지 번호를 가져옵니다.
        int pageLimit = 10; // 페이지당 최대 포스트 수
        Page<Post> postPage = null;
        StudyGroup studyGroup = studyGroupRepository.findById(group_id).get();
        if(category == Category.ALL){
            postPage = postRepository.findAllStudyGroupPosts(PageRequest.of(page-1, pageLimit),studyGroup);
        }else{
            postPage = postRepository.findCategoryAllStudyGroupPosts(PageRequest.of(page-1, pageLimit),category,studyGroup);
        }

        // 각 Post를 PostResponseDto로 변환합니다.
        Page<PostResponseDTO> postResponseDtos = postPage.map(
                post -> new PostResponseDTO(post));

        return postResponseDtos;
    }

    // post 조회
//    @Transactional
//    public PostResponseDTO getPost(Long post_id) {
//        Optional<Post> post = postRepository.findById(post_id);
//
//        return post.map(PostResponseDTO::new).orElseThrow(() -> new RuntimeException("Post not found"));
//    }

    @Transactional
    public PostResponseDTO getPost(Long post_id, Long user_id) {
        Optional<Post> o_post = postRepository.findById(post_id);
        PostResponseDTO post = o_post.map(PostResponseDTO::new).orElse(null);
        post.setStatus(PostRecommendStatus.NONE);

        if(postRecommendRepository.existsPostRecommendBy(post_id,user_id)) {
            PostRecommend postRecommend = postRecommendRepository.findPostRecommend(post_id, user_id);
            post.setStatus(postRecommend.getStatus());
        }

        return post;
    }


    // post 생성
    @Transactional
    public Long registerPost(PostCreateRequestDTO postCreateRequestDTO, User user){


        Board board = boardRepository.findById(postCreateRequestDTO.getBoard_id())
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // group_id, group_type 기본값 설정
        StudyGroup studyGroup = null;
        GroupType groupType = GroupType.PUBLIC;
        Category category = null;

        // group_id가 존재할 경우 스터디그룹을 조회, group_type 설정
        if(postCreateRequestDTO.getGroup_id() != null) {
            studyGroup = studyGroupRepository.findById(postCreateRequestDTO.getGroup_id())
                    .orElseThrow(() -> new RuntimeException("StudyGroup not found"));
            groupType = GroupType.STUDY_GROUP;
        }

        category = enumCastingUtil.castingCategory(postCreateRequestDTO.getCategory());

        Post post = Post.builder()
                    .board_id(board)
                    .title(postCreateRequestDTO.getTitle())
                    .content(postCreateRequestDTO.getContent())
                    .recommend_num(0)
                    .not_recommend_num(0)
                    .views_num(0)
                    .category(category)
                    .reply_num(0)
                    .uid(user)
                    .group_id(studyGroup)
                    .group_type(groupType)
                    .build();

        postRepository.save(post);

        return post.getPost_id();
    }

    @Transactional
    public void updatePost(PostUpdateDto postUpdateDto) {
        Long post_id = postUpdateDto.getPost_id();
        Post post = postRepository.findById(post_id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.updateContents(postUpdateDto);
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.deletePost();
        postRepository.save(post);
    }

    // 스터디그룹 내 게시판에서 검색
    public List<Post> searchPostsInStudyGroup(Long group_id, String keyword, Category category) {
        if(category != null) {
            // 특정 카테고리에서 검색
            return postRepository.findByStudyGroupAndKeywordAndCategory(group_id, keyword, category);
        } else {
            // 전체 게시판에서 검색
            return postRepository.findByStudyGroupAndKeyword(group_id, keyword);
        }
    }

    public List<Post> searchPostsInGeneral(String keyword, Category category) {
        if (category != null) {
            // 특정 카테고리에서 검색
            return postRepository.findByKeywordInGeneralAndCategory(keyword, category);
        } else {
            // 전체 게시판에서 검색
            return postRepository.findByKeywordInGeneral(keyword);
        }
    }

    public int recommendPost(PostRecommendRequestDTO requestDTO,Long uid){
        Post post = postRepository.findById(requestDTO.getPost_id()).get();
        PostRecommendStatus status = enumCastingUtil.castingRecommendStatus(requestDTO.getRecommendation());
        PostRecommend recommend = postRecommendRepository.findPostRecommend(requestDTO.getPost_id(),uid);
        if(!postRecommendRepository.existsPostRecommendBy(requestDTO.getPost_id(),uid)) {
            PostRecommendId id = PostRecommendId.builder()
                    .postId(requestDTO.getPost_id())
                    .userId(uid)
                    .build();
            PostRecommend postRecommend = PostRecommend.builder()
                    .id(id)
                    .status(status)
                    .createdAt(LocalDateTime.now())
                    .build();

            postRecommendRepository.save(postRecommend);


            if (status == PostRecommendStatus.RECOMMEND) {
                post.setRecommend_num(post.getRecommend_num() + 1);
            } else if (status == PostRecommendStatus.NOT_RECOMMEND) {
                post.setNot_recommend_num(post.getNot_recommend_num() + 1);
            }
            postRepository.save(post);
            return 200;
        }

        else {
             if(recommend.getStatus() == status){
                 return 202;
             }

             else{
                 changeRecommendStatusUtil.changeRecommendStatus(post,requestDTO,recommend);
                 postRepository.save(post);
                 recommend.setStatus(status);
                 postRecommendRepository.save(recommend);
                 return 200;
             }
        }
    }

    public PostRecommendStatus getRecommend(Long user_id, Long post_id){
        PostRecommendStatus status = PostRecommendStatus.NONE;

        if(postRecommendRepository.existsPostRecommendBy(post_id,user_id)) {
            PostRecommend postRecommend = postRecommendRepository.findPostRecommend(post_id, user_id);
            status =  postRecommend.getStatus();
        }
        return status;
    }

    public Map<String, Object> getNumOfRecommend(Long post_id){
        Map<String, Object> map = new HashMap<>();
        map.put("recommend_num",postRepository.getRecommendNum(post_id));
        map.put("not_recommend_num",postRepository.getNotRecommendNum(post_id));
        return map;
    }

}
