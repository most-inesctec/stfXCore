package stfXCore.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stfXCore.Models.Storyboard;

/**
 * JPA Repository is the interface responsible for:
 * Creating new instances
 * Updating existing ones
 * Deleting
 * Finding (one, all, by simple or complex properties)
 */
public interface StoryboardRepository extends JpaRepository<Storyboard, Long> {

}