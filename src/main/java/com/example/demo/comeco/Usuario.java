package com.example.demo.comeco;

import com.example.demo.Hibernate.HibernateUtil;
import javafx.collections.ObservableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter @Setter
public class Usuario {

    /**
     * Chave primária em UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    protected UUID codigo;

    private String username;
    private String password;
    private String role;

    @ManyToOne
    private Pessoa pessoa;


    public Usuario(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Usuario() {

    }
    public Usuario buscarUsuarioPorNome(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Usuario.class);
        criteria.add(Restrictions.eq("username", username));
        Usuario usuario = (Usuario) criteria.uniqueResult();
        session.close();
       return usuario;
    }

    public Usuario buscarUsuarioPorPessoa(Pessoa pessoa) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Usuario.class);
        criteria.add(Restrictions.eq("pessoa", pessoa));
        Usuario usuario = (Usuario) criteria.uniqueResult();
        session.close();
       return usuario;
    }

    public ObservableList<Usuario> recuperarTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Usuario.class);
        ObservableList<Usuario> usuarios = (ObservableList<Usuario>) criteria.list();
        session.close();
        return usuarios;
    }

    public void salvar() {
    }
}
