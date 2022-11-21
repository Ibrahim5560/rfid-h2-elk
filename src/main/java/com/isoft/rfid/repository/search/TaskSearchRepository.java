package com.isoft.rfid.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.isoft.rfid.domain.Task;
import com.isoft.rfid.repository.TaskRepository;
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
 * Spring Data Elasticsearch repository for the {@link Task} entity.
 */
public interface TaskSearchRepository extends ElasticsearchRepository<Task, Long>, TaskSearchRepositoryInternal {}

interface TaskSearchRepositoryInternal {
    Page<Task> search(String query, Pageable pageable);

    Page<Task> search(Query query);

    void index(Task entity);
}

class TaskSearchRepositoryInternalImpl implements TaskSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final TaskRepository repository;

    TaskSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, TaskRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Task> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery.setPageable(pageable));
    }

    @Override
    public Page<Task> search(Query query) {
        SearchHits<Task> searchHits = elasticsearchTemplate.search(query, Task.class);
        List<Task> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Task entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
