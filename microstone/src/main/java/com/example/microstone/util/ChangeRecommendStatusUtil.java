package com.example.microstone.util;

import com.example.microstone.domain.Enum.PostRecommendStatus;
import com.example.microstone.domain.Post;
import com.example.microstone.domain.PostRecommend;
import com.example.microstone.dto.post.PostRecommendRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class ChangeRecommendStatusUtil {
    private final EnumCastingUtil enumCastingUtil;

    public ChangeRecommendStatusUtil(EnumCastingUtil enumCastingUtil) {
        this.enumCastingUtil = enumCastingUtil;
    }

    //status = 요청값, recommend = 저장값
    public Post changeRecommendStatus(Post post, PostRecommendRequestDTO requestDTO, PostRecommend recommend) {
        PostRecommendStatus status = enumCastingUtil.castingRecommendStatus(requestDTO.getRecommendation());
        if(status == PostRecommendStatus.RECOMMEND){
            if(recommend.getStatus() == PostRecommendStatus.NOT_RECOMMEND){
                post.setRecommend_num(post.getRecommend_num() + 1);
                post.setNot_recommend_num(post.getNot_recommend_num() - 1);
            }else if(recommend.getStatus() == PostRecommendStatus.NONE)
            {
                post.setRecommend_num(post.getRecommend_num() + 1);
            }
        }
        else if(status == PostRecommendStatus.NOT_RECOMMEND){
            if(recommend.getStatus() == PostRecommendStatus.RECOMMEND){
                post.setRecommend_num(post.getRecommend_num() - 1);
                post.setNot_recommend_num(post.getNot_recommend_num() + 1);
            }else if(recommend.getStatus() == PostRecommendStatus.NONE)
            {
                post.setNot_recommend_num(post.getNot_recommend_num() + 1);
            }
        }
        else if(status == PostRecommendStatus.NONE){
            if(recommend.getStatus() == PostRecommendStatus.RECOMMEND){
                post.setRecommend_num(post.getRecommend_num() - 1);
            }else if(recommend.getStatus() == PostRecommendStatus.NOT_RECOMMEND)
            {
                post.setNot_recommend_num(post.getNot_recommend_num() - 1);
            }
        }
        return post;
    }
}
