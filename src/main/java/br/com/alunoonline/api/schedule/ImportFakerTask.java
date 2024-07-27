package br.com.alunoonline.api.schedule;

import br.com.alunoonline.api.client.AlunoImportClient;
import br.com.alunoonline.api.dtos.AlunoFakerResponseDTO;
import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Endereco;
import br.com.alunoonline.api.service.AlunoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImportFakerTask {

    private final AlunoService service;
    private final AlunoImportClient client;

    //@Scheduled(fixedRate = 60000)
    private void importarAlunos(){
        var listaAluno = new ArrayList<Aluno>();
        log.info("Start - ImportFaker");
        List<AlunoFakerResponseDTO> lista = client.getListaAlunos();
        for(AlunoFakerResponseDTO alunofaker: lista){
            var endereco = new Endereco();
            endereco.setNumero(alunofaker.getNumeroCasa());
            endereco.setCep(alunofaker.getCep());
            Aluno aluno = Aluno.builder()
                    .name(alunofaker.getName())
                    .anoNascimento(alunofaker.getAnoNascimento())
                    .cpf(alunofaker.getCpf())
                    .email(alunofaker.getEmail())
                    .endereco(endereco)
                    .build();
            service.atualizaEnderecoPorCep(aluno);
            listaAluno.add(aluno);
        }
        service.createAll(listaAluno);
    }
}
