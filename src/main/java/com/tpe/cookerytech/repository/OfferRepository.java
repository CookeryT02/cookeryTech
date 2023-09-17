package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Offer;
import com.tpe.cookerytech.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import com.tpe.cookerytech.dto.response.OfferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    static boolean existByUser(User user) {
        return false;
    }

//    @Query("SELECT o FROM Offer o WHERE (:uId IS NULL OR o.user_id = :uId)) "
//            + "AND (:o IS NULL OR o.status = :o) "
//            + "AND (:dt1 IS NULL OR o.createAt < :dt1) "
//            + "AND (:dt2 IS NULL OR o.createAt > :dt2)")

//    @Query("SELECT o FROM Offer o " +
//            "JOIN FETCH User u on o.user=u.id WHERE " +
//            "u.id=:user_id and (o.createAt not in :createAt) and :dt1 BETWEEN o.dt1 and o.dt2 " +
//            "or " +
//            "u.id=:user_id and (o.createAt not in :createAt) and :dt2 BETWEEN o.dt1 and o.dt2 " +
//            "or " +
//            "u.id=:user_id and (o.createAt not in :createAt) and (o.dt1 BETWEEN :dt1 and :dt2)")
//    @EntityGraph(attributePaths = {"offer","offer.user_id"})

//    @Query("SELECT o FROM Offer o WHERE (:uId IS NULL OR o.user_id = :uId)"
//            + " AND (:o IS NULL OR o.status = :o) "
//            + " AND ()

//    @Query("SELECT * FROM Offer o WHERE o.user.id = :uId AND status = :status AND create_at BETWEEN :dt1 AND :dt2" + "ORDER BY create_at DESC")


    @Query("SELECT o FROM Offer o WHERE (:id IS NULL OR o.user.id = :id) " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND DATE(o.createAt) BETWEEN :date1 AND :date2 ORDER BY o.createAt")
    Page<Offer> getAllUserOfferById( Long id, Pageable pageable, byte status, LocalDate date1, LocalDate date2);

//    @Query("SELECT o FROM Offer o WHERE (:q IS NULL OR o.code LIKE %:q%) AND " +
//            "DATE(o.createAt) BETWEEN :date1 AND :date2 ORDER BY o.createAt")
//    Page<Offer> findByCreateAtBetweenOrderByCreateAt(String q, LocalDate date1, LocalDate date2, Pageable pageable);


//    @EntityGraph(attributePaths = {"user"})
    @EntityGraph(attributePaths = "id")
    List<Offer> findByUser(Long id);




    /*

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH Car c on r.car=c.id WHERE " +
            "c.id=:carId and (r.status not in :status) and :pickUpTime BETWEEN r.pickUpTime and r.dropOfTime " +
            "or " +
            "c.id=:carId and (r.status not in :status) and :dropOfTime BETWEEN r.pickUpTime and r.dropOfTime " +
            "or " +
            "c.id=:carId and (r.status not in :status) and (r.pickUpTime BETWEEN :pickUpTime and :dropOfTime)")
    List<Reservation> checkCarStatus(@Param("carId") Long carId,
                                     @Param("pickUpTime") LocalDateTime pickUpTime,
                                     @Param("dropOfTime") LocalDateTime dropOfTime,
                                     @Param("status") ReservationStatus[] status);


    */


    /*	 @Query("SELECT p FROM Product p WHERE " +
	            "(LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR LOWER(p.short_desc) LIKE CONCAT('%', LOWER(:q), '%') OR LOWER(p.long_desc) LIKE CONCAT('%', LOWER(:q), '%')) " +
	            "AND (:cId IS NULL OR p.category.id = :cId) " +
	            "AND (:bId IS NULL OR p.brand.id = :bId) " +
	            "AND (:pr1 IS NULL OR p.price >= :pr1) " +
	            "AND (:pr2 IS NULL OR p.price <= :pr2) "   )
	 Page<Product> findProductsByCriteria( Pageable pageable,
			 @Param("q") String query, @Param("cId") Long categoryId,
				@Param("bId") Long brandId, @Param("pr1") Double price1, @Param("pr2") Double price2);
*/

//    List<Offer> findByUserIdOffer();


    @EntityGraph(attributePaths = "id")
    List<Offer> findByUserId(Long id);

    @Query("SELECT o " +
            "FROM Offer o " +
            "JOIN User u ON o.user.id = u.id " +
            "WHERE " +
            "(:q IS NULL OR u.firstName LIKE CONCAT('%', :q, '%') OR u.lastName LIKE CONCAT('%', :q, '%') OR o.code LIKE CONCAT('%', :q, '%')) ")
    Page<Offer> findFilteredOffers(
            @Param("q") String q,
            Pageable pageable
    );

}