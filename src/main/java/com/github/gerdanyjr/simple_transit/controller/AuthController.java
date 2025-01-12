package com.github.gerdanyjr.simple_transit.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.gerdanyjr.simple_transit.model.dto.req.LoginReq;
import com.github.gerdanyjr.simple_transit.model.dto.req.RefreshTokenReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.ErrorResponse;
import com.github.gerdanyjr.simple_transit.model.dto.res.LoginRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.TokenRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.ValidationErrorResponse;
import com.github.gerdanyjr.simple_transit.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Fazer login no sistema", operationId = "login", tags = { "Autenticação" }, responses = {
            @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenRes.class))
            }),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ValidationErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Usuário ou senha inválido", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping("/login")
    public ResponseEntity<LoginRes> login(@RequestBody @Valid LoginReq req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @Operation(summary = "Obter um novo par de tokens a partir de um refresh token", operationId = "refreshToken", tags = {
            "Autenticação" }, responses = {
                    @ApiResponse(responseCode = "200", description = "Tokens atualizados com sucesso", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenRes.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou token expirado", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRes> refreshToken(@RequestBody @Valid RefreshTokenReq req) {
        return ResponseEntity.ok(authService.refreshToken(req.refreshToken()));
    }

}
