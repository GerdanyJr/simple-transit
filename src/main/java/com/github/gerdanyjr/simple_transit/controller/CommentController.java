package com.github.gerdanyjr.simple_transit.controller;

import java.net.URI;
import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateCommentReq;
import com.github.gerdanyjr.simple_transit.model.entity.Comment;
import com.github.gerdanyjr.simple_transit.service.CommentService;

import jakarta.validation.Valid;

@RequestMapping("/comentarios")
@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CreateCommentReq req, Principal principal) {
        Comment comment = commentService.create(req, principal);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(comment.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

}
