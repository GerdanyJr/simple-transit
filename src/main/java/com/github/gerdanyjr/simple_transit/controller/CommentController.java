package com.github.gerdanyjr.simple_transit.controller;

import java.security.Principal;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateCommentReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.ErrorResponse;
import com.github.gerdanyjr.simple_transit.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Comentários", description = "Endpoints para gerenciamento de comentários")
@RequestMapping("/comentarios")
@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(description = "Adicionar um comentário a uma ocorrência", security = {
            @SecurityRequirement(name = "bearerAuth") }, operationId = "createComment", tags = {
                    "Comentários" }, responses = {
                            @ApiResponse(responseCode = "201", description = "Comentário criado com sucesso", headers = {
                                    @Header(name = "location", description = "Localização do comentário criado")
                            }),
                            @ApiResponse(responseCode = "403", description = "Usuário não eutenticado"),
                            @ApiResponse(responseCode = "404", description = "Usuário ou ocorrência não encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
                            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
                    })
    @PostMapping("/")
    public ResponseEntity<Void> create(@RequestBody @Valid CreateCommentReq req, Principal principal) {
        return ResponseEntity
                .created(commentService.create(req, principal))
                .build();
    }

}
