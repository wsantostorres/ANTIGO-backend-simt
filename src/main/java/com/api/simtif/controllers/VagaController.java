package com.api.simtif.controllers;

import com.api.simtif.models.Aluno;
import com.api.simtif.models.Curso;
import com.api.simtif.models.Vaga;
import com.api.simtif.repositories.AlunoRepository;
import com.api.simtif.repositories.CursoRepository;
import com.api.simtif.repositories.VagaRepository;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
public class VagaController {
    @Autowired
    VagaRepository vagaRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    AlunoRepository alunoRepository;

    @GetMapping("/vagas/")
    public List<Vaga> getAllVagas(@RequestParam(required = false) String cursoNome){
        Sort sortByDataUltimaModificacao = Sort.by(Sort.Direction.DESC, "dataUltimaModificacao");
        List <Vaga> vagas = vagaRepository.findAll(sortByDataUltimaModificacao);;
        

        if(cursoNome != null && !cursoNome.isEmpty()){
            Curso curso = cursoRepository.findByNome(cursoNome);
            List<Vaga> vagasDestinadas = new ArrayList<>();

            for(Vaga vaga : vagas){
                if(vaga.getCursos().contains(curso)){
                    vagasDestinadas.add(vaga);
                }
            }

            return vagasDestinadas;
        }


        return vagas;
    }


    @GetMapping("/vagas/{id}/")
    public Optional<Vaga> getVagaById(@PathVariable long id){
        return vagaRepository.findById(id);
    }

    @PostMapping("/vagas/")
    public ResponseEntity<Object> saveVaga(@RequestBody Vaga vaga) {
        List<Curso> cursos = cursoRepository.findAll();
        List<Curso> vagaCursos = vaga.getCursos();

        if (cursos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ainda não há cursos.");
        }

        List<Curso> cursosRelacionados = new ArrayList<>();

        for (Curso curso : cursos) {
            for (Curso vagaCurso : vagaCursos) {
                if (vagaCurso.getId() == curso.getId()) {
                    curso.getVagas().add(vaga);
                    cursosRelacionados.add(curso);
                }
            }
        }
        LocalDateTime dataUltimaModificacao = LocalDateTime.now();
        LocalDateTime dataEncerramento = vaga.getDataEncerramento();

        vaga.setDataEncerramento(dataEncerramento);
        vaga.setDataUltimaModificacao(dataUltimaModificacao);
        vaga.setCursos(cursosRelacionados);
        return ResponseEntity.status(HttpStatus.CREATED).body(vagaRepository.save(vaga));

    }

    @PutMapping("/vagas/{id}")
    public ResponseEntity<Object> updateVaga(@PathVariable Long id, @RequestBody Vaga vagaAtualizada) {
    Optional<Vaga> optionalVaga = vagaRepository.findById(id);

    if (optionalVaga.isPresent()) {
        Vaga vaga = optionalVaga.get();
        List<Curso> cursosAntigos = vaga.getCursos();
        List<Curso> cursosParaRemover = new ArrayList<>();

        for (Curso cursoAntigo : cursosAntigos) {
            cursoAntigo.getVagas().remove(vaga);
            cursosParaRemover.add(cursoAntigo);
        }

        vaga.getCursos().removeAll(cursosParaRemover);

        List<Curso> cursosRelacionados = new ArrayList<>();

        List<Curso> cursos = cursoRepository.findAll();
        List<Curso> vagaCursos = vagaAtualizada.getCursos();

        for (Curso curso : cursos) {
            for (Curso vagaCurso : vagaCursos) {
                if (vagaCurso.getId() == curso.getId()) {
                    curso.getVagas().add(vaga);
                    cursosRelacionados.add(curso);
                }
            }
        }

        vaga.setCursos(cursosRelacionados);
        vaga.setTitulo(vagaAtualizada.getTitulo());
        vaga.setDescricao(vagaAtualizada.getDescricao());
        vaga.setTipo(vagaAtualizada.getTipo());
        vaga.setDispManha(vagaAtualizada.getDispManha());
        vaga.setDispTarde(vagaAtualizada.getDispTarde());
        vaga.setDispNoite(vagaAtualizada.getDispNoite());
        LocalDateTime dataUltimaModificacao = LocalDateTime.now();
        vaga.setDataUltimaModificacao(dataUltimaModificacao);
        LocalDateTime dataEncerramento = vaga.getDataEncerramento();
        vaga.setDataEncerramento(dataEncerramento);

        vagaRepository.save(vaga);
        return ResponseEntity.status(HttpStatus.OK).body("Vaga atualizada com sucesso.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga não encontrada.");
    }
}

    @DeleteMapping("/vagas/{id}")
    public ResponseEntity<Object> deleteVaga(@PathVariable Long id) {
        Optional<Vaga> optionalVaga = vagaRepository.findById(id);

        if (optionalVaga.isPresent()) {
            Vaga vaga = optionalVaga.get();
            List<Curso> cursosRelacionados = vaga.getCursos();

            for (Curso curso : cursosRelacionados) {
                curso.getVagas().remove(vaga);
            }

            List<Aluno> alunosRelacionados = vaga.getAlunos();
            for (Aluno aluno : alunosRelacionados) {
                aluno.getVagas().remove(vaga);
            }

            vagaRepository.delete(vaga);
            return ResponseEntity.status(HttpStatus.OK).body("Vaga excluída com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga não encontrada.");
        }
    }

