package com.example.lms.repository;

import com.example.lms.model.LeaveRequest;
import com.example.lms.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("""
        SELECT lr FROM LeaveRequest lr
        WHERE lr.employee.id = :empId
          AND lr.status IN (:statuses)
          AND NOT (lr.endDate < :start OR lr.startDate > :end)
    """)
    List<LeaveRequest> findOverlapping(@Param("empId") Long empId,
                                       @Param("start") LocalDate start,
                                       @Param("end") LocalDate end,
                                       @Param("statuses") List<LeaveStatus> statuses);
}
