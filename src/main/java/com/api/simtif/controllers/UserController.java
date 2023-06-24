package com.api.simtif.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.api.simtif.models.Administrador;
import com.api.simtif.models.Aluno;
import com.api.simtif.repositories.AdministradorRepository;
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
    AdministradorRepository administradorRepository;

    // Método de busca por matrícula
    @GetMapping(value="/user/{matricula}")
    public ResponseEntity<Object> getByMatricula(@PathVariable String matricula) {
        // Busca na entidade Aluno
        Aluno aluno = alunoRepository.findByMatricula(matricula);
        if (aluno != null) {
            // Aluno encontrado
            return ResponseEntity.status(HttpStatus.OK).body(aluno.getId());
        }
        
        // Busca na entidade Admin
        Administrador admin = administradorRepository.findByMatricula(matricula);
        if (admin != null) {
            // Admin encontrado
            return ResponseEntity.status(HttpStatus.OK).body("Admin encontrado");
        }
        
        // Nenhum usuário encontrado com a matrícula fornecida
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
    }
    

}
