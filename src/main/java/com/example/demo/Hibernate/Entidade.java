package com.example.demo.Hibernate;

public interface Entidade<T> {
    void salvar(T entidade);


    void atualizar(T entidade);

    void apagar(T entidade);
}

