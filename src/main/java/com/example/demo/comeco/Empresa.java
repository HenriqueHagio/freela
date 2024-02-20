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
@Table(name = "empresa")
@Getter @Setter
public class Empresa {

    /**
     * Chave primária em UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    protected UUID codigo;

    private String nome;

//    public Empresa(String nome ) {
//        this.nome = nome;
//
//    }
public Empresa buscarPessoaPorNome(String nome) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Criteria criteria = session.createCriteria(Empresa.class);
    criteria.add(Restrictions.eq("nome", nome));
    Empresa empresa = (Empresa) criteria.uniqueResult();
    session.close();
    return empresa;
}

    public Empresa() {

    }

}
