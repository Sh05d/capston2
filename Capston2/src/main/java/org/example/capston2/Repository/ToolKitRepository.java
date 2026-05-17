package org.example.capston2.Repository;

import org.example.capston2.Model.ToolKit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolKitRepository extends JpaRepository<ToolKit,Integer> {
    ToolKit findToolKitsById(Integer id);

    @Query("select t from ToolKit t where t.category=?1 and t.pricePerDay<=?2")
    List<ToolKit> toolKitByCategoryAndPriceRange(String category, Integer maxPrice);

    @Query("select t from ToolKit t where t.category=?1 and t.quantity>=?2")
    List<ToolKit> toolKitByCategoryAndQuantity(String category, Integer quantity);

    @Query("select t from ToolKit t where t.category=?1 and t.pickupMethod=?2")
    List<ToolKit> toolKitByCategoryAndPickupMethod(String category, String pickupMethod);

    ToolKit findToolKitsByIdAndStudioId(Integer id, Integer studioId);
}
