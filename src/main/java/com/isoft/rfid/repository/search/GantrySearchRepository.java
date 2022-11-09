package com.isoft.rfid.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.isoft.rfid.domain.Gantry;
import com.isoft.rfid.repository.GantryRepository;
import java.util.List;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data Elasticsearch repository for the {@link Gantry} entity.
 */
public interface GantrySearchRepository extends ElasticsearchRepository<Gantry, Long>, GantrySearchRepositoryInternal {}

interface GantrySearchRepositoryInternal {
    Page<Gantry> search(String query, Pageable pageable);

    Page<Gantry> search(Query query);

    void index(Gantry entity);
}

class GantrySearchRepositoryInternalImpl implements GantrySearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final GantryRepository repository;

    GantrySearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, GantryRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Gantry> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Gantry> search(Query query) {
        SearchHits<Gantry> searchHits = elasticsearchTemplate.search(query, Gantry.class);
        List<Gantry> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Gantry entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
