package com.isoft.rfid.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.isoft.rfid.domain.EventsImages;
import com.isoft.rfid.repository.EventsImagesRepository;
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
 * Spring Data Elasticsearch repository for the {@link EventsImages} entity.
 */
public interface EventsImagesSearchRepository extends ElasticsearchRepository<EventsImages, Long>, EventsImagesSearchRepositoryInternal {}

interface EventsImagesSearchRepositoryInternal {
    Page<EventsImages> search(String query, Pageable pageable);

    Page<EventsImages> search(Query query);

    void index(EventsImages entity);
}

class EventsImagesSearchRepositoryInternalImpl implements EventsImagesSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final EventsImagesRepository repository;

    EventsImagesSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, EventsImagesRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<EventsImages> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<EventsImages> search(Query query) {
        SearchHits<EventsImages> searchHits = elasticsearchTemplate.search(query, EventsImages.class);
        List<EventsImages> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(EventsImages entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
