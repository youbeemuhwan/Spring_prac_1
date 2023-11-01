package com.practice.simpleWeb.Repository;

import com.practice.simpleWeb.domain.BlackListToken;
import org.springframework.data.repository.CrudRepository;

public interface BlackListTokenRepository extends CrudRepository<BlackListToken, String> {
}
