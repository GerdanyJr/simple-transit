package com.github.gerdanyjr.simple_transit.service;

import java.net.URI;
import java.security.Principal;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateCommentReq;

public interface CommentService {
    URI create(CreateCommentReq req, Principal principal);
}
