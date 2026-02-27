package com.teste.primeiro_exemplo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.primeiro_exemplo.model.Produto;
import com.teste.primeiro_exemplo.model.exception.Temp2;
import com.teste.primeiro_exemplo.repository.ProdutoRepository;
import com.teste.primeiro_exemplo.shared.Temp1;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Temp1> obterTodos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream()
                .map(produto -> modelMapper.map(produto, Temp1.class))
                .collect(Collectors.toList());
    }

    public Optional<Temp1> obterPorId(Integer id) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isEmpty()) {
            throw new Temp2("Produto com id: " + id + " não encontrado");
        }

        Temp1 dto = modelMapper.map(produto.get(), Temp1.class);
        return Optional.of(dto);
    }

    // --- CORREÇÃO AQUI: Retorna ProdutoDTO ---
    public Temp1 adicionar(Temp1 produtoDto) {
        // Remove o id para garantir que é um cadastro novo
        produtoDto.setId(null);

        // Cria objeto de mapeamento
        Produto produto = modelMapper.map(produtoDto, Produto.class);

        // Salva no banco
        produto = produtoRepository.save(produto);

        // Atualiza o DTO com o novo ID e retorna
        produtoDto.setId(produto.getId());
        return produtoDto;
    }

    // --- CORREÇÃO AQUI: Lógica de deletar ---
    public void deletar(Integer id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isEmpty()) {
            throw new Temp2(
                    "Não é possível deletar o produto com o id: " + id + " - Produto não existe");
        }
        produtoRepository.deleteById(id);
    }

    // --- CORREÇÃO AQUI: Retorna ProdutoDTO ---
    public Temp1 atualizar(Integer id, Temp1 produtoDto) {
        produtoDto.setId(id);
        Produto produto = modelMapper.map(produtoDto, Produto.class);
        produtoRepository.save(produto);
        return produtoDto;
    }
}