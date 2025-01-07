package com.github.gerdanyjr.simple_transit.model.entity;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ocorrencia")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "resumo", length = 100, nullable = false)
    private String summary;

    @Column(name = "descricao", length = 255, nullable = false)
    private String description;

    @Column(name = "data_hora", nullable = false)
    private Instant timestamp;

    @Column(name = "endereco", length = 100, nullable = false)
    private String address;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "report")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "tipo_ocorrencia_id", nullable = false)
    private ReportType reportType;
}
