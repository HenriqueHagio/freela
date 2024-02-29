package com.example.demo.Hibernate;

import java.util.List;

public interface Entidade<T> {
    void salvar(T entidade);

    void salvarAdicional(List<T> entidades);
}

