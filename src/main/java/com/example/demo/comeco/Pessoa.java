package com.example.demo.comeco;
import com.example.demo.Hibernate.HibernateUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "pessoa")
@Getter @Setter
public class Pessoa {

    /**
     * Chave primária em UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    protected UUID codigo;

    public Pessoa(String nome, String sobrenome, String email, Empresa empresa) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.empresa = empresa;
    }

    private String nome;

    private String sobrenome;

    private String email;

    @ManyToOne
    private Empresa empresa;

    public Pessoa buscarPessoaPorEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Pessoa.class);
        criteria.add(Restrictions.eq("email", email));
        Pessoa pessoa = (Pessoa) criteria.uniqueResult();
        session.close();
        return pessoa;
    }

    public Pessoa() {

    }

}
