package org.example.capston2.Repository;

import org.example.capston2.Model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudioRepository extends JpaRepository<Studio,Integer> {
    Studio findStudioById(Integer id);

    @Query("select s from Studio s where s.city=?1 order by s.rate DESC, s.numberOfRating DESC")
    List<Studio> topStudioInCity(String city);
}
