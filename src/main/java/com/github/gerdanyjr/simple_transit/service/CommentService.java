package com.github.gerdanyjr.simple_transit.service;

import java.security.Principal;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateCommentReq;
import com.github.gerdanyjr.simple_transit.model.entity.Comment;

public interface CommentService {
    Comment create(CreateCommentReq req, Principal principal);
}
