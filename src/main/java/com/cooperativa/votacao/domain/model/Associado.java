package com.cooperativa.votacao.domain.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Associado {
    
    @Id
    private String id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;
} 