package com.github.gerdanyjr.simple_transit.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.gerdanyjr.simple_transit.model.dto.req.RegisterUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.UpdateUserReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.ErrorResponse;
import com.github.gerdanyjr.simple_transit.model.dto.res.UserRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.ValidationErrorResponse;
import com.github.gerdanyjr.simple_transit.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
@RequestMapping("/usuarios")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Cadatrar um novo Usuário", operationId = "registerUser", tags = {
            "Usuários" }, responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", headers = {
                            @Header(name = "location", description = "Localização do usuário criado")
                    }),
                    @ApiResponse(responseCode = "400", description = "Erro de validação", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ValidationErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Usuário já cadastrado", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    @PostMapping("/")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserReq req) {
        return ResponseEntity
                .created(userService.register(req))
                .build();
    }

    @Operation(summary = "Busca um usuário por id", operationId = "findUserById", security = {}, tags = {
            "Usuários" }, responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserRes.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = {
                            @Content(mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    @GetMapping("/{userId}")
    public ResponseEntity<UserRes> findById(@PathVariable("userId") Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "Atualizar os dados do usuário", operationId = "updateUser", tags = {
            "Usuários" }, security = { @SecurityRequirement(name = "bearerAuth") }, responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso", headers = {
                            @Header(name = "location", description = "Localização do usuário atualizado") }),
                    @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para executar esta operação"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                    })
            })

    @PatchMapping
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UpdateUserReq req, Authentication authentication) {
        return ResponseEntity
                .noContent()
                .location(userService.updateUser(req, authentication))
                .build();
    }
}
