package com.example.rdb.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CassandraCommonRepository extends CassandraRepository<CassandraReceipt, String> {
    List<CassandraReceipt> findAllByCustomerId(String customerId);
}
