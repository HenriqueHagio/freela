package com.example.demo.Estoque;

import com.example.demo.Cadastro.Constante.UnidadeMedida;
import com.example.demo.Hibernate.HibernateUtil;
import com.example.demo.Lubrificantes.Lubrificante;
import com.example.demo.comeco.Empresa;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "produto")
@Getter
@Setter
public class Produto {
    /**
     * Chave primária em UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    protected UUID codigo;

    private Double quantidade;

    private UnidadeMedida unidadeMedida;

    @ManyToOne
    private Lubrificante lubrificante;

    @ManyToOne(optional = true)
    private Empresa empresa;

    public Produto recuperarPorLubrificante(Lubrificante lubrificante) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Produto.class);
        criteria.add(Restrictions.eq("lubrificante", lubrificante));
        Produto produto = (Produto) criteria.uniqueResult();
        session.close();
        return produto;
    }
    public List<Lubrificante> recuperarLubrificantePorEmpresa(Empresa empresa) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Produto.class, "produto");
        criteria.createAlias("produto.lubrificante", "lubrificante", JoinType.LEFT_OUTER_JOIN);
        criteria.add(Restrictions.eq("produto.empresa", empresa));
        criteria.add(Restrictions.isNotNull("lubrificante"));
        List<Lubrificante> lubrificantes = criteria.setProjection(Projections.property("lubrificante")).list();
        session.close();
        return lubrificantes;
    }





}
