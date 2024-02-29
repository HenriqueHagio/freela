package com.example.demo.Lubrificantes;

import com.example.demo.Hibernate.HibernateUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Criteria;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "lubrificante")
@Getter
@Setter
public class Lubrificante {
    /**
     * Chave primária em UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    protected int codigo;
    private String descricao;


    public Lubrificante(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Lubrificante() {

    }

    public void setText(String descricao) {
    }

    public List<Lubrificante> recuperarTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Lubrificante.class);
        List<Lubrificante> lubrificantes =  criteria.list();
        session.close();
        return lubrificantes;
    }


}
