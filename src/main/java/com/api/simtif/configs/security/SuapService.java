package com.api.simtif.configs.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class SuapService {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public SuapService(@Value("${suap.api.base-url}") String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public String obterTokenAcesso(String matricula, String senha) {
        String url = baseUrl + "/token/";

        // Construa o objeto de requisição com os parâmetros necessários
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", matricula);
        requestBody.put("password", senha);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // Faça a chamada para obter o token de acesso
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Verifique se a resposta foi bem-sucedida
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // Retorne o token de acesso obtido
            return responseEntity.getBody();
        } else {
            // Trate o erro de autenticação
            throw new RuntimeException("Erro ao obter token de acesso do SUAP");
        }
    }
}