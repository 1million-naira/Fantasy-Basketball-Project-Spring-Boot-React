package org.example.newtopsquadproject.Repository.FantasyLeagues;

import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface UserLeagueRepo extends CrudRepository<UserLeague, Integer>, PagingAndSortingRepository<UserLeague, Integer> {
}
