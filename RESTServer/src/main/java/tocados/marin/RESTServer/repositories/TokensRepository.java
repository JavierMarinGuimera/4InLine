package tocados.marin.RESTServer.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tocados.marin.RESTServer.models.Token;

@Repository
public interface TokensRepository extends CrudRepository<Token, Integer> {
    
}
