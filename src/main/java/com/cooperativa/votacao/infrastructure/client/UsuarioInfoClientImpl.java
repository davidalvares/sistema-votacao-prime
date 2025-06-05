package com.cooperativa.votacao.infrastructure.client;

import com.cooperativa.votacao.domain.exception.CpfInvalidoException;
import com.cooperativa.votacao.domain.model.StatusUsuario;
import com.cooperativa.votacao.domain.ports.UserInfoPort;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioInfoClientImpl implements UserInfoPort {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public UsuarioInfoClientImpl(@Value("${user.info.base-url}") String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public StatusUsuario verificarCPF(String cpf) {
        try {
            String url = baseUrl + "/users/" + cpf;
            UsuarioInfoResponse response = restTemplate.getForObject(url, UsuarioInfoResponse.class);
            return response != null ? StatusUsuario.valueOf(response.getStatus()) : StatusUsuario.UNABLE_TO_VOTE;
        } catch (HttpClientErrorException.NotFound e) {
            throw new CpfInvalidoException("CPF inválido");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar CPF", e);
        }
    }

    public static class UsuarioInfoResponse {
        @JsonProperty("status")
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
} 