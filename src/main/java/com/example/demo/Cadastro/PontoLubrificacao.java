package com.example.demo.Cadastro;

import com.example.demo.Cadastro.Constante.UnidadeMedida;
import com.example.demo.Hibernate.HibernateUtil;
import com.example.demo.Lubrificantes.Lubrificante;
import com.example.demo.comeco.Empresa;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pontoLubrificacao")
@Getter
@Setter
public class PontoLubrificacao {
    /**
     * Chave primária em UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    protected UUID codigo;

    private String setor;

    private String ponto;

    private String equipamento;

    private String tag;

    @ManyToOne
    private Empresa empresa;

    private LocalDateTime dataHoraLubrificacao;

    private LocalDate dataProxLubrificacao;

    private Integer quantidadeDeGraxa;

    private Integer quantidadeDeLubrificante;

    private UnidadeMedida unidadeMedida;

    @Column(name = "componentes_do_equipamento")
    private String componentes;

    private String operacao;

    @Column(name = "observacaoes")
    private String obs;

    @ManyToOne
    private Lubrificante lubrificante;

    public List<PontoLubrificacao> buscarPontosPorEmpresa(Empresa empresa) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(PontoLubrificacao.class);
        criteria.add(Restrictions.eq("empresa", empresa));
        List<PontoLubrificacao> pontos =  criteria.list();
        session.close();
        return pontos;

    }

    public List<PontoLubrificacao> buscarPontosPorEmpresaLimit(Empresa empresa) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(PontoLubrificacao.class);
        criteria.add(Restrictions.eq("empresa", empresa));
        criteria.add(Restrictions.gt("dataProxLubrificacao", LocalDate.now())); // Usa LocalDate para a comparação de datas
        criteria.addOrder(Order.desc("dataProxLubrificacao")); // Ordena pela data de próxima lubrificação de forma decrescente
        criteria.setMaxResults(5); // Limita a consulta aos 5 resultados mais recentes
        List<PontoLubrificacao> pontos = criteria.list();
        session.close();
        return pontos;
    }





}
