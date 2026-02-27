package com.teste.primeiro_exemplo.view.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.primeiro_exemplo.services.ProdutoService;
import com.teste.primeiro_exemplo.shared.Temp1;
import com.teste.primeiro_exemplo.view.model.ProdutoRequest;
import com.teste.primeiro_exemplo.view.model.ProdutoResponse;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> obterTodos() {
        List<Temp1> produtos = produtoService.obterTodos();

        List<ProdutoResponse> resposta = produtos.stream()
                .map(produtoDto -> modelMapper.map(produtoDto, ProdutoResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProdutoResponse>> obterPorId(@PathVariable Integer id) {
        Optional<Temp1> produto = produtoService.obterPorId(id);

        Optional<ProdutoResponse> resposta = produto.map(dto -> modelMapper.map(dto, ProdutoResponse.class));

        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> adicionar(@RequestBody ProdutoRequest produtoReq) {
        // Converte Request em DTO
        Temp1 dtoRequest = modelMapper.map(produtoReq, Temp1.class);

        // Manda pro Service salvar e pega o retorno
        Temp1 dtoResponse = produtoService.adicionar(dtoRequest);

        // Converte o DTO salvo em Response
        ProdutoResponse resposta = modelMapper.map(dtoResponse, ProdutoResponse.class);

        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        produtoService.deletar(id);

        // Deleta e devolve status 204 (Sem Conteúdo)
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@RequestBody ProdutoRequest produtoReq, @PathVariable Integer id) {
        // Converte Request em DTO
        Temp1 dtoRequest = modelMapper.map(produtoReq, Temp1.class);

        // Manda pro Service atualizar
        Temp1 dtoResponse = produtoService.atualizar(id, dtoRequest);

        // Converte o DTO atualizado em Response
        ProdutoResponse resposta = modelMapper.map(dtoResponse, ProdutoResponse.class);

        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }
}