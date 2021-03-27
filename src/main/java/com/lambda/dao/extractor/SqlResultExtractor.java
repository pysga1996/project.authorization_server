package com.lambda.dao.extractor;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SqlResultExtractor<T> {

    ResultSetExtractor<Optional<T>> singleExtractor();

    default ResultSetExtractor<List<T>> customListExtractor() {
        return rs -> new ArrayList<>();
    }

    default RowMapper<T> listExtractor() {
        return (rs, rowNum) -> null;
    }
}
