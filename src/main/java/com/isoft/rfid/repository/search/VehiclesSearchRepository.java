package com.isoft.rfid.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.isoft.rfid.domain.Vehicles;
import com.isoft.rfid.repository.VehiclesRepository;
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
 * Spring Data Elasticsearch repository for the {@link Vehicles} entity.
 */
public interface VehiclesSearchRepository extends ElasticsearchRepository<Vehicles, Long>, VehiclesSearchRepositoryInternal {}

interface VehiclesSearchRepositoryInternal {
    Page<Vehicles> search(String query, Pageable pageable);

    Page<Vehicles> search(Query query);

    void index(Vehicles entity);
}

class VehiclesSearchRepositoryInternalImpl implements VehiclesSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final VehiclesRepository repository;

    VehiclesSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, VehiclesRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Vehicles> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Vehicles> search(Query query) {
        SearchHits<Vehicles> searchHits = elasticsearchTemplate.search(query, Vehicles.class);
        List<Vehicles> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Vehicles entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