    @PutMapping("/participar/{alunoId}/{vagaId}/")
    public ResponseEntity<Object> participarVaga(@PathVariable Long alunoId, @PathVariable Long vagaId) {
        
        Optional<Aluno> alunoOptional = alunoRepository.findById(alunoId);
        Optional<Vaga> vagaOptional = vagaRepository.findById(vagaId);

        if(alunoOptional.isPresent() && vagaOptional.isPresent()){
            Aluno aluno = alunoOptional.get();
            Vaga vaga = vagaOptional.get();
            String cursoAluno = aluno.getCurso();
            Curso curso = cursoRepository.findByNome(cursoAluno);

            if(!vaga.getCursos().contains(curso)){
                return ResponseEntity.status(HttpStatus.OK).body("Você não pode participar desta vaga.");
            }

            if(aluno.getTipoVinculo() == null || aluno.getTipoVinculo().isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body("Você não tem mais vinculo com a instituição.");
            }

            if(vaga.getAlunos().contains(aluno)){
                return ResponseEntity.status(HttpStatus.OK).body("Você já está participando desta vaga!");
            }

            
            aluno.getVagas().add(vaga);
            vaga.getAlunos().add(aluno);

            alunoRepository.save(aluno);
            vagaRepository.save(vaga);

            return ResponseEntity.status(HttpStatus.OK).body("Participação realizada com sucesso!");
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ocorreu um erro.");
    }


    @GetMapping(value = "buscarTitulo")
    @ResponseBody
    public ResponseEntity<List<Vaga>> buscarTitulo(@RequestParam(name = "titulo") String titulo, @RequestParam(required = false) String cursoNome){
        List<Vaga> vagas = vagaRepository.buscarTitulo(titulo.trim().toUpperCase());

        if(cursoNome != null && !cursoNome.isEmpty()){
            Curso curso = cursoRepository.findByNome(cursoNome);
            List<Vaga> vagasDestinadas = new ArrayList<>();

            for(Vaga vaga : vagas){
                if(vaga.getCursos().contains(curso)){
                    vagasDestinadas.add(vaga);
                }
            }
            
            return new ResponseEntity<List<Vaga>>(vagasDestinadas, HttpStatus.OK);
        }

        return new ResponseEntity<List<Vaga>>(vagas, HttpStatus.OK);
    }

    @PostMapping("/baixar-curriculos/{id}/")
    public ResponseEntity<Object> baixarCurriculos(@PathVariable long id) {
        Optional<Vaga> vagaOptional = vagaRepository.findById(id);

        if (vagaOptional.isPresent()) {
            Vaga vaga = vagaOptional.get();
            List<Aluno> alunosByVaga = vaga.getAlunos();

            if (alunosByVaga.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ainda não há alunos participando desta vaga.");
            }

            // Gerar os HTMLs dos currículos
            byte[] zipBytes = generateCurriculosZip(alunosByVaga);

            String nomeArquivo = vaga.getTitulo();

            // Configurar a resposta HTTP com o arquivo ZIP contendo os HTMLs
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/zip"));
            headers.setContentDispositionFormData("attachment", nomeArquivo + ".zip");
            headers.setContentLength(zipBytes.length);

            return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga não encontrada.");
    }

    private byte[] generateCurriculosZip(List<Aluno> alunos) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {

            for (Aluno aluno : alunos) {
                String curriculoHtml = generateCurriculoHtml(aluno);

                // Adicionar o arquivo HTML do currículo ao arquivo ZIP
                ZipEntry entry = new ZipEntry(aluno.getNomeCompleto() + aluno.getMatricula() + ".html");
                zipOutputStream.putNextEntry(entry);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(curriculoHtml.getBytes(StandardCharsets.UTF_8));
                IOUtils.copy(inputStream, zipOutputStream);
                inputStream.close();
                zipOutputStream.closeEntry();
            }

            zipOutputStream.finish();
            zipOutputStream.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            // Lidar com erros de IO
            e.printStackTrace();
            return null;
        }
    }

    private String generateCurriculoHtml(Aluno aluno) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Adicionar o cabeçalho HTML
        htmlBuilder.append("<!DOCTYPE html >\r\n" + //
                "<html lang=\"pt-br\">\r\n" + //
                "<head>\r\n" + //
                "    <meta charset=\"UTF-8\">\r\n" + //
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n" + //
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + //
                "    <title>Curr\u00EDculo Digital</title>\r\n" + //
                "    <style>\r\n" + //
                "        *{\r\n" + //
                "            margin: 0;\r\n" + //
                "            padding: 0;\r\n" + //
                "            box-sizing: border-box;\r\n" + //
                "            font-family: Georgia, 'Times New Roman', Times, serif;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        html, body{\r\n" + //
                "            height: 100vh;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        h1, h2, h3 {\r\n" + //
                "            color: #237acc;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        h1{\r\n" + //
                "            border-bottom: 1px solid #aaa;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        .main{\r\n" + //
                "            width: 670px;\r\n" + //
                "            margin: 20px auto;\r\n" + //
                "            padding: 50px;\r\n" + //
                "            border: 20px solid #237acc;\r\n" + //
                "\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        .infoContato{\r\n" + //
                "            margin-bottom: 10px;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        .infoProfisional > div > h2 {\r\n" + //
                "            padding: 0px 0px 5px 0px;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        .infoProfisional > div > h3 {\r\n" + //
                "            padding: 0px 0px 5px 0px;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        .infoProfisional > div{\r\n" + //
                "            padding: 10px 0px;\r\n" + //
                "            margin-bottom: 5px;\r\n" + //
                "            border-bottom: 1px solid #aaa;\r\n" + //
                "        }\r\n" + //
                "    </style>\r\n" + //
                "</head>\r\n" + //
                "<body>\r\n" + //
                "    <div class=\"main\">\r\n" + //
                "        <div class=\"infoContato\">\r\n" + //
                "            <h1>{NOMECOMPLETO}</h1>\r\n" + //
                "            <p>{EMAIL}</p>\r\n" + //
                "            <p>{NUMTELEFONE}</p>\r\n" + //
                "            <p>{CIDADE}/{UF}</p>\r\n" + //
                "        </div>\r\n" + //
                "        <div class=\"infoProfisional\">\r\n" + //
                "            <div>\r\n" + //
                "                <h3>Objetivos</h3>\r\n" + //
                "                <div>\r\n" + //
                "                    <p>{OBJETIVOS}</p>\r\n" + //
                "                </div>\r\n" + //
                "            </div>\r\n" + //
                "            <div>\r\n" + //
                "                <h3>Habilidades</h3>\r\n" + //
                "                <div>\r\n" + //
                "                    <p>{HABILIDADES}</p>\r\n" + //
                "                </div>\r\n" + //
                "            </div>\r\n" + //
                "            <div>\r\n" + //
                "                <h3>Experi\u00EAncia</h3>\r\n" + //
                "                <div>\r\n" + //
                "                    <p>{EXPERIENCIA}</p>\r\n" + //
                "                </div>\r\n" + //
                "            </div>\r\n" + //
                "            <div>\r\n" + //
                "                <h3>Educa\u00E7\u00E3o</h3>\r\n" + //
                "                <div>\r\n" + //
                "                    <p>{EDUCACAO}</p>\r\n" + //
                "                </div>\r\n" + //
                "            </div>\r\n" + //
                "            <div>\r\n" + //
                "                <h3>Projetos</h3>\r\n" + //
                "                <div>\r\n" + //
                "                    <p>{PROJETOS}</p>\r\n" + //
                "                </div>\r\n" + //
                "            </div>\r\n" + //
                "            <div>\r\n" + //
                "                <h3>Cursos Complementares</h3>\r\n" + //
                "                <div>\r\n" + //
                "                    <p>{CURSOSCOMPLEMENTARES}</p>\r\n" + //
                "                </div>\r\n" + //
                "            </div>\r\n" + //
                "        </div>\r\n" + //
                "    </div>\r\n" + //
                "</body>\r\n" + //
                "</html>");

        String curriculo = htmlBuilder.toString();

        String nomeCompleto = aluno.getNomeCompleto();
        String email = aluno.getEmail();
        String numTelefone = aluno.getNumTelefone();
        String cidade = aluno.getCidade();
        String uf = aluno.getUf();
        String objetivos = aluno.getObjetivos();
        String habilidades = aluno.getHabilidades();
        String experiencia = aluno.getExperiencia();
        String educacao = aluno.getEducacao();
        String projetos = aluno.getProjetos();
        String cursosComplementares = aluno.getCursosComplementares();

        curriculo = curriculo.replace("{NOMECOMPLETO}", nomeCompleto != null ? nomeCompleto : "");
        curriculo = curriculo.replace("{EMAIL}", email != null ? email : "");
        curriculo = curriculo.replace("{NUMTELEFONE}", numTelefone != null ? numTelefone : "");
        curriculo = curriculo.replace("{CIDADE}", cidade != null ? cidade : "");
        curriculo = curriculo.replace("{UF}", uf != null ? uf : "");
        curriculo = curriculo.replace("{OBJETIVOS}", objetivos != null ? objetivos : "");
        curriculo = curriculo.replace("{HABILIDADES}", habilidades != null ? habilidades : "");
        curriculo = curriculo.replace("{EXPERIENCIA}", experiencia != null ? experiencia : "");
        curriculo = curriculo.replace("{EDUCACAO}", educacao != null ? educacao : "");
        curriculo = curriculo.replace("{PROJETOS}", projetos != null ? projetos : "");
        curriculo = curriculo.replace("{CURSOSCOMPLEMENTARES}", cursosComplementares != null ? cursosComplementares : "");

        return curriculo;
    }
}
