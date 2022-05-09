package tocados.marin.RESTServer.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tocados.marin.RESTServer.models.User;

@Repository
public interface UsersRepository extends CrudRepository<User, Integer> {
    public abstract User findByUsername(String username);
}
