package com.api.simtif.controllers;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.api.simtif.models.Servidor;
import com.api.simtif.models.Aluno;
import com.api.simtif.repositories.ServidorRepository;
import com.api.simtif.repositories.AlunoRepository;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {
    
    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    ServidorRepository servidorRepository;

    @GetMapping(value="/user/dados/{token}/")
    public ResponseEntity<Object> getDataByToken(@PathVariable String token) {
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        String tokenPayload = payload;

        Long id = prepareId(tokenPayload);

        Optional<Aluno> alunoOptional = alunoRepository.findById(id);
        if(alunoOptional.isPresent()){
            Aluno aluno = alunoOptional.get();
            
            Map<String, Object> alunoInfo = new HashMap<>();
            alunoInfo.put("id", aluno.getId());
            alunoInfo.put("tipoVinculo", aluno.getTipoVinculo());
            alunoInfo.put("nomeCompleto", aluno.getNomeCompleto());
            alunoInfo.put("curso", aluno.getCurso());

            return ResponseEntity.status(HttpStatus.OK).body(alunoInfo);
        }

        Optional<Servidor> servidorOptional = servidorRepository.findById(id);
        if(servidorOptional.isPresent()){
            Servidor servidor = servidorOptional.get();

            Map<String, Object> servidorInfo = new HashMap<>();
            servidorInfo.put("id", servidor.getId());
            servidorInfo.put("tipoVinculo", servidor.getTipoVinculo());

            return ResponseEntity.status(HttpStatus.OK).body(servidorInfo);
        }
        
        Map<String, Object> userId = new HashMap<>();
        userId.put("id", id);
        return ResponseEntity.status(HttpStatus.OK).body(userId);

    }

    private Long prepareId(String tokenPayload){
        // Pegando substring iniciado com user_id
        int indexUserId = tokenPayload.indexOf("user_id");
        String getUserId = tokenPayload.substring(indexUserId);

        // Pegando apenas o numero do id
        int indexNumbersUserId = getUserId .indexOf(":");
        String getNumberUserId = getUserId .substring(indexNumbersUserId);

        // Removendo caracteres indesejados
        String removePontos = getNumberUserId.replace(":", "");
        String removeChave = removePontos.replace("}", "");
        Long idNumber = Long.valueOf(removeChave);

        return idNumber;
    }

}
