package com.github.gerdanyjr.simple_transit.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.gerdanyjr.simple_transit.model.dto.req.CreateReportReq;
import com.github.gerdanyjr.simple_transit.model.dto.res.CommentRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.ErrorResponse;
import com.github.gerdanyjr.simple_transit.model.dto.res.PageRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.ReportRes;
import com.github.gerdanyjr.simple_transit.model.dto.res.ValidationErrorResponse;
import com.github.gerdanyjr.simple_transit.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Ocorrências", description = "Endpoints para gerenciamento de ocorrências")
@RequestMapping("/ocorrencias")
@RestController
public class ReportController {
        private final ReportService reportService;

        public ReportController(ReportService reportService) {
                this.reportService = reportService;
        }

        @Operation(summary = "Registrar uma nova ocorrência", operationId = "createReport", security = {
                        @SecurityRequirement(name = "bearerAuth")
        }, tags = {
                        "Ocorrências" }, responses = {
                                        @ApiResponse(responseCode = "201", description = "Ocorrência criada com sucesso", headers = {
                                                        @Header(name = "location", description = "Localização da ocorrência criada")
                                        }),
                                        @ApiResponse(responseCode = "400", description = "Erro de validação", content = {
                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ValidationErrorResponse.class))
                                        }),
                                        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = {
                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                                        })
                        })
        @PostMapping("/")
        public ResponseEntity<Void> create(
                        @RequestBody @Valid CreateReportReq req,
                        Principal principal) {
                return ResponseEntity
                                .created(reportService.create(req, principal))
                                .build();
        }

        @Operation(summary = "Buscar ocorrência por id", operationId = "findById", tags = {
                        "Ocorrências" }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Ocorrência retornada com sucesso", content = {
                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportRes.class))
                                        }),
                                        @ApiResponse(responseCode = "404", description = "Ocorrência não encontrada", content = {
                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                                        }),
                                        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = {
                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                                        })
                        })
        @GetMapping("/{reportId}")
        public ResponseEntity<ReportRes> findById(@PathVariable("reportId") Integer reportId) {
                return ResponseEntity.ok(reportService.findById(reportId));
        }

        @Operation(summary = "Listar todas as ocorrências com filtros opcionais", operationId = "findAll", tags = {
                        "Ocorrências" }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Ocorrências listadas com sucesso", content = {
                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PageRes.class))
                                        }),
                                        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = {
                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                                        })
                        })
        @GetMapping
        public ResponseEntity<PageRes<ReportRes>> findAll(
                        @RequestParam(value = "reportTypeId", required = false) Integer reportTypeId,
                        @RequestParam(value = "startsAt", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime startsAt,
                        @RequestParam(value = "endsAt", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime endsAt,
                        @RequestParam(value = "address", required = false) String address,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                        @RequestParam(value = "sortDirection", defaultValue = "ASC", required = false) String sortDirection,
                        @RequestParam(value = "sortBy", defaultValue = "timestamp", required = false) String sortBy) {
                return ResponseEntity.ok(reportService
                                .findAll(reportTypeId,
                                                startsAt,
                                                endsAt,
                                                address,
                                                keyword,
                                                page,
                                                sortDirection,
                                                sortBy));
        }

        @Operation(summary = "Listar comentários de uma ocorrência", operationId = "findCommentsByReportId", tags = {
                        "Ocorrências" }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Comentários listados com sucesso", content = {
                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PageRes.class))
                                        }),
                                        @ApiResponse(responseCode = "404", description = "Ocorrência não encontrada", content = {
                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                                        }),
                                        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = {
                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                                        })
                        })
        @GetMapping("{reportId}/comentarios")
        public ResponseEntity<PageRes<CommentRes>> findCommentsByReportId(
                        @PathVariable("reportId") Integer reportId,
                        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
                return ResponseEntity.ok(reportService.findCommentsByReport(reportId, page));
        }

        @Operation(summary = "Excluir uma ocorrência por id", operationId = "deleteReport", security = {
                        @SecurityRequirement(name = "bearerAuth") }, tags = {
                                        "Ocorrências" }, responses = {
                                                        @ApiResponse(responseCode = "204", description = "Ocorrência excluída com sucesso"),
                                                        @ApiResponse(responseCode = "403", description = "Usuário não autorizado a excluir a ocorrência"),
                                                        @ApiResponse(responseCode = "404", description = "Ocorrência não encontrada", content = {
                                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                                                        }),
                                                        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = {
                                                                        @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                                                        })
                                        })
        @DeleteMapping("/{reportId}")
        public ResponseEntity<Void> delete(
                        @PathVariable("reportId") Integer reportId,
                        Principal principal) {

                reportService.delete(reportId, principal);

                return ResponseEntity
                                .noContent()
                                .build();
        }
}
