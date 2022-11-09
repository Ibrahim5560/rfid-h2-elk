package com.isoft.rfid.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.isoft.rfid.domain.Events;
import com.isoft.rfid.repository.EventsRepository;
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
 * Spring Data Elasticsearch repository for the {@link Events} entity.
 */
public interface EventsSearchRepository extends ElasticsearchRepository<Events, Long>, EventsSearchRepositoryInternal {}

interface EventsSearchRepositoryInternal {
    Page<Events> search(String query, Pageable pageable);

    Page<Events> search(Query query);

    void index(Events entity);
}

class EventsSearchRepositoryInternalImpl implements EventsSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final EventsRepository repository;

    EventsSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, EventsRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Events> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Events> search(Query query) {
        SearchHits<Events> searchHits = elasticsearchTemplate.search(query, Events.class);
        List<Events> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Events entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
