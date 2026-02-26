package com.teste.primeiro_exemplo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.primeiro_exemplo.model.Produto;
import com.teste.primeiro_exemplo.model.exception.ResourceNotFoundException;
import com.teste.primeiro_exemplo.repository.ProdutoRepository;
import com.teste.primeiro_exemplo.shared.ProdutoDTO;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ProdutoDTO> obterTodos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<ProdutoDTO> obterPorId(Integer id) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isEmpty()) {
            throw new ResourceNotFoundException("Produto com id: " + id + " não encontrado");
        }

        ProdutoDTO dto = modelMapper.map(produto.get(), ProdutoDTO.class);
        return Optional.of(dto);
    }

    // --- CORREÇÃO AQUI: Retorna ProdutoDTO ---
    public ProdutoDTO adicionar(ProdutoDTO produtoDto) {
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
            throw new ResourceNotFoundException(
                    "Não é possível deletar o produto com o id: " + id + " - Produto não existe");
        }
        produtoRepository.deleteById(id);
    }

    // --- CORREÇÃO AQUI: Retorna ProdutoDTO ---
    public ProdutoDTO atualizar(Integer id, ProdutoDTO produtoDto) {
        produtoDto.setId(id);
        Produto produto = modelMapper.map(produtoDto, Produto.class);
        produtoRepository.save(produto);
        return produtoDto;
    }
}