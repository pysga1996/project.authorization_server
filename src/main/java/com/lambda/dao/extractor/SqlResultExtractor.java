package com.lambda.dao.extractor;

import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;
import java.util.Optional;

public interface SqlResultExtractor<T> {

    ResultSetExtractor<Optional<T>> singleExtractor();

    ResultSetExtractor<List<T>> listExtractor();
}
